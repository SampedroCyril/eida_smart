package org.eida.SMART;

/**
 * This function is used to generate a 10-digit number for the accounts.
 * If needed, '0' are added in the front.
 */
public class GenNumAccount {
    public static String genNumAccount(int i) {
        String out = Integer.toString(i);

        while (out.length() < 10) {
            out = "0" + out;
        }

        return out;
    }

}
