package com.codeforfun.himanshu.votetochange;

import android.content.SharedPreferences;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.codeforfun.himanshu.votetochange.Adapters.RecyclerViewAdapters.CandidateRecyclerViewAdapter;
import com.codeforfun.himanshu.votetochange.DataObjetcs.CandidateData;
import com.codeforfun.himanshu.votetochange.Database.DatabaseHelper;
import com.codeforfun.himanshu.votetochange.NetworkCalls.BackgroundNetworkCall;
import com.codeforfun.himanshu.votetochange.NetworkHelper.UrlData;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class SelectCandidate extends AppCompatActivity {

    private static final String TAG = "Select Candidate";
    private RecyclerView mRecyclerView;
    private CandidateRecyclerViewAdapter mRecyclerViewAdapter;
    //private SwipeRefreshLayout mSwipeRefreshLayout;
    private String mElectionKey;
    private int mElectionStatus;
    private List<CandidateData> candidateData;
    private DatabaseHelper mDatabaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_candidate);

        //mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.candidateSwipeRefreshLayout);
        mDatabaseHelper = new DatabaseHelper(this);

        Bundle bundle = getIntent().getExtras();
        mElectionKey = bundle.getString("electionKey");
        mElectionStatus = getIntent().getIntExtra("electionStatus", 0);
        Log.i(TAG, mElectionKey);



        //setupSwipeRefreshLayoutCandidate();

        try {
            candidateData = getCandidateData();
            Log.i(TAG, "" + candidateData.size());
            Log.i(TAG, "" + candidateData.get(0).getUsername());
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        mRecyclerView = (RecyclerView) findViewById(R.id.candidateRecyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerViewAdapter = new CandidateRecyclerViewAdapter(this, candidateData, new OnClickToVote() {
            @Override
            public void clickToVote(String candidateUsername) {
                voteForCandidate(candidateUsername);
            }
        });

        mRecyclerView.setAdapter(mRecyclerViewAdapter);
        Log.i(TAG,""+mRecyclerViewAdapter.getItemCount());

    }

    /*public void setupSwipeRefreshLayoutCandidate() {
        //SwipeRefreshLayout
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                try {
                    update();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
    }

    public void update() throws ExecutionException, InterruptedException {
        mSwipeRefreshLayout.setRefreshing(true);
        candidateData.clear();
        List<CandidateData> cd = getCandidateData();
        candidateData = cd;
        mSwipeRefreshLayout.setRefreshing(false);
    }*/

    public void voteForCandidate(String username) {

        //Make a network call to record this users vote for the candidate with 'username'.
        if (mElectionStatus == 0) {
            Toast.makeText(getApplicationContext(), "Error : Election not started", Toast.LENGTH_SHORT);
        } else if (mElectionStatus == 1) {
            //-----Vote--for CandidateUsername
            //-----Send your username and electionKey
            boolean result = false;
            try {
                result = getResult(username);
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (result) {

            } else {

            }
        } else {
            Toast.makeText(getApplicationContext(), "Time Elapsed : Election already finished", Toast.LENGTH_SHORT);
        }
        //Show the user the vote is successfully recorded and then go to home screen.

    }

    //Candidate List
    public List<CandidateData> getCandidateData() throws ExecutionException, InterruptedException {
        List<CandidateData> list = getNetworkCallData();
        return list;
    }

    //Network call for candidate list
    public List<CandidateData> getNetworkCallData() throws ExecutionException, InterruptedException
    {
        List<CandidateData> list=new ArrayList<>();
        List<String> queryData=new ArrayList<>();
        queryData.add("electionKey");
        queryData.add(mElectionKey);
        Log.i(TAG,mElectionKey);
        String value=new BackgroundNetworkCall().execute(this,UrlData.CANDIDATE_LIST_URL,queryData);
        Log.i(TAG,value);
        if(value.contains("Error"))
        {
            Toast.makeText(this,"Candidate=0",Toast.LENGTH_SHORT);
            return list;
        }
        String candidateList[]=value.split("#1234#");
        for (String e : candidateList)
        {
            Log.i("Candidate",e);
            CandidateData cd=new CandidateData();
            cd.setUsername(e);
            list.add(cd);
        }
        return list;
    }
    /*public List<CandidateData> getDataFromCall() throws ExecutionException, InterruptedException {
        List<CandidateData> list = new ArrayList<>();
        List<String> queryData = new ArrayList<>();
        queryData.add("electionKey");
        queryData.add(mElectionKey);
        Log.i("Select Candidate", mElectionKey);
        String value = new BackgroundNetworkCall().execute(this, UrlData.CANDIDATE_LIST_URL, queryData);
        Log.i("Select Candidate", value);
        String candidateList[] = value.split("#1234#");
        for (String e : candidateList) {
            Log.i("Candidate", e);
            CandidateData cd = new CandidateData();
            cd.setUsername(e);
            list.add(cd);
        }
        return list;
    }*/

    public boolean getResult(String candidateName) throws ExecutionException, InterruptedException {

        SharedPreferences pref = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String username = pref.getString(AppConstants.SHARED_PREFS_USERNAME, "DEFAULT");
        List<String> queryData = new ArrayList<>();
        queryData.add("username");
        queryData.add(username);
        queryData.add("candidateName");
        queryData.add(candidateName);
        queryData.add("electionKey");
        queryData.add(mElectionKey);
        String value = new BackgroundNetworkCall().execute(this, UrlData.VOTE_FOR_CANDIDATE, queryData);
        if (value.equals("1")) {
            return true;
        }
        return false;
    }

    public void getElectionStatus() throws ExecutionException, InterruptedException {
        List<String> queryData = new ArrayList<>();
        queryData.add("electionKey");
        queryData.add(mElectionKey);
        String value = new BackgroundNetworkCall().execute(this, UrlData.ELECTION_STATUS, queryData);
        if (!value.equals("-1")) {
            mElectionStatus = Integer.parseInt(value);
            int electionId = mDatabaseHelper.getElectionId(mElectionKey);
            if (electionId != -1) {
                mDatabaseHelper.updateElectionStatus(electionId, mElectionStatus);
                Log.i(TAG, "Updating Election Status");
            }
        }
    }
}
/*
java.net.SocketTimeoutException: failed to connect to codeforfun.16mb.com/31.170.165.231 (port 80) after 10000ms
W/System.err:     at libcore.io.IoBridge.connectErrno(IoBridge.java:169)
W/System.err:     at libcore.io.IoBridge.connect(IoBridge.java:122)
W/System.err:     at java.net.PlainSocketImpl.connect(PlainSocketImpl.java:183)
W/System.err:     at java.net.PlainSocketImpl.connect(PlainSocketImpl.java:456)
W/System.err:     at java.net.Socket.connect(Socket.java:882)
07-05 12:12:06.728 12864-13709/com.codeforfun.himanshu.votetochange W/System.err:     at com.android.okhttp.internal.Platform.connectSocket(Platform.java:139)
07-05 12:12:06.728 12864-13709/com.codeforfun.himanshu.votetochange W/System.err:     at com.android.okhttp.Connection.connect(Connection.java:1194)
07-05 12:12:06.728 12864-13709/com.codeforfun.himanshu.votetochange W/System.err:     at com.android.okhttp.internal.http.HttpEngine.connect(HttpEngine.java:393)
07-05 12:12:06.728 12864-13709/com.codeforfun.himanshu.votetochange W/System.err:     at com.android.okhttp.internal.http.HttpEngine.sendRequest(HttpEngine.java:296)
07-05 12:12:06.728 12864-13709/com.codeforfun.himanshu.votetochange W/System.err:     at com.android.okhttp.internal.http.HttpURLConnectionImpl.execute(HttpURLConnectionImpl.java:399)
07-05 12:12:06.728 12864-13709/com.codeforfun.himanshu.votetochange W/System.err:     at com.android.okhttp.internal.http.HttpURLConnectionImpl.connect(HttpURLConnectionImpl.java:110)
07-05 12:12:06.728 12864-13709/com.codeforfun.himanshu.votetochange W/System.err:     at com.android.okhttp.internal.http.HttpURLConnectionImpl.getOutputStream(HttpURLConnectionImpl.java:221)
 */