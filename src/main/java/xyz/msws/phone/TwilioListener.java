package xyz.msws.phone;

import com.twilio.twiml.MessagingResponse;
import org.apache.commons.io.FileUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.apache.tika.mime.MimeTypes;

import java.io.File;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static spark.Spark.post;

/**
 * Listens for Twilio messages and relays them to the bot
 * Returns an empty TwiML response
 */
public class TwilioListener {

    private final IPhoneBot bot;

    public TwilioListener(IPhoneBot bot, String webhook) {
        this.bot = bot;

        post("/" + webhook, (req, res) -> {
            try {
                Map<String, String> parameters = parseBody(req.body());
                String numMediaStr = parameters.get("NumMedia");
                int numMedia = Integer.parseInt(numMediaStr);

                List<File> attachments = new ArrayList<>();
                if (numMedia > 0) while (numMedia > 0) {
                    numMedia = numMedia - 1;

                    // Get all info
                    String mediaUrl = parameters.get(String.format("MediaUrl%d", numMedia));
                    String contentType = parameters.get(String.format("MediaContentType%d", numMedia));
                    String fileName = mediaUrl.substring(mediaUrl.lastIndexOf("/") + 1);
                    String fileExtension = MimeTypes.getDefaultMimeTypes().forName(contentType).getExtension();
                    File file = new File(fileName + fileExtension);

                    // Download file
                    URL url = new URL(mediaUrl);
                    CloseableHttpClient httpclient = HttpClients.custom().setRedirectStrategy(new LaxRedirectStrategy()).build();
                    HttpGet get = new HttpGet(url.toURI());
                    HttpResponse response = httpclient.execute(get);
                    InputStream source = response.getEntity().getContent();
                    FileUtils.copyInputStreamToFile(source, file);
                    attachments.add(file);
                }


                String body = parameters.get("Body");
                String from = parameters.get("From");

                if (attachments.isEmpty()) {
                    bot.relayMessage(from, body);
                } else {
                    bot.relayMessage(from, body, attachments);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            res.type("application/xml");
            MessagingResponse twiml = new MessagingResponse.Builder().build();
            return twiml.toXml();
        });
    }

    // Body parser help
    public static Map<String, String> parseBody(String body) throws UnsupportedEncodingException {
        String[] unparsedParams = body.split("&");
        Map<String, String> parsedParams = new HashMap<String, String>();
        for (int i = 0; i < unparsedParams.length; i++) {
            String[] param = unparsedParams[i].split("=");
            if (param.length == 2) {
                parsedParams.put(urlDecode(param[0]), urlDecode(param[1]));
            } else if (param.length == 1) {
                parsedParams.put(urlDecode(param[0]), "");
            }
        }
        return parsedParams;
    }

    public static String urlDecode(String s) throws UnsupportedEncodingException {
        return URLDecoder.decode(s, "utf-8");
    }
}
