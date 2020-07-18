package com.home.khalil.smartshop;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class TextResult {

    @SerializedName("text")
    @Expose
    private ArrayList<ArrayList<String>> text;

    @SerializedName("status")
    @Expose
    private String status;




    public ArrayList<ArrayList<String>> getText(){
        return text;
    }

    public String getStatus(){
        return status;
    }
}
