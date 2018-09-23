/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Database;


import Constant.DBInfo;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import org.iq80.leveldb.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

/**
 *
 * @author root
 */
public class DatabaseRecommentHashTable implements DatabaseRecommentStrategy {

    private TreeMap<String, Long> pointOfGame = new TreeMap();
    private String databaseName;
    private Options option;

    public DatabaseRecommentHashTable() {
        this(DBInfo.databaseRecommentName);
    }

    public DatabaseRecommentHashTable(String databaseName) {
        this(databaseName, new Options().createIfMissing(true));
    }

    public DatabaseRecommentHashTable(String databaseName, Options option) {
        this.databaseName = databaseName;
        this.option = option;
    }

    @Override
    public void update(ArrayList<String> listGame) {
        for (int i = 0; i < listGame.size(); i++) {
            long point = 0;
            if (pointOfGame.containsKey(listGame.get(i))) {
                point = pointOfGame.get(listGame.get(i));
            }
            pointOfGame.put(listGame.get(i), point + 1);
        }

    }

    public static void main(String[] args) throws IOException {
        DatabaseRecommentHashTable databaseRecommentHashTable = new DatabaseRecommentHashTable();
        ArrayList<String> list = new ArrayList();
        list.add("1");
        list.add("2");
        list.add("3");
        list.add("4");
        list.add("5");
        list.add("1");
        list.add("2");
        list.add("3");
        list.add("4");
        list.add("1");
        list.add("2");
        databaseRecommentHashTable.update(list);
        databaseRecommentHashTable.getTop(2);
    }

    @Override
    public JsonArray getTop(int number) {
        int i = 0;
        JsonArray jsonArray = new JsonArray();
        for (Map.Entry<String, Long> entry : pointOfGame.entrySet()) {
            JsonPrimitive element = new JsonPrimitive(entry.getKey());
            jsonArray.add(element);
            if(++i>number)
                break;
	}
        return jsonArray;
    }
}
