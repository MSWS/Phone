package xyz.msws.phone;

import com.twilio.twiml.MessagingResponse;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
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
            String body = req.queryParams("Body");
            String from = req.queryParams("From");

            bot.relayMessage(from, body);

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
