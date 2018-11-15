/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package exception;

import java.io.PrintStream;
import java.io.PrintWriter;

/**
 *
 * @author root
 */
public class OrderGameListException extends Exception{
    private static final long serialVersionUID = -2332210270382264631L;
    private Throwable nestedThrowable = null;
    
    public OrderGameListException(){
        super();
    }
    
    public OrderGameListException(String msg){
        super(msg);
    }
    
    public OrderGameListException(Throwable throwable){
        super(throwable);
        this.nestedThrowable = throwable;
    }
    
    public OrderGameListException(String msg, Throwable throwable){
        super(msg,throwable);
        this.nestedThrowable = throwable;
    }

    @Override
    public void printStackTrace(PrintWriter pw) {
        super.printStackTrace(pw);
        if(this.nestedThrowable!=null){
            this.nestedThrowable.printStackTrace(pw);
        }
    }

    @Override
    public void printStackTrace(PrintStream ps) {
        super.printStackTrace(ps);
        if(this.nestedThrowable!=null){
            this.nestedThrowable.printStackTrace(ps);
        }
    }

    @Override
    public void printStackTrace() {
        super.printStackTrace();
        if(this.nestedThrowable!=null){
            this.nestedThrowable.printStackTrace();
        }
    }
}