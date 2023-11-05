package xyz.msws.phone;

import com.twilio.twiml.MessagingResponse;

import javax.servlet.ServletException;

import static spark.Spark.*;

public class Main {
    public static void main(String[] args) throws ServletException {
        final String token = System.getenv("DISCORD_TOKEN");
        final String webhook = System.getenv("TWILIO_WEBHOOK");
        IPhoneBot bot = new PhoneBot(token);

        new TwilioListener(bot, webhook);
    }
}
