package sharedClasses;

import rmi.BroadcastType;

import java.io.Serializable;

public class Message implements Serializable {
    private static final long serialVersionUID = 1L;

    private BroadcastType msgType;
    private String content;


    public Message(){}

    public Message( String content){
        this.content = content;
    }

    public String getContent(){
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
