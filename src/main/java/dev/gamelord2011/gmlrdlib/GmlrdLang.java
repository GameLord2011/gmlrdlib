package dev.gamelord2011.gmlrdlib;

import java.lang.StackWalker.Option;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Set;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;
import java.util.List;
import java.util.Map.Entry;
import java.util.Objects;

public class GmlrdLang {
    static Map<Integer, Map<String, Map<String, String>>> keyMap = new LinkedHashMap<>();
    static Map<String, Integer> INDEX = new LinkedHashMap<>();
    static Map<String, String> langMap = new LinkedHashMap<>();

    // Source - https://stackoverflow.com/questions/1383797/java-hashmap-how-to-get-key-from-value
    // Posted by Vitalii Fedorenko, modified by community. See post 'Timeline' for change history
    // Retrieved 2025-12-30, License - CC BY-SA 4.0
    public static <T, E> T getKeyByValue(Map<T, E> map, E value) {
        for (Entry<T, E> entry : map.entrySet()) {
            if (Objects.equals(value, entry.getValue())) {
                return entry.getKey();
            }
        }
        return null;
    }

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
     * @param langMap a language map in the form of String (ISO639-1 language code), String[][], (in the structure of [normal strings (chat text or smth)], [things that need to be turned into identifiers])
     */
    public static void addToLanguageSet(Map<String, String[][]> langMap) {
        if(langMap.values().size() > 2) throw new IndexOutOfBoundsException();
        String callerClass = StackWalker.getInstance(Option.RETAIN_CLASS_REFERENCE).getCallerClass().toString();
        Map<String, Map<String, String>> keys = new LinkedHashMap<>();

        List<String> UUIDS = new ArrayList<>();

        int KeysLen = langMap.values().iterator().next().length;

        for(int i = 0; i < KeysLen; i++) {
            UUIDS.add(hashString(UUID.randomUUID().toString()));
        }


        int index = 0;
        for (String[][] vals : langMap.values()) {
            Map<String, String> kvps = new LinkedHashMap<>();
            String langCode = getKeyByValue(langMap, vals);
            int i = 0;
            for(String val : vals) {
                String key = UUIDS.get(i++);
                kvps.put(key, val);
            }
            keys.put(langCode, kvps);
        }

        keyMap.put(
            getIndex(callerClass),
            keys
        );
        GmlrdLib.LOGGER.info("keyMap: {}", keyMap);
    }

    public static Map<String, String> constructLanguageSet(String langCode) {

        for(Integer index : keyMap.keySet()) {
            Map<String, Map<String, String>> map = keyMap.get(index);
            Map<String, String> keys = map.getOrDefault(langCode, map.get("en_us"));
            for(String key : keys.keySet()) {
                langMap.put(key, keys.get(key));
            }
        }

        return langMap;
    }

    /**
     * Gets the runtime translation key of a string at a given index from the translation map.
     * @param index the index of the string to fetch for.
     * @return the translation key of the language.
     */
    public static String getRuntimeKeyFromMap(Integer index) {
        String callerClass = StackWalker.getInstance(Option.RETAIN_CLASS_REFERENCE).getCallerClass().toString();

        Set<String> valSet = keyMap.get(getIndex(callerClass)).values().iterator().next().keySet();

        int i = 0;
        for(String key : valSet) {
            if(i == index) {
                return key;
            }
            i++;
        }

        return "";
    }
}
