package com.bowen.assignment.entity;

/**
 * Created by patrick on 2015-03-10.
 */
public class ImageEntity {

    private long id;

    private String name;

    private double x;

    private double y;

    private String uri;

    private int sendState;

    public String getName() {
        return name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public int getSendState() {
        return sendState;
    }

    public void setSendState(int sendState) {
        this.sendState = sendState;
    }
}
