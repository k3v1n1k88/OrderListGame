/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package object.value.database;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
                            "  \"userID\": \"34938944\",\n" +
                            "  \"listGameID\": {\n" +
                            "    \"176271627162\": [\n" +
                            "      {\n" +
                            "        \"timestamp\": 1000000,\n" +
                            "        \"amount\": 13984948593\n" +
                            "      },\n" +
                            "      {\n" +
                            "        \"timestamp\": 1289211212,\n" +
                            "        \"amount\": 13984948593\n" +
                            "      }\n" +
                            "    ],\n" +
                            "    \"17627161262\": [\n" +
                            "      {\n" +
                            "        \"timestamp\": 1000000,\n" +
                            "        \"amount\": 13984948593\n" +
                            "      },\n" +
                            "      {\n" +
                            "        \"timestamp\": 1289211212,\n" +
                            "        \"amount\": 13984948593\n" +
                            "      }\n" +
                            "    ]\n" +
                            "  }\n" +
                            "}";
        MappingValueWrapper obj = MappingValueWrapper.parse(jsonString);
        Map<String,List<MappingValueWrapper.Info>> list = obj.getListGameID();
        List<MappingValueWrapper.Info> historyGame1 = list.get("176271627162");
        assertEquals(obj.getUserID(), "34938944");
        assertTrue(historyGame1.get(0).getAmount() == 13984948593L);
        assertTrue(historyGame1.get(1).getTimestamp()== 1289211212);
    }

    /**
     * Test of toJSONString method, of class MappingValueWrapper.
     */
    @Test
    public void testToJSONString() {
        String expected = "{\"userID\":\"123\",\"listGameID\":{\"176271627162\":[{\"timestamp\":1000000,\"amount\":13984948593},{\"timestamp\":1289211212,\"amount\":13984948593}]}}";
        MappingValueWrapper.Info info1 = new MappingValueWrapper.Info(1000000L,13984948593L);
        MappingValueWrapper.Info info2 = new MappingValueWrapper.Info(1289211212L,13984948593L);
        List<MappingValueWrapper.Info> listInfo = new ArrayList<>();
        listInfo.add(info1);
        listInfo.add(info2);
        Map<String,List<MappingValueWrapper.Info>> m = new HashMap<>();
        m.put("176271627162", listInfo);
        MappingValueWrapper mappedPaymentValue = new MappingValueWrapper("123",m);
        assertEquals(expected,mappedPaymentValue.toJSONString());
    }

}
