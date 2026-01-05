package io.github.gamelord2011.gmlrdlib;

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

import net.minecraft.resources.Identifier;

/**
 * GmlrdLang class.
 */
public class GmlrdLang {
    private static Map<Integer, Map<String, Map<String, String>>> keyMap = new LinkedHashMap<>();
    private static Map<Integer, Map<String, Map<Identifier, String>>> IdentMap = new LinkedHashMap<>();
    private static Map<Class<?>, Integer> INDEX = new LinkedHashMap<>();
    private static Map<String, String> langMap = new LinkedHashMap<>();

    /**
     * Gets the key of a given item by the value for a Map.
     * Source - https://stackoverflow.com/questions/1383797/java-hashmap-how-to-get-key-from-value
     * Posted by Vitalii Fedorenko, modified by community. See post 'Timeline' for change history, 
     * also modified by GameLord2011.
     * Retrieved 2025-12-30, License - CC BY-SA 4.0
     * 
     * @param map the map to root through
     * @param value the value to find the key for
     * @return the key for the value "value"
     * @since 1.0.0
     */
    private static <T, E> T getKeyByValue(Map<T, E> map, E value) {
        for (Entry<T, E> entry : map.entrySet()) {
            if (Objects.equals(value, entry.getValue())) {
                return entry.getKey();
            }
        }
        return null;
    }

    /**
     * This returns an SHA3-512 hash of a given string.
     * @param input the string to hash
     * @return the hashed string
     * @since 1.0.0
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
     * @param callerClass the Class that the upper method was called from.
     * @return the index of the class in the array.
     * @since 1.0.0
     */
    public static Integer getIndex(Class<?> callerClass) {
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
     * @param langMap a language map in the form of String (ISO639-1 language code), String[][], (in the structure of [normal strings (chat text
     * or smth)], [keybinding categories, but can probably be used for identifiers in general])
     * @param MOD_ID Pass the MOD_ID string from the mods main class into here.
     * @since 1.0.0
     */
    public static void addToLanguageSet(Map<String, String[][]> langMap, String MOD_ID) {
        Class<?> callerClass = StackWalker.getInstance(Option.RETAIN_CLASS_REFERENCE).getCallerClass();
        Map<String, Map<String, String>> keys = new LinkedHashMap<>();

        List<String> UUIDS = new ArrayList<>();
        List<String> IdentUUIDS = new ArrayList<>();

        int KeysLen = langMap.values().iterator().next()[0].length;

        for(int i = 0; i < KeysLen; i++) {
            UUIDS.add(hashString(UUID.randomUUID().toString()));
        }

        if(langMap.values().iterator().next().length > 1) {
            int IdentLen = langMap.values().iterator().next()[1].length;
            for(int i = 0; i < IdentLen; i++) {
                IdentUUIDS.add(hashString(UUID.randomUUID().toString()));
            }
        }

        Map<String, Map<Identifier, String>> identMap = new LinkedHashMap<>();

        for (String[][] vals : langMap.values()) {
            Map<String, String> kvps = new LinkedHashMap<>();
            Map<Identifier, String> identKvps = new LinkedHashMap<>();
            String langCode = getKeyByValue(langMap, vals);
            int i = 0, n = 0;
            for(String[] valz : vals) {
                if(vals[0].equals(valz)) {
                    for(String val : valz) {
                        String key = UUIDS.get(i++);
                        kvps.put(key, val);
                    }
                } else if (vals[1].equals(valz)) {
                    for(String val : valz) {
                        Identifier identifier = Identifier.fromNamespaceAndPath(MOD_ID, IdentUUIDS.get(n++));
                        identKvps.put(identifier, val);
                    }
                    identMap.put(langCode, identKvps);
                }
            }
            keys.put(langCode, kvps);
        }

        keyMap.put(
            getIndex(callerClass),
            keys
        );
        IdentMap.put(
            getIndex(callerClass),
            identMap
        );
    }

    /**
     * Constructs the language set for a given ISO639-1 language code <strong>DO NOT CALL THIS CLASS IN YOUR MOD, THIS IS ONLY HERE FOR COMPLETENESS, I also put in some handling to where you can't anyway, so HA</strong>.
     * @param langCode the ISO639-1 language code for which the language is to be generated.
     * @return A complete set of key-value pairs fot the given language.
     */
    public static Map<String, String> constructLanguageSet(String langCode) {
        if(
            StackWalker.getInstance(Option.RETAIN_CLASS_REFERENCE)
                .getCallerClass()
                .getName()
            != "net.minecraft.client.resources.language.ClientLanguage"
        ) throw new IllegalAccessError();

        for(Integer index : keyMap.keySet()) {
            Map<String, Map<String, String>> map = keyMap.get(index);
            Map<String, String> keys = map.getOrDefault(langCode, map.get("en_us"));
            for(String key : keys.keySet()) {
                langMap.put(key, keys.get(key));
            }
        }

        for(Integer index : IdentMap.keySet()) {
            Map<Identifier, String> idents = IdentMap.get(index).getOrDefault(langCode, IdentMap.get(index).get("en_us"));
            for(Identifier identifier : idents.keySet()) {
                String keyString = "key.category." + identifier.getNamespace() + "." + identifier.getPath(); // Umm... it throws people off? (Can I use this key for say, another thing that requires an identifier?)
                langMap.put(keyString, idents.get(identifier));
            }
        }

        return langMap;
    }

    /**
     * Gets the runtime translation key of a string at a given index from the translation map.
     * @param index the index of the string to fetch for.
     * @return the translation key of the language.
     * @since 1.0.0
     */
    public static String getRuntimeKeyFromMap(Integer index) {
        Class<?> callerClass = StackWalker.getInstance(Option.RETAIN_CLASS_REFERENCE).getCallerClass();

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

    /**
     * Gets the identifier key for a given index.
     * @param index The index to fetch for.
     * @return the Identifier
     * @since 1.0.0
     */
    public static Identifier getIdentifier(Integer index) {
        Class<?> callerClass = StackWalker.getInstance(Option.RETAIN_CLASS_REFERENCE).getCallerClass();

        Set<Identifier> Identifiers = IdentMap.get(getIndex(callerClass)).values().iterator().next().keySet();

        int i = 0;
        for(Identifier key : Identifiers) {
            if(i == index) {
                return key;
            }
            i++;
        }

        return null;
    }
}
