package fr.utc.lo23.sharutc.model;

/**
 *
 */
public class ErrorMessage {
    private String mMessage;
    
    public ErrorMessage(String msg){
        mMessage = msg;
    }

    public String getMessage() {
        return mMessage;
    }

    public void setMessage(String mMessage) {
        this.mMessage = mMessage;
    }
}

