package xyz.msws.phone;

import com.twilio.twiml.MessagingResponse;
import com.twilio.twiml.messaging.Body;
import com.twilio.twiml.messaging.Message;

import static spark.Spark.*;

public class TwilioListener {

    private IPhoneBot bot;

    public TwilioListener(IPhoneBot bot, String webhook) {
        this.bot = bot;

//        get("/", (req, res) -> "Hello Web");

        post("/" + webhook, (req, res) -> {
            String body = req.queryParams("Body");
            String from = req.queryParams("From");

            bot.relayMessage(from, body);

            res.type("application/xml");
            MessagingResponse twiml = new MessagingResponse.Builder().build();
            return twiml.toXml();
        });
    }
}
