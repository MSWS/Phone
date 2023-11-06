package xyz.msws.phone;

/**
 * Responsible for sanitizing phone numbers to E.164 format
 */
public class NumberSanitizer {
    public static String formatToE164(String phoneNumber) {
        // Remove all non-digit characters from the phone number
        String cleanNumber = phoneNumber.replaceAll("[^\\d]", "");

        // Prepend with country code 1 if it does not start with it
        if (!cleanNumber.startsWith("1"))
            cleanNumber = "1" + cleanNumber;

        // Prepend with '+' to comply with E.164 format
        return "+" + cleanNumber;
    }

    public static boolean isValidNumber(String phoneNumber) {
        if (phoneNumber == null)
            return false;
        // Regular expression to match a 10-digit phone number
        String regex = "\\d{11}";

        // Remove all non-digits and check against the regex
        String cleanNumber = phoneNumber.replaceAll("[^\\d]", "");
        return cleanNumber.matches(regex);
    }
}
