package xyz.msws.phone;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

public class TwilioMessenger implements Messenger {

    public static final String ACCOUNT_SID = System.getenv("TWILIO_ACCOUNT_SID");
    public static final String AUTH_TOKEN = System.getenv("TWILIO_AUTH_TOKEN");
    public static final String SENDER = System.getenv("TWILIO_SENDER");

    public TwilioMessenger() {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
    }

    @Override
    public void sendMessage(String number, String message) {
        if (message == null || message.isEmpty())
            return;
        Message.creator(new PhoneNumber(number), new PhoneNumber(SENDER), message).create();
    }

}
