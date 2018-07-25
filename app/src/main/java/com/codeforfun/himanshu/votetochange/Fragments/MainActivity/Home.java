package com.codeforfun.himanshu.votetochange.Fragments.MainActivity;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.codeforfun.himanshu.votetochange.AppConstants;
import com.codeforfun.himanshu.votetochange.Database.DatabaseHelper;
import com.codeforfun.himanshu.votetochange.NetworkCalls.BackgroundNetworkCall;
import com.codeforfun.himanshu.votetochange.NetworkHelper.UrlData;
import com.codeforfun.himanshu.votetochange.R;
import com.codeforfun.himanshu.votetochange.SelectCandidate;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 */
public class Home extends Fragment {

    private static final String TAG = "Home";

    private String mElectionKey = null;
    private String mCandidateKey = null;
    private String mElectionName =null;
    private int mElectionStatus = -1;
    private DatabaseHelper mDatabaseHelper;
    private Context mContext;
    public Home() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        mDatabaseHelper =new DatabaseHelper(getContext());
        mContext = getContext();

        view.findViewById(R.id.registerACandidate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerAsCandidate();
                //Use a dialog box and take a name for the candidate.
                //The name along with the username name should be registered with the server.

                //After the registration of candidate completes notify the user.

            }
        });


        view.findViewById(R.id.VoteButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                casteVote();
            }
        });



        return view;
    }

    public void registerAsCandidate()
    {
        getCandidateKey();
        if(mCandidateKey == null) {
            return;
        }
        else if (mCandidateKey.length() == 0) {
            Toast.makeText(getContext(), "Enter Correct Candidate Key", Toast.LENGTH_SHORT).show();
            return;
        }
        //Network Call
        //Check if key is present
        Log.i("Candidate",mCandidateKey);
        boolean result= false;
        try {
            result = getAcceptance();
            //-----check election Status too----
            //------accept elecionKey and name too-------
            //-----already exist------
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if(result)
        {
            //Add to database
            if(!mDatabaseHelper.containsElectionKey(mElectionKey))
            {
                mDatabaseHelper.addElectionEntry(mElectionName,mElectionKey,mCandidateKey,null,
                        null,0,mElectionStatus,0);
            }
            else{
                int electionId=mDatabaseHelper.getElectionId(mElectionKey);
                if(electionId!=-1) {
                    mDatabaseHelper.updateCandidateKey(electionId, mCandidateKey);
                    Log.i(TAG,"Updating Candidate Key");
                }
            }
            if(mElectionStatus==0){
                Toast.makeText(getContext(),"Successfull Registered",Toast.LENGTH_SHORT);
            }
            else{
                Toast.makeText(getContext(),"Time Elapsed: Registration process already ended",Toast.LENGTH_SHORT);
            }
        }
        else
        {
            Toast.makeText(getContext(),"Invalid Key",Toast.LENGTH_SHORT);
        }
    }

    public void getCandidateKey()
    {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());

        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_for_election_key_input,null);
        alertDialogBuilder.setView(dialogView);
        alertDialogBuilder.setTitle("Register as Candidate");
        final EditText key = (EditText) dialogView.findViewById(R.id.electionKeyInput);
        key.setHint("Enter Candidate Key");

        alertDialogBuilder.setCancelable(true)
                .setPositiveButton("Proceed", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mCandidateKey = key.getText().toString();
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

    public boolean getAcceptance() throws ExecutionException, InterruptedException {
        List<String> queryData = new ArrayList<>();

        SharedPreferences pref =getContext().getSharedPreferences("UserPrefs",MODE_PRIVATE);
        String username = pref.getString(AppConstants.SHARED_PREFS_USERNAME, "DEFAULT");

        queryData.add("username");
        queryData.add(username);
        queryData.add("candidateKey");
        queryData.add(mCandidateKey);
        Log.i("Candidate",username+" "+mCandidateKey);
        String value=new BackgroundNetworkCall().execute(getContext(),UrlData.REGISTER_AS_CANDIDATE,queryData);
        Log.i("Candidate",value);
        String input[]=value.split(" ");
        if(input[0].equals("1")||input[0].equals("0"))
        {
            mElectionName=input[1];
            mElectionKey=input[2];
            mElectionStatus= Integer.parseInt(input[3]);
            return true;
        }
        return false;
    }

    public void casteVote(){

        //We first need the user to input the election key
        getElectionKeyInput();

        //If the user had cancelled the dialog box then election key will be null.

    }

    public void getElectionKeyInput(){

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());

        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_for_election_key_input,null);
        alertDialogBuilder.setView(dialogView);
        alertDialogBuilder.setTitle("Vote");
        final EditText key = (EditText) dialogView.findViewById(R.id.electionKeyInput);

        alertDialogBuilder.setCancelable(true)
                .setPositiveButton("Proceed", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mElectionKey = key.getText().toString();
                        if(mElectionKey == null) {
                            return;
                        }
                        else if (mElectionKey.length() == 0) {
                            Toast.makeText(mContext, "Election Key Not Proper", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        //Now we need to make a network call to check wether the entered key is valid or not.
                        //After that we need to check whether the user has already voted or not in that election.
                        //Network Call
                        //Check if key is present
                        boolean result= false;
                        try {
                            result = getResult();
                            //--------------Get Elecion Status Also------
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        Log.i(TAG,""+result);
                        if(result)
                        {
                            if(!(mDatabaseHelper.containsElectionKey(mElectionKey)))
                            {
                                mDatabaseHelper.addElectionEntry(mElectionName,mElectionKey,null,
                                        null,null,0,mElectionStatus,0);
                                Log.i(TAG,"Adding new Entry");
                            }
                            else {
                                int electionId=mDatabaseHelper.getElectionId(mElectionKey);
                                if(electionId!=-1) {
                                    mDatabaseHelper.updateElectionStatus(electionId, mElectionStatus);
                                    Log.i(TAG,"Updating Election Status");
                                }
                            }
                            //getStatus too
                            Intent i=new Intent(getContext(), SelectCandidate.class);
                            i.putExtra("electionKey",mElectionKey);
                            i.putExtra("electionStatus",mElectionStatus);
                            startActivity(i);
                        }
                        else
                        {
                            Toast.makeText(mContext, "Election Key Not Proper", Toast.LENGTH_SHORT).show();
                        }
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

    public boolean getResult() throws ExecutionException, InterruptedException {
        List<String> queryData = new ArrayList<>();
        queryData.add("electionKey");
        queryData.add(mElectionKey);
        String value= new BackgroundNetworkCall().execute(mContext,UrlData.VALIDATING_ELECTION_KEY_URL,queryData);
        Log.i(TAG,value);
        String input[]=value.split(" ");
        if(input[0].equals("1")) {
            mElectionStatus=Integer.parseInt(input[1]);
            return true;
        }
        return false;
    }
}