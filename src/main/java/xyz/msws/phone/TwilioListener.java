package xyz.msws.phone;

import com.twilio.twiml.MessagingResponse;

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
}
