package com.mymatatu.App_Importants;

import android.content.Context;
import android.text.TextUtils;

import com.mymatatu.InternetConnection.ConnectivityReceiver;

/**
 * Created by anonymous on 24-07-2017.
 */

public class Validation {
    public static boolean checkEmptyfield(String fieldValue) {
        return TextUtils.isEmpty(fieldValue);
    }

    public static boolean checkPassWordLenth(String fieldValue) {
        if (fieldValue.length() < 6) {
            return true;
        } else {
            return false;
        }
    }


    public static boolean checkPassWordMatch(String Password,String comfirmPassword) {
        boolean pstatus = false;
        if (comfirmPassword != null && comfirmPassword != null)
        {
            if (Password.equals(comfirmPassword))
            {
                pstatus = true;
            }
        }
        return pstatus;
    }
    public static boolean checkconnection(Context c){
        if (!ConnectivityReceiver.isConnected(c)) {
            return false;
        }
        return true;
    }

    public static boolean checkphonenolength (String phone){
        if(phone.length() == 9){
            return true;
        }
        return false;
    }
    public static boolean checkroutesame (String source , String Destination){
        if(source.compareToIgnoreCase(Destination) == 0){
            return true;
        }
        return false;
    }
    public static boolean checkcost(int balance,int cost){
        if(balance > cost){
            return true;
        }
        return false;
    }
}
