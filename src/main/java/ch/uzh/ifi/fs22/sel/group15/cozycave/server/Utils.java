package ch.uzh.ifi.fs22.sel.group15.cozycave.server;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import org.checkerframework.checker.index.qual.Positive;

public class Utils {

    public static final int SALT_LENGTH = 16;

    public static boolean checkValidEmail(String email) {
        String regex = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)])";
        return email.matches(regex);
    }

    public static String generateSalt() {
        return generateSalt(SALT_LENGTH);
    }

    public static String generateSalt(@Positive int length) {
        SecureRandom random = null;

        try {
            random = SecureRandom.getInstance("SHA1PRNG");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return (Math.random() * (SALT_LENGTH + 2) + "").substring(1, SALT_LENGTH + 1);
        }

        byte[] bytes = new byte[length];
        random.nextBytes(bytes);

        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes).substring(0, SALT_LENGTH);
    }

    /**
     * removes all special characters and white spaces, if they are at the front or back of the string. it also replaces
     * all white spaces with a single space and checks if the string has no letters.
     *
     * @param str name
     * @return the stripped version of the name
     */
    // TODO: change to also allow other languages characters
    public static String stripNames(String str) {
        return str.replaceAll("\\s+", " ").trim();
    }

    /**
     * only allows numbers and a + at the front
     *
     * @param str of the phone number
     * @return the stripped phone number
     */
    public static String stripPhoneNumber(String str) {
        return str.replaceAll("[^+\\d]", "");
    }

    // TODO: validate the phone number
    public static boolean isPhoneNumberValid(String str) {
        return true;
    }
}
