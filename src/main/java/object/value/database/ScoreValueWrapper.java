/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package object.value.database;

import com.google.gson.Gson;

/**
 *
 * @author root
 */
public class ScoreValueWrapper {
    
    private long score;
    private long timesOfLogin;
    private long amountTotal;
    private long latestLogin;

    public static ScoreValueWrapper parse(String jsonString){
        return new Gson().fromJson(jsonString, ScoreValueWrapper.class);
    }
    
    public ScoreValueWrapper(long score, long timesOfLogin, long amountTotal, long time) {
        this.score = score;
        this.timesOfLogin = timesOfLogin;
        this.amountTotal = amountTotal;
        this.latestLogin = time;
    }
    
    public String toJSONString(){
        return new Gson().toJson(this);
    }

    public long getScore() {
        return score;
    }

    public long getTimesOfLogin() {
        return timesOfLogin;
    }

    public long getAmountTotal() {
        return amountTotal;
    }

    public long getLatestLogin() {
        return latestLogin;
    }

    public void setScore(long score) {
        this.score = score;
    }

    public void setTimesOfLogin(long timesOfLogin) {
        this.timesOfLogin = timesOfLogin;
    }

    public void setAmountTotal(long amountTotal) {
        this.amountTotal = amountTotal;
    }

    public void setLatestLogin(long latestLogin) {
        this.latestLogin = latestLogin;
    }
    
}
