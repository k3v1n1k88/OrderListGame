/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Database;

import com.google.gson.JsonArray;
import java.util.ArrayList;

/**
 *
 * @author root
 */
public interface DatabaseRecommentStrategy {
    public void update(ArrayList<String> listGame);
    public JsonArray getTop(int number);
}
