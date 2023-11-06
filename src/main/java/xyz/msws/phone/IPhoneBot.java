package xyz.msws.phone;

import java.io.File;
import java.util.List;

/**
 * Responsible for interacting from SMS <-> Discord
 */
public interface IPhoneBot {
    Messenger getMessenger();

    void relayMessage(String number, String message);

    void relayMessage(String number, String message, List<File> attachments);
}
