package com.rndtechnosoft.foodcart.Item;

import java.util.ArrayList;

public class ScratchModel {

    public static final int TEXT_TYPE=0;
    public static final int IMAGE_TYPE=1;

    public int type;
    public int data;
    public String text;
    public ArrayList<ScratchList> scratchLists;
    ScratchList scratch;

    public ScratchModel() {
    }

    public ScratchModel(int type, String text, int data)
    {
        this.type=type;
        this.data=data;
        this.text=text;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getData() {
        return data;
    }

    public void setData(int data) {
        this.data = data;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public ArrayList<ScratchList> getScratchLists() {
        return scratchLists;
    }

    public void setScratchLists(ArrayList<ScratchList> scratchLists) {
        this.scratchLists = scratchLists;
    }

    public ScratchList getScratch() {
        return scratch;
    }

    public void setScratch(ScratchList scratch) {
        this.scratch = scratch;
    }
}
