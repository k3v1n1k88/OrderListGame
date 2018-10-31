/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Task;

import Log.Log;

/**
 *
 * @author root
 * @param <L> instance of Log
 */
public abstract class WorkerAbstract<L extends Log> implements Runnable{
    
    protected L log;
    
    public WorkerAbstract(L log){
        this.log = log;
    }
    
    @Override
    public void run() {
        processLog();
    }
    
    protected abstract boolean processLog();
}
