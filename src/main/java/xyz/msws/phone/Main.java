package xyz.msws.phone;

public class Main {
    public static void main(String[] args) {
        final String token = System.getenv("DISCORD_TOKEN");
        new PhoneBot(token);
    }
}
