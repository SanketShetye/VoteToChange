package com.codeforfun.himanshu.votetochange.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

//import com.codeforfun.himanshu.votetochange.DataObjetcs.Election_data;

import com.codeforfun.himanshu.votetochange.DataObjetcs.ElectionData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DATTA SHETYE on 14/03/2017.
 */

public class DatabaseHelper extends SQLiteOpenHelper {


    private static final String TAG = "Database TAG";
    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_NAME = "Election.db";

    //Election
    private static final String ELECTION_TABLE = "election";
    private static final String ELECTION_ID = "electionid";
    private static final String ELECTION_NAME = "elctionname";
    //private static final String START_DATE = "startdate";
    //private static final String END_DATE = "enddate";
    private static final String ELECTION_KEY = "electionkey";
    private static final String CANDIDATE_KEY = "candidatekey";
    private static final String ELECTION_DESCRIPTION = "electiondescription";
    //private static final String CANDIDATE_REGISTRATION_STATUS = "registrationstatus";
    private static final String WINNER = "winner";
    private static final String OWNER = "owner";
    private static final String ELECTION_STATUS = "electionstatus";
    private static final String VOTE_COUNT = "votecount";

    //Initial vlaues
    private static final String INITIAL_VALUE_FOR_STRING = "NULL";
    private static final int INITIAL_VALUE_FOR_INTEGER = 0; // 0 - Ongoing  1 - End


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createElectionTable(db);
        Log.i(TAG, "Table is created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i(TAG, "ONUPGRADE in DB");
        db.execSQL("DROP TABLE IF EXISTS " + ELECTION_TABLE);
        Log.i(TAG, "Tables Dropped");
        onCreate(db);
    }

    //Creating Election Table
    public void createElectionTable(SQLiteDatabase db) {
        String electionTableQuery = "CREATE TABLE " + ELECTION_TABLE + "("
                + ELECTION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
                + ELECTION_NAME + " TEXT ,"
                + ELECTION_KEY + " TEXT ,"
                + CANDIDATE_KEY + " TEXT ,"
                + ELECTION_DESCRIPTION + " TEXT ,"
                + WINNER + " TEXT,"
                + OWNER + " INTEGER,"
                + VOTE_COUNT + " INTEGER,"
                + ELECTION_STATUS + " INTEGER,"
                + "UNIQUE("+ELECTION_KEY+")) ;";
        db.execSQL(electionTableQuery);
        Log.i(TAG, "Tables Created");
    }

