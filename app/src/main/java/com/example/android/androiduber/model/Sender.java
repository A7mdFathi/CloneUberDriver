package com.example.android.androiduber.model;

public class Sender {

    public DataNotify data;
    public String to;

    public Sender() {
    }

    public Sender(DataNotify data, String to) {
        this.data = data;
        this.to = to;
    }

    public DataNotify getData() {
        return data;
    }

    public void setData(DataNotify data) {
        this.data = data;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }
}
