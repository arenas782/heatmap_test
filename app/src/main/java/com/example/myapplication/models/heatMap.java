package com.example.myapplication.models;/*
 Created by arenas on 23/02/21.
*/


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class heatMap {

    @SerializedName("x")
    @Expose
    private String x;

    @SerializedName("y")
    @Expose
    private String y;


    public String getX() {
        return x;
    }

    public void setX(String x) {
        this.x = x;
    }

    public String getY() {
        return y;
    }

    public void setY(String y) {
        this.y = y;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    @SerializedName("value")
    @Expose
    private int value;




}