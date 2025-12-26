package dev.gamelord2011.gmlrdlib;

import java.lang.StackWalker.Option;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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

            The index.
              \
               |
        Map<Intiger, Map<String, Map<String, String>[]>>
                           |     + The keymap for a given package/language pair.
              The language code


    */

                    //Make me an array.
                    //   \/
    static Map<Integer, Map<String, String[]>> keyMap = new LinkedHashMap<>();
    static Map<String, Integer> INDEX;

    public static Integer getIndex(String callerClass) {
        Integer i = INDEX.size();

        if (INDEX.containsKey(callerClass)) {
            return INDEX.get(callerClass);
        }

        INDEX.put(callerClass, i);
        return i;
    }

    public static void addToLanguageSet(String langCode, String[] strings) {
        String callerClass = StackWalker.getInstance(Option.RETAIN_CLASS_REFERENCE).getCallerClass().toString();

        Map<String, String[]> keys = new LinkedHashMap<>();

        keys.put(callerClass, strings);

        keyMap.put(
            getIndex(callerClass),
            keys
        );
    }

    static Map<String, String> langMap = new LinkedHashMap<>();

    public static Map<String, String> constructLanguageSet(String langCode) {
        final String[] values;
        List<String> merged = new ArrayList<>();
        

        for (Map<String, String[]> innerMap : keyMap.values()) {
            for (String[] arr : innerMap.values()) {
                Collections.addAll(merged, arr);
            }
        }

        values = merged.toArray(new String[0]);

        for (String value : values) {
            langMap.put(hashString(UUID.randomUUID().toString()), value);
        }

        return langMap;
    }

    public static String getRuntimeKeyFromMap(Integer index, String langCode) {
        String callerClass = StackWalker.getInstance(Option.RETAIN_CLASS_REFERENCE).getCallerClass().toString();

        Map<String, String[]> innerMap = keyMap.get(getIndex(callerClass));
    }
}
