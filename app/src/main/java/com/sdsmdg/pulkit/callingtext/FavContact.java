package com.sdsmdg.pulkit.callingtext;

public class FavContact {
    String name, number;

    public void setName(String name){
        this.name = name;
    }

    public void setNumber(String number){
        this.number=number;
    }

    public String getName(){
        return name;
    } //returns favourite contact's name

    public String getNumber(){
        return number;
    } //returns favourite contact's number

}
