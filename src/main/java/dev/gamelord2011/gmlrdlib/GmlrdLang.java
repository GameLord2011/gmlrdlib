package dev.gamelord2011.gmlrdlib;

import java.lang.StackWalker.Option;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class GmlrdLang {
    static Map<Integer, Map<String, String[]>> keyMap = new LinkedHashMap<>();
    static Map<String, Integer> INDEX = new LinkedHashMap<>();
    static Map<String, String> langMap = new LinkedHashMap<>();
    static Map<Integer, Map<String, String>> keyGetter = new LinkedHashMap<>();

    /**
     * This returns an SHA3-512 hash of a given string.
     * @param input
     * @return
     */
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

    /**
     * Fetches the index of a class from a map.
     * @param callerClass
     * @return the index of the class in the array.
     */
    public static Integer getIndex(String callerClass) {
        Integer i = INDEX.size();

        if (INDEX.containsKey(callerClass)) {
            return INDEX.get(callerClass);
        }

        INDEX.put(callerClass, i);
        return i;
    }

    /**
     * Adds a translation map to the language map. <strong>IMPORTANT:</strong> it counts each class that it's called from as a seperate class,
     * so make sure that all language stuff is done in the <code>onInitialize()</code> of the main class.
     * @param langMap a language map in the form of String (ISO639-1 language code), String[] (the strings that are translated)
     */
    public static void addToLanguageSet(Map<String, String[]> langMap) {
        String callerClass = StackWalker.getInstance(Option.RETAIN_CLASS_REFERENCE).getCallerClass().toString();

        GmlrdLib.LOGGER.info("addToLanguageSet called by {}", callerClass);

        keyMap.put(
            getIndex(callerClass),
            langMap
        );
    }

    public static Map<String, String> constructLanguageSet(String langCode) {
        // final String[] values;
        // List<String> merged = new ArrayList<>();

        for(Integer index : keyMap.keySet()) {
            Map<String, String[]> map = keyMap.get(index);

            String[] keys = map.getOrDefault(langCode, map.get("en_us"));
            Map<String, String> keysMap = new LinkedHashMap<>();
            for(String value : keys) {
                String key = hashString(UUID.randomUUID().toString());
                keysMap.put(key, value);
                langMap.put(key, value);
            }
            keyGetter.put(index, keysMap);
        }

        GmlrdLib.LOGGER.info("conStructLanguageSet called");

        // for (Map<String, String[]> innerMap : keyMap.values()) {
        //     for (String[] arr : innerMap.values()) {
        //         Collections.addAll(merged, arr);
        //     }
        // }

        // values = merged.toArray(new String[0]);

        // for (String value : values) {
        //     langMap.put(hashString(UUID.randomUUID().toString()), value);
        // }

        return langMap;
    }

    /**
     * Gets the runtime translation key of a string at a given index from the translation map.
     * @param index the index of the string to fetch for.
     * @return the translation key of the language.
     */
    public static String getRuntimeKeyFromMap(Integer index) {
        String callerClass = StackWalker.getInstance(Option.RETAIN_CLASS_REFERENCE).getCallerClass().toString();

        Map<String, String> innerMap = keyGetter.get(getIndex(callerClass));

        GmlrdLib.LOGGER.info("innerMap: {}", innerMap);

        Optional<String> nthKey = innerMap.keySet().stream().skip(index).findFirst();

        if(!nthKey.isPresent()) {
            throw new IndexOutOfBoundsException();
        }

        return nthKey.toString();
    }
}
