/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Strategy.DatabaseRecomment;

import com.google.gson.JsonArray;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author root
 */
public interface DatabaseRecommentStrategy {
    public int addGameIDEvenExist(String gameID) throws Exception;
    public int update(List<String> listGame) throws Exception;
    public JsonArray getTop(int number) throws Exception;
}
