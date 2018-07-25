package com.codeforfun.himanshu.votetochange.Fragments.MainActivity;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.codeforfun.himanshu.votetochange.Adapters.RecyclerViewAdapters.MyRecyclerViewAdapter;
import com.codeforfun.himanshu.votetochange.AppConstants;
import com.codeforfun.himanshu.votetochange.DataObjetcs.ElectionData;
import com.codeforfun.himanshu.votetochange.Database.DatabaseHelper;
import com.codeforfun.himanshu.votetochange.ElectionDisplay;
import com.codeforfun.himanshu.votetochange.NetworkCalls.BackgroundNetworkCall;
import com.codeforfun.himanshu.votetochange.NetworkHelper.UrlData;
import com.codeforfun.himanshu.votetochange.OnClickToDisplayElection;
import com.codeforfun.himanshu.votetochange.R;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static android.content.Context.MODE_PRIVATE;


public class MyElection extends Fragment {

    private Context mContext;

    private static final String LOG_TAG = "MyElection";

    private RecyclerView mRecyclerView;
    private MyRecyclerViewAdapter myRecyclerViewAdapter;

    private String mElectionName;
    private String mCandidateKey;
    private String mElectionKey;
    private String mElectionDescription;
    private boolean mNetworkValidation;
    private DatabaseHelper mDatabaseHelper;
    private List<ElectionData> election_data;

    public MyElection() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_election, container, false);

        mContext = getContext();

        mRecyclerView = (RecyclerView) view.findViewById(R.id.electionDataRecyclerView);

        election_data = getData();
        //get information from database

        myRecyclerViewAdapter = new MyRecyclerViewAdapter(getContext(), election_data, new OnClickToDisplayElection() {
            @Override
            public void displayElection(ElectionData electionData) {
                Intent i = new Intent(getContext(), ElectionDisplay.class);
                i.putExtra("electionKey",electionData.getElectionKey());
                i.putExtra("electionName",electionData.getElectionName());
                i.putExtra("candidateKey",electionData.getCandidateKey());
                i.putExtra("electionStatus",electionData.getElectionStatus());
                startActivity(i);
            }
        });
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(myRecyclerViewAdapter);
        mRecyclerView.setFocusable(false);

        view.findViewById(R.id.createElectionButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startNewElection();
            }
        });


        return view;
    }

    public void startNewElection() {

        InputNewElectionName();

        if (mElectionName == null)
            return;
        else if (mElectionName.length() == 0) {
            Toast.makeText(mContext, "Election Name Not Proper", Toast.LENGTH_SHORT).show();
            return;
        }

        SharedPreferences pref = getContext().getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String username = pref.getString(AppConstants.SHARED_PREFS_USERNAME, "DEFAULT");

        Log.i(LOG_TAG,"Network Call");
        mNetworkValidation = sendingData(username, mElectionName, mElectionDescription);

        if (mNetworkValidation == true) {
            Toast.makeText(mContext, "Successfull\nElection name : " + mElectionName, Toast.LENGTH_SHORT).show();
            Log.i(LOG_TAG,"Network Call Successfull");
            addDataToApapter(mElectionName, mElectionDescription);
            addToDatabase();
        }
        else
        {
            Log.i(LOG_TAG,"Network call issue");
            Toast.makeText(mContext,"Network Problem", Toast.LENGTH_SHORT).show();
        }
    }

    public void InputNewElectionName() {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
        alertDialogBuilder.setTitle("Create new Election");

        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_for_creating_new_election, null);
        alertDialogBuilder.setView(dialogView);

        final EditText name = (EditText) dialogView.findViewById(R.id.electionKeyInput);
        final EditText description = (EditText) dialogView.findViewById(R.id.electionDescription);
        name.setHint("Enter Election Name");


        alertDialogBuilder.setCancelable(true)
                .setPositiveButton("Create", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mElectionName = name.getText().toString();
                        mElectionDescription = description.getText().toString();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public String getCurrentDate() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String formattedDate = df.format(c.getTime());
        return formattedDate;
    }

    public boolean sendingData(String username, String name, String description) {

        List<String> queryData = new ArrayList<>();
        queryData.add("username");
        queryData.add(username);
        queryData.add("election_name");
        queryData.add(name);
        queryData.add("description");
        queryData.add(description);

        try {
            String electionResult = new BackgroundNetworkCall().execute(getContext(),UrlData.REGISTER_ELECTION_URL, queryData);
            Log.i(AppConstants.TAG, "Election Result = " + electionResult);
            String resultarray[] = electionResult.split(" ");
            Log.i(AppConstants.TAG, "Result array = " + resultarray[3]);

            if (electionResult != null && resultarray[3].equals("1")) {
                mElectionKey = resultarray[0];
                mCandidateKey = resultarray[1];
                Log.i(AppConstants.TAG, "Election Created");

                return true;
            } else {
                Toast.makeText(getContext(), "Network error", Toast.LENGTH_SHORT).show();
                //notifyLoginFailed();
                return false;
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void addDataToApapter(String name, String description) {
        ElectionData data = new ElectionData();

        data.setElectionName(name);
        data.setStartDate(getCurrentDate());
        data.setVoteCount(0);
        data.setElectionDescription(description);

        election_data.add(data);
    }

    public void addToDatabase()
    {
        mDatabaseHelper.addElectionEntry(mElectionName,mElectionKey,mCandidateKey,mElectionDescription,null,0,0,1);
        //null-winner
        //0-voteCount
        //0-electionStatus
        //1-Owner
    }

    public List<ElectionData> getData() {
        mDatabaseHelper = new DatabaseHelper(getContext());
        List<ElectionData> da = mDatabaseHelper.getElectionData();
        return da;
    }

}
