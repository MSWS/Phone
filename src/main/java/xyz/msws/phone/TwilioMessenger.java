package xyz.msws.phone;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class TwilioMessenger implements Messenger {

    public static final String ACCOUNT_SID = System.getenv("TWILIO_ACCOUNT_SID");
    public static final String AUTH_TOKEN = System.getenv("TWILIO_AUTH_TOKEN");
    public static final String SENDER = System.getenv("TWILIO_SENDER");

    public TwilioMessenger() {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
    }

    @Override
    public void sendMessage(String number, String message) {
        Message.creator(new PhoneNumber(number), new PhoneNumber(SENDER), message).create();
    }

    @Override
    public void sendMessage(String number, String message, List<File> attachment) {
        List<URI> urls = new ArrayList<>();
        for (File attach : attachment) {
            urls.add(attach.toURI());
        }
        if (message == null) {
            Message.creator(new PhoneNumber(number), new PhoneNumber(SENDER), urls).create();
        } else {
            Message.creator(new PhoneNumber(number), new PhoneNumber(SENDER), message).setMediaUrl(urls).create();
        }
    }
}
