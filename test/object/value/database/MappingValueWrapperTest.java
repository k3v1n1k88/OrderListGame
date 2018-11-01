/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package object.value.database;

import java.util.HashMap;
import java.util.Map;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author root
 */
public class MappingValueWrapperTest {

    public MappingValueWrapperTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of parse method, of class MappingValueWrapper.
     */
    @Test
    public void testParse() {
        String jsonString = "{\n" +
                            "  \"userID\": \"1213224\",\n" +
                            "  \"listGameID\": {\n" +
                            "    \"121212ssasas\": {\n" +
                            "      \"deposit\": {\n" +
                            "        \"101212\": 20,\n" +
                            "        \"2232440\": 30\n" +
                            "      }\n" +
                            "    },\n" +
                            "    \"34325aasa\": {\n" +
                            "      \"deposit\": {\n" +
                            "        \"1133540\": 20,\n" +
                            "        \"254540\": 30\n" +
                            "      }\n" +
                            "    }\n" +
                            "  }\n" +
                            "}";
        MappingValueWrapper obj = MappingValueWrapper.parse(jsonString);
        Map<String,MappingValueWrapper.Info> list = obj.getListGameID();
        MappingValueWrapper.Info infoGame1 = list.get("121212ssasas");
        assertEquals(obj.getUserID(), "1213224");
        assertTrue(infoGame1.getDeposit().get(101212L).equals(20L));
        assertTrue(infoGame1.getDeposit().get(2232440L).equals(30L));
    }

    /**
     * Test of toJSONString method, of class MappingValueWrapper.
     */
    @Test
    public void testToJSONString() {
        HashMap<Long,Long> hashMap1 = new HashMap<>();
        hashMap1.put(10L, 20L);
        hashMap1.put(20L, 30L);
        MappingValueWrapper.Info info1 = new MappingValueWrapper.Info(hashMap1);
        
        HashMap<Long,Long> hashMap2 = new HashMap<>();
        hashMap2.put(10L, 20L);
        hashMap2.put(20L, 30L);
        MappingValueWrapper.Info info2 = new MappingValueWrapper.Info(hashMap2);
        
        HashMap<String,MappingValueWrapper.Info> listGame = new HashMap<>();
        listGame.put("game1", info1);
        listGame.put("game2", info2);
        
        
        MappingValueWrapper mappingValue = new MappingValueWrapper("1213224",listGame);
        System.out.println(mappingValue.toJSONString());
    }

}
