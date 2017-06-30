package com.sdsmdg.pulkit.callingtext;

/**
 * Created by pulkit on 4/2/17.
 */

public class CallerDetails {

    String caller_name;
    String caller_number;
    String caller_msg;
    String call_type;
    String call_time;
    String call_duration;

    CallerDetails(String name,String number , String msg, String type,String time, String duration)
   {
        this.call_time=time;
        this.call_type=type;
        this.caller_msg=msg;
        this.caller_number=number;
        this.caller_name=name;
        this.call_duration=duration;
   }
    public void setCall_time(String call_time) {
        this.call_time = call_time;
    }

    public String getCall_time() {

        return call_time;
    }

    public void setCaller_name(String caller_name) {
        this.caller_name = caller_name;
    }

    public void setCaller_number(String caller_number) {
        this.caller_number = caller_number;
    }

    public void setCaller_msg(String caller_msg) {
        this.caller_msg = caller_msg;
    }

    public void setCall_type(String call_type) {
        this.call_type = call_type;
    }

    public String getCall_type() {

        return call_type;
    }
    public void setCall_duration(String call_duration){
        this.call_duration=call_duration;
    }
    public String getCall_duration(){
        return call_duration;
    }

    public String getCaller_msg() {
        return caller_msg;
    }

    public String getCaller_number() {
        return caller_number;
    }

    public String getCaller_name() {
        return caller_name;
    }

}
