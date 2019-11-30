package com.neueda.shorturl.util;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.HashMap;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
public class IDUtilsTest {
    
    @Before
    public void init() {
        charToIndexTable = IDUtils.initializeCharToIndexTable();
        indexToCharTable = IDUtils.initializeIndexToCharTable();
        ReflectionTestUtils.setField(IDUtils.class, "charToIndexTable", charToIndexTable);
        ReflectionTestUtils.setField(IDUtils.class, "indexToCharTable", indexToCharTable);
    }
    
    private HashMap<Character, Integer> charToIndexTable;
    private List<Character> indexToCharTable;
    
    @Test
    public void createUniqueID() {
        Long input = 1639000000000L;
        String expected = "C1cK8g8";
        
        String rta = IDUtils.generateUniqueID(input);
    
        assertThat(rta).isEqualTo(expected);
    }
    
    @Test
    public void getDictionaryKeyFromUniqueIDTest() {
        String input = "C1cK8g8";
        Long expected = 1639000000000L;
        
        Long rta = IDUtils.getKeyFromUniqueID(input);
        
        assertThat(rta).isEqualTo(expected);
    }
}