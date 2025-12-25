package dev.gamelord2011.gmlrdlib;

import java.lang.StackWalker.Option;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import java.util.Map;
import java.util.Optional;

public class GmlrdLang {

    private static String hashString(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA3-512");
            byte[] hashBytes = digest.digest(input.getBytes());

            StringBuilder hexString = new StringBuilder();
            for(byte b : hashBytes) {
                String hex = Integer.toHexString(0xff & b);
                if(hex.length() == 1) hexString.append(0);
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            GmlrdLib.LOGGER.error("[GMLRDLANG]: Dangit, an excepting was thrown..." + e.toString());
            return input;
        }
    }

    /*
        keyMap structure (cuz I cant keep track of it).

            The package it was called from.
              \                               It's an array so several packages can interface with it.
               |                                        |
        Map<String, Map<String, Map<String, String>[]>>[]
                           |     + The keymap for a given package/language pair.
              The language code


    */

    private Map<String, Map<String, Map<String, String>[]>>[] keyMap;

    public static Map<String, String> constructLanguageSet(String langCode, String[] strings) { // I'm not implementing auto-language handling here, as it breaks some stuff.
        Class<?> callerClass = StackWalker.getInstance(Option.RETAIN_CLASS_REFERENCE).getCallerClass();

        
    }

    public static String getKeyFromMap(int index) {
        return "lalala";
    }
}
