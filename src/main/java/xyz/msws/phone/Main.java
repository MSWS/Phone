package xyz.msws.phone;

import com.twilio.twiml.MessagingResponse;

import static spark.Spark.*;

public class Main {
    public static void main(String[] args) {
        final String token = System.getenv("DISCORD_TOKEN");

        post("/sms", (req, res) -> {
            System.out.println(req.body());

            res.type("application/xml");
            MessagingResponse twiml = new MessagingResponse.Builder().build();
            return twiml.toXml();
        });

        new PhoneBot(token);
    }
}
