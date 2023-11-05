package xyz.msws.phone;

import com.twilio.twiml.MessagingResponse;
import com.twilio.twiml.messaging.Body;
import com.twilio.twiml.messaging.Message;

import static spark.Spark.*;

public class TwilioListener {

    private IPhoneBot bot;

    public TwilioListener(IPhoneBot bot) {
        this.bot = bot;

        get("/", (req, res) -> "Hello Web");

        post("/sms", (req, res) -> {
            String body = req.queryParams("Body");
            String from = req.queryParams("From");
            System.out.println("Received message: " + body);
            System.out.println("From: " + from);

            bot.relayMessage(from, body);

            res.type("application/xml");
            MessagingResponse twiml = new MessagingResponse.Builder().build();
            return twiml.toXml();
        });
    }
}
