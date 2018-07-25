package com.codeforfun.himanshu.votetochange;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.codeforfun.himanshu.votetochange.Adapters.RecyclerViewAdapters.CandidateRecyclerViewAdapter;
import com.codeforfun.himanshu.votetochange.DataObjetcs.CandidateData;
import com.codeforfun.himanshu.votetochange.NetworkCalls.BackgroundNetworkCall;
import com.codeforfun.himanshu.votetochange.NetworkHelper.UrlData;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class ElectionDisplay extends AppCompatActivity {

    private TextView mElectionName, mElectionVoteCount;
    private RecyclerView mCandidateRecyclerView;
    private CandidateRecyclerViewAdapter mCandidateRecyclerViewAdapter;
    private List<CandidateData> candidateData;
    private SwipeRefreshLayout mSwipeRefreshLayoutCandidate;
    private String mElectionKey;
    private String mCandidateKey;
    private String mElectionStatus;

    private static final String LOG_TAG = "ElectionDisplay";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_election_display);

        mSwipeRefreshLayoutCandidate = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        mCandidateRecyclerView = (RecyclerView) findViewById(R.id.candidateList);
        mElectionName = (TextView) findViewById(R.id.electionName);


        Bundle bundle = getIntent().getExtras();
        //ElectionData electionData = (ElectionData) getIntent().getSerializableExtra("electionData");
        String electionName = bundle.getString("electionName");
        mCandidateKey = bundle.getString("candidateKey");
        mElectionKey = bundle.getString("electionKey");
        mElectionStatus = bundle.getString("electionStatus");

        Log.i(LOG_TAG, electionName + "  " + mElectionKey + "  " + mCandidateKey + "  " + mElectionStatus);

        mElectionName.setText(electionName);

        try {
            candidateData = getData();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        setupSwipeRefreshLayoutCandidate();

        setAdapter();

        OnClickButtonListner((Button) findViewById(R.id.candidateButton), mCandidateKey);
        OnClickButtonListner((Button) findViewById(R.id.electionKeyButton), mElectionKey);

        findViewById(R.id.startElection).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startElection();
            }
        });

        findViewById(R.id.endElection).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                endElection();
            }
        });

    }

    public void setupSwipeRefreshLayoutCandidate() {
        //SwipeRefreshLayout
        mSwipeRefreshLayoutCandidate.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
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
        mSwipeRefreshLayoutCandidate.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

    }

    public void setAdapter() {
        //Candidate Adapter
        mCandidateRecyclerViewAdapter = new CandidateRecyclerViewAdapter(this, candidateData, new OnClickToVote() {
            @Override
            public void clickToVote(String username) {
                //------send data and check Status-------
                //------if successfull show result-------
            }
        });
        mCandidateRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mCandidateRecyclerView.getLayoutManager().setAutoMeasureEnabled(true);
        mCandidateRecyclerView.setNestedScrollingEnabled(false);
        mCandidateRecyclerView.setHasFixedSize(false);
        mCandidateRecyclerView.setAdapter(mCandidateRecyclerViewAdapter);
    }

    public void OnClickButtonListner(Button but, final String value) {
        but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, value);
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
            }
        });
    }

    public void startElection()
    {
        SharedPreferences pref = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String username = pref.getString(AppConstants.SHARED_PREFS_USERNAME, "DEFAULT");
        List<String> queryData =new ArrayList<>();
        queryData.add("electionKey");
        queryData.add(mElectionKey);
        queryData.add("username");
        queryData.add(username);

        try {
            String value = new BackgroundNetworkCall().execute(this,UrlData.START_ELECTION,queryData);
            if(value.equals("1"))
            {
                Toast.makeText(this,"Election Process Started",Toast.LENGTH_SHORT);
            }
            else{
                Toast.makeText(this,"Network Issue",Toast.LENGTH_SHORT);
            }

        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void endElection()
    {
        SharedPreferences pref = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String username = pref.getString(AppConstants.SHARED_PREFS_USERNAME, "DEFAULT");
        List<String> queryData =new ArrayList<>();
        queryData.add("electionKey");
        queryData.add(mElectionKey);
        queryData.add("username");
        queryData.add(username);

        try {
            String value = new BackgroundNetworkCall().execute(this,UrlData.START_ELECTION,queryData);
            if(value.equals("1"))
            {
                Toast.makeText(this,"Election Process Ended",Toast.LENGTH_SHORT);
            }
            else{
                Toast.makeText(this,"Network Issue",Toast.LENGTH_SHORT);
            }

        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public void update() throws ExecutionException, InterruptedException {
        mSwipeRefreshLayoutCandidate.setRefreshing(true);
        List<CandidateData> cd=getData();
        candidateData.clear();
        candidateData=cd;
        mSwipeRefreshLayoutCandidate.setRefreshing(false);
    }

    public List<CandidateData> getData() throws ExecutionException, InterruptedException
    {
        List<CandidateData> cd=getNetworkCallData();
        return cd;
    }

    public List<CandidateData> getNetworkCallData() throws ExecutionException, InterruptedException
    {
        List<CandidateData> list=new ArrayList<>();
        List<String> queryData=new ArrayList<>();
        queryData.add("electionKey");
        queryData.add(mElectionKey);
        Log.i(LOG_TAG,mElectionKey);
        String value=new BackgroundNetworkCall().execute(this,UrlData.CANDIDATE_LIST_URL,queryData);
        Log.i(LOG_TAG,value);
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
}
