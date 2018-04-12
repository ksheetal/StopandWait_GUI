package com.example.sheetal.stopandwait;

/**
 * Created by sheetal on 4/11/2018.
 */

public class Data {
    public String message;
    public String ack;

    public Data(String message, String ack) {
        this.message = message;
        this.ack = ack;
    }

    public Data() {
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getAck() {
        return ack;
    }

    public void setAck(String ack) {
        this.ack = ack;
    }
}