    //Adding entry in election table
    public void addElectionEntry(String elecctionName, String electionKey, String candidateKey,
                                 String electionDescription,String winner, int voteCount,
                                 int electionStatus, int owner) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues newElectionValues = new ContentValues();
        newElectionValues.put(ELECTION_NAME, elecctionName);
        newElectionValues.put(ELECTION_KEY, electionKey);
        newElectionValues.put(CANDIDATE_KEY, candidateKey);
        newElectionValues.put(ELECTION_DESCRIPTION, electionDescription);
        newElectionValues.put(WINNER, winner);
        newElectionValues.put(OWNER, owner);
        newElectionValues.put(VOTE_COUNT, voteCount);
        newElectionValues.put(ELECTION_STATUS, electionStatus);
        db.insert(ELECTION_TABLE, null, newElectionValues);
        Log.i(TAG,"ADDING ENTRY = "+elecctionName+" "+electionKey+" "+candidateKey+" "+winner
                +" "+owner+" "+voteCount+" "+electionStatus );
    }

    /*
    //Update Candidate Registration status
    public void updateCandidateRegistrationStatus(int electionId, int candidateRegValue) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(CANDIDATE_REGISTRATION_STATUS, candidateRegValue);
        db.update(ELECTION_TABLE, cv, ELECTION_ID + "=" + electionId, null);
        String strSQL = "UPDATE "+ELECTION_TABLE+
                "SET "+CANDIDATE_REGISTRATION_STATUS+"="+candidateRegValue+
                " WHERE "+ELECTION_ID+"="+ electionId;
        myDataBase.execSQL(strSQL);
    }*/

    //Update Winner
    public void updateWinner(int electionId, String winner) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(WINNER, winner);
        db.update(ELECTION_TABLE, cv, ELECTION_ID + "=" + electionId, null);
        String strSQL = "UPDATE "+ELECTION_TABLE+
                " SET "+WINNER+"="+winner+
                " WHERE "+ELECTION_ID+"="+ electionId;
        db.execSQL(strSQL);
        Log.i(TAG, "UPDATE WINNER = "+electionId+" "+ winner);
    }
    //Update election status
    public void updateElectionStatus(int electionId, int electionStatus) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(ELECTION_STATUS, electionStatus);
        db.update(ELECTION_TABLE, cv, ELECTION_ID + "=" + electionId, null);
        String strSQL = "UPDATE "+ELECTION_TABLE+
                " SET "+ELECTION_STATUS+"="+electionStatus+
                " WHERE "+ELECTION_ID+"="+ electionId;
        db.execSQL(strSQL);
        Log.i(TAG, "UPDATE Election Status = "+electionId+" "+ electionStatus);
    }

    //Update vote count
    public void updateVoteCount(int electionId, int voteCount) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(VOTE_COUNT, voteCount);
        db.update(ELECTION_TABLE, cv, ELECTION_ID + "=" + electionId, null);
        String strSQL = "UPDATE "+ELECTION_TABLE+
                " SET "+VOTE_COUNT+"="+voteCount+
                " WHERE "+ELECTION_ID+"="+ electionId;
        db.execSQL(strSQL);
        Log.i(TAG, "UPDATE Vote Count = "+electionId+" "+ voteCount);
    }

    //Update Candidate Key
    public void updateCandidateKey(int electionId, String candidateKey) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(CANDIDATE_KEY, candidateKey);
        db.update(ELECTION_TABLE, cv, ELECTION_ID + "=" + electionId, null);
        String strSQL = "UPDATE "+ELECTION_TABLE+
                " SET "+CANDIDATE_KEY+"="+CANDIDATE_KEY+
                " WHERE "+ELECTION_ID+"="+ electionId;
        db.execSQL(strSQL);
        Log.i(TAG, "UPDATE CandidateKey = "+electionId+" "+ CANDIDATE_KEY);
    }

    public int getElectionId(String electionKey) {
        int electionId=-1;
        String entries= "SELECT *"
                + " FROM " + ELECTION_TABLE
                + " WHERE " + ELECTION_KEY +" = "+ "'"+electionKey+"'";
        SQLiteDatabase db = getWritableDatabase();
        Cursor c=db.rawQuery(entries,null);
        if(c!=null)
        {
            c.moveToFirst();
            electionId=Integer.parseInt(c.getString(c.getColumnIndex(ELECTION_ID)));
        }
        Log.i(TAG, "Get Election ID = "+electionId);
        return electionId;
    }

    public boolean containsElectionKey(String electionKey)
    {
        String entries= "SELECT *"
                + " FROM " + ELECTION_TABLE
                + " WHERE " + ELECTION_KEY +" = "+ "'"+electionKey+"'";

        SQLiteDatabase db =getWritableDatabase();

        Cursor c = db.rawQuery(entries,null);
        Log.i(TAG, "Present ? "+electionKey);
        if(c!=null)
        {
            return true;
        }

        return false;
    }

    //Selecting data from election table
    public List<ElectionData> getElectionData()
    {
        List<ElectionData> electionEntries = new ArrayList<>();
        String getAllElections = "SELECT * FROM " + ELECTION_TABLE ;

        SQLiteDatabase db=getWritableDatabase();

        Cursor c= db.rawQuery(getAllElections,null);
        c.moveToFirst();

        if(c.moveToFirst()){
            do{
                ElectionData electionData=new ElectionData();
                electionData.setElectionId(Integer.parseInt(c.getString(c.getColumnIndex(ELECTION_ID))));
                electionData.setElectionName(c.getString(c.getColumnIndex(ELECTION_NAME)));
                electionData.setCandidateKey(c.getString(c.getColumnIndex(CANDIDATE_KEY)));
                electionData.setElectionKey(c.getString(c.getColumnIndex(ELECTION_KEY)));
                electionData.setElectionDescription(c.getString(c.getColumnIndex(ELECTION_DESCRIPTION)));
                electionData.setWinner(c.getString(c.getColumnIndex(WINNER)));
                electionData.setOwner(Integer.parseInt(c.getString(c.getColumnIndex(OWNER))));
                electionData.setVoteCount(Integer.parseInt(c.getString(c.getColumnIndex(VOTE_COUNT))));
                electionData.setElectionStatus(c.getString(c.getColumnIndex(ELECTION_STATUS)));
                Log.i(TAG, "Select All data = "+electionData);
                electionEntries.add(electionData);

            }while(c.moveToNext());
        }

        return electionEntries;
    }
}
/*
//Update end date
    public void updateEndDate(int electionId, String endDate) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(END_DATE, endDate);
        db.update(ELECTION_TABLE, cv, ELECTION_ID + "=" + electionId, null);
        String strSQL = "UPDATE "+ELECTION_TABLE+
                "SET "+CANDIDATE_REGISTRATION_STATUS+"="+candidateRegValue+
                " WHERE "+ELECTION_ID+"="+ electionId;
        myDataBase.execSQL(strSQL);
    }
 */