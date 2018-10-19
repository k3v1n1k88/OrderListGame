/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Strategy.DatabaseMapping;

import Constant.DBConstantString;
import DatabaseConnectionPool.DatabaseConnectionPoolLevelDB;
import Exception.DatabaseException;
import Object.LogPayment;
import Strategy.DatabaseMappingMissing.DatabaseMappingLevelDB;
import com.google.gson.JsonObject;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.commons.net.ntp.NTPUDPClient;
import org.apache.commons.net.ntp.TimeInfo;
import org.apache.log4j.Logger;
import static org.fusesource.leveldbjni.JniDBFactory.bytes;
import static org.fusesource.leveldbjni.JniDBFactory.factory;
import org.iq80.leveldb.DB;
import org.iq80.leveldb.Options;

/**
 *
 * @author root
 */
public class DatabaseMappingLevelDB implements DatabaseMappingStrategy {
    private static final Logger LOGGER = Logger.getLogger(DatabaseMappingLevelDB.class);

    private static DatabaseConnectionPoolLevelDB pool = null;

    private Options option;
    private String databaseMissingName;
    private DB db;

    public DatabaseMappingLevelDB(String databaseMissingName) {
        this(databaseMissingName, new Options().createIfMissing(true));
    }

    public DatabaseMappingLevelDB(String databaseMissingName, Options option) {
        this.option = option;
        this.databaseMissingName = databaseMissingName;
    }

    @Override
    public void writeMappedInfo(String session, LogPayment logPayment) throws DatabaseException, IOException {
        db = factory.open(new File(this.databaseMissingName), option);
        try {
            Date currentDate = this.getDate();
            DateFormat dateFormat = new SimpleDateFormat(Configuration.ConfigOfSystem.formatDate);

            String dateString = dateFormat.format(this.getDate().getTime());
            long timeStamp = currentDate.getTime();

            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty(DBConstantString.DATE_STRING, dateString);
            jsonObject.addProperty(DBConstantString.USERID_STRING, logPayment.getUserID());
            jsonObject.addProperty(DBConstantString.GAMEID_STRING, logPayment.getGameID());
            jsonObject.addProperty(DBConstantString.AMOUNT_STRING, logPayment.getAmount());

            String value = jsonObject.toString();

            db.put(bytes(String.valueOf(timeStamp)), bytes(value));
        } finally {
            db.close();
        }
        //LOGGER.info("Mapping misssed with" +value);
    }

    private Date getDate() throws UnknownHostException, IOException {
        NTPUDPClient timeClient = new NTPUDPClient();

        InetAddress inetAddress = InetAddress.getByName(Constant.PathConstantString.TIME_SERVER_ADDRESS);
        TimeInfo timeInfo = timeClient.getTime(inetAddress);
        long returnTime = timeInfo.getReturnTime();

        Date time = new Date(returnTime);
        long systemtime = System.currentTimeMillis();

        timeInfo.computeDetails();

        Date realdate = new Date(systemtime + timeInfo.getOffset());

        return realdate;
    }
}
