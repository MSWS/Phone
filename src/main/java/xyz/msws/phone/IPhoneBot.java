package xyz.msws.phone;

public interface IPhoneBot {
    Messenger getMessenger();

    void relayMessage(String number, String message);
}
