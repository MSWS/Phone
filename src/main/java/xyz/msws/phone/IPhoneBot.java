package xyz.msws.phone;

/**
 * Responsible for interacting from SMS <-> Discord
 */
public interface IPhoneBot {
    Messenger getMessenger();

    void relayMessage(String number, String message);
}
