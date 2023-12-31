package xyz.msws.phone;

/**
 * Represents a messenger that can send SMS messages
 */
public interface Messenger {

    /**
     * Sends a message to the given number
     * Each number should be in the format +1XXXXXXXXXX
     *
     * @param number  The number to send the message to
     * @param message The message to send
     */
    void sendMessage(String number, String message);
}
