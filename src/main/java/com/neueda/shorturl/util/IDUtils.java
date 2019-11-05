package com.neueda.shorturl.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class IDUtils {
    public static final IDUtils INSTANCE = new IDUtils();
    
    private IDUtils() {
        initializeCharToIndexTable();
        initializeIndexToCharTable();
    }
    
    private static HashMap<Character, Integer> charToIndexTable;
    private static List<Character> indexToCharTable;
    
    /**
     * Represents the chart displayed of converting shortened URL back to a Dictionary key.
     */
    private void initializeCharToIndexTable() {
        charToIndexTable = new HashMap<>();
        // 0->a, 1->b, ..., 25->z, ..., 52->0, 61->9
        for (int i = 0; i < 26; ++i) {
            char c = 'a';
            c += i;
            charToIndexTable.put(c, i);
        }
        for (int i = 26; i < 52; ++i) {
            char c = 'A';
            c += (i - 26);
            charToIndexTable.put(c, i);
        }
        for (int i = 52; i < 62; ++i) {
            char c = '0';
            c += (i - 52);
            charToIndexTable.put(c, i);
        }
    }
    
    /**
     * Represents the conversion chart in the base10 to base62
     */
    private void initializeIndexToCharTable() {
        // 0->a, 1->b, ..., 25->z, ..., 52->0, 61->9
        indexToCharTable = new ArrayList<>();
        for (int i = 0; i < 26; ++i) {
            char c = 'a';
            c += i;
            indexToCharTable.add(c);
        }
        for (int i = 26; i < 52; ++i) {
            char c = 'A';
            c += (i - 26);
            indexToCharTable.add(c);
        }
        for (int i = 52; i < 62; ++i) {
            char c = '0';
            c += (i - 52);
            indexToCharTable.add(c);
        }
    }
    
    /**
     * Takes in an id (base10) and converts it into base62
     * @param id The url id which will be converted to base62.
     * @return The id in base62 format.
     */
    public static String createUniqueID(Long id) {
        List<Integer> base62ID = convertBase10ToBase62ID(id);
        StringBuilder uniqueURLID = new StringBuilder();
        for (int digit : base62ID) {
            uniqueURLID.append(indexToCharTable.get(digit));
        }
        return uniqueURLID.toString();
    }
    
    /**
     * Convert base10 to base62 version.
     * @param id The url id which will be converted to base62.
     * @return The id in base62 format.
     */
    private static List<Integer> convertBase10ToBase62ID(Long id) {
        List<Integer> digits = new LinkedList<>();
        while (id > 0) {
            int remainder = (int) (id % 62);
            ((LinkedList<Integer>) digits).addFirst(remainder);
            id /= 62;
        }
        return digits;
    }
    
    /**
     * Takes the unique URL ID and converts it back into its base62 number counterparts.
     * @param uniqueID the input unique id to be processed.
     * @return the base62 id to base10 version.
     */
    public static Long getDictionaryKeyFromUniqueID(String uniqueID) {
        List<Character> base62IDs = new ArrayList<>();
        for (int i = 0; i < uniqueID.length(); ++i) {
            base62IDs.add(uniqueID.charAt(i));
        }
        Long dictionaryKey = convertBase62ToBase10ID(base62IDs);
        return dictionaryKey;
    }
    
    /**
     * Convert the base62 url to base10 version.
     * @param ids
     * @return
     */
    private static Long convertBase62ToBase10ID(List<Character> ids) {
        long id = 0L;
        for (int i = 0, exp = ids.size() - 1; i < ids.size(); ++i, --exp) {
            int base10 = charToIndexTable.get(ids.get(i));
            id += (base10 * Math.pow(62.0, exp));
        }
        return id;
    }
}
