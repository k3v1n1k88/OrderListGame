package Exception;

import java.io.PrintStream;
import java.io.PrintWriter;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author root
 */
public class ParseLogException extends Exception{

    private static final long serialVersionUID = -2332210270382264631L;
    
    
    private Throwable nestedThrowable = null;
    
    public ParseLogException(){
        super();
    }
    
    public ParseLogException(String msg){
        super(msg);
    }
    
    public ParseLogException(Throwable throwable){
        super(throwable);
        this.nestedThrowable = throwable;
    }
    
    public ParseLogException(String msg, Throwable throwable){
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
