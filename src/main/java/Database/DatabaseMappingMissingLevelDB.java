/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Database;

import Constant.DBInfo;
import Exception.DatabaseException;
import Exception.ParseLogException;
import Object.LogPayment;
import VNG.generate.GenerateRequestFromPayment;
import com.google.gson.JsonObject;
import com.ibm.json.java.JSON;
import org.iq80.leveldb.*;
import org.apache.log4j.Logger;
import static org.fusesource.leveldbjni.JniDBFactory.*;
import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;  
import org.json.simple.parser.ParseException;
/**
 *
 * @author root
 */
public class DatabaseMappingMissingLevelDB implements DatabaseMappingMissingStrategy{
    private static final Logger LOGGER = Logger.getLogger(DatabaseMappingMissingLevelDB.class);
    Calendar calendar = Calendar.getInstance();
    
    private Options option;
    private String databaseMissingName;
    private DB db;
    
    public DatabaseMappingMissingLevelDB(){
        this(DBInfo.databaseMissMappingName);
    }
    public DatabaseMappingMissingLevelDB(String databaseMissingName){
        this(databaseMissingName,new Options().createIfMissing(true));
    }
    public DatabaseMappingMissingLevelDB(String databaseMissingName, Options option){
        this.option = option;
        this.databaseMissingName = databaseMissingName;
    }
    
    @Override
    public void writeLogToDatabase(LogPayment logPayment) throws DatabaseException,IOException{
        db = factory.open(new File(this.databaseMissingName), option);
        try {
            DateFormat timeStamp = new SimpleDateFormat(Configuration.ConfigOfSystem.formatDate);
            String dateCurrent = timeStamp.format(calendar.getTime());

            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty(DBInfo.timeString, dateCurrent);
            jsonObject.addProperty(DBInfo.userIDString, logPayment.getUserID());
            jsonObject.addProperty(DBInfo.gameIDString, logPayment.getGameID());
            jsonObject.addProperty(DBInfo.amountString, logPayment.getAmount());

            String value = jsonObject.toString();

            db.put(bytes(dateCurrent), bytes(value));
        } finally {
            db.close();
        }
        //LOGGER.info("Mapping misssed with" +value);
    }
    public static void main(String[] args) throws ParseException, ParseLogException, DatabaseException {

    }
}
