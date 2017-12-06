package com.mymatatu.appdecsiptionscreen.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by S.V. on 8/1/2017.
 */

public class AppDesciptionModel implements Parcelable {

    public int imageId;
    public  String desciption;
    public  String highLightText;

    protected AppDesciptionModel(Parcel in) {
        imageId = in.readInt();
        desciption = in.readString();
        highLightText = in.readString();
    }

    public static final Creator<AppDesciptionModel> CREATOR = new Creator<AppDesciptionModel>() {
        @Override
        public AppDesciptionModel createFromParcel(Parcel in) {
            return new AppDesciptionModel(in);
        }

        @Override
        public AppDesciptionModel[] newArray(int size) {
            return new AppDesciptionModel[size];
        }
    };

    public AppDesciptionModel() {

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(imageId);
        parcel.writeString(desciption);
        parcel.writeString(highLightText);
    }
}
