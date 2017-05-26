package com.sdsmdg.pulkit.callingtext;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Harshit Bansal on 5/26/2017.
 */

public class PhoneContact implements Parcelable {

    String name, phone;
    protected PhoneContact(Parcel in) {
    }

    public PhoneContact(String name, String phone){
        this.name = name;
        this.phone = phone;
    }

    public static final Creator<PhoneContact> CREATOR = new Creator<PhoneContact>() {
        @Override
        public PhoneContact createFromParcel(Parcel in) {
            return new PhoneContact(in);
        }

        @Override
        public PhoneContact[] newArray(int size) {
            return new PhoneContact[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
    }
}
