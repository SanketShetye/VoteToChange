package com.codeforfun.himanshu.votetochange.NetworkHelper;

/**
 * Created by Himanshu on 13-03-2017.
 */

public class IncorrectEncodingData extends Exception {
    @Override
    public String getMessage() {
        return "Number of Arguments are ODD.  It should be EVEN";
    }
}
