package xyz.msws.phone;

import javax.servlet.ServletException;

public class Main {
    public static void main(String[] args) throws ServletException {
        final String token = System.getenv("DISCORD_TOKEN");
        final String webhook = System.getenv("TWILIO_WEBHOOK");
        final IPhoneBot bot = new PhoneBot(token);

        new TwilioListener(bot, webhook);
    }
}
