package sharedClasses;


import rmi.BroadcastType;

import java.io.Serial;
import java.io.Serializable;

public class Message implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private BroadcastType msgType;
    private String content;
    private String timestamp;
    private String playerName;


    public Message(){}

    public Message( String content, String timestamp, String playerName, String type){
        this.content = content;
        this.timestamp = timestamp;
        this.playerName = playerName;
        this.msgType = BroadcastType.valueOf(type);
    }

    public Message(String timestamp, String playerName, BroadcastType type, String content){
        this.content = content;
        this.timestamp = timestamp;
        this.playerName = playerName;
        this.msgType = type;
    }

    public BroadcastType getMsgType(){return msgType;}
    public void setMsgType(BroadcastType msgType){this.msgType = msgType;}
    public String getTimestamp(){return timestamp;}

    public void setTimestamp(String timestamp){this.timestamp = timestamp;}
    public String getPlayerName(){return playerName;}

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public String getContent(){
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String toString(){
        return this.timestamp+ " " + this.playerName + "(" + this.msgType.toString() + "): " + this.content;
    }
}