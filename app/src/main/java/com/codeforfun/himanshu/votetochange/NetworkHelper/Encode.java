package com.codeforfun.himanshu.votetochange.NetworkHelper;

import android.text.TextUtils;
import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

/**
 * Created by Himanshu on 13-03-2017.
 */

public class Encode {
    public static void checkData(List<String> data) throws IncorrectEncodingData {
        if(data.size()%2 != 0){
            throw new IncorrectEncodingData();
        }
    }

    public static String generateEncodedString(List<String> data) throws IncorrectEncodingData {

        checkData(data);
        StringBuilder encodedData = new StringBuilder("");
        for(int i=0;i<data.size();i+=2){
            try {

                if(!TextUtils.isEmpty(encodedData.toString()))
                    encodedData.append("&") ;

                encodedData.append(URLEncoder.encode(data.get(i), "UTF-8")).append("=").append(URLEncoder.encode(data.get(i + 1), "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        Log.i("TAGG","ENCODE -- -- "+encodedData.toString());
        return encodedData.toString();
    }
}
