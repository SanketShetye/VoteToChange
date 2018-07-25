package com.codeforfun.himanshu.votetochange.Adapters.RecyclerViewAdapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.codeforfun.himanshu.votetochange.DataObjetcs.CandidateData;
import com.codeforfun.himanshu.votetochange.OnClickToVote;
import com.codeforfun.himanshu.votetochange.R;

import java.util.List;

/**
 * Created by Himanshu on 19-03-2017.
 */

public class CandidateRecyclerViewAdapter extends RecyclerView.Adapter<CandidateRecyclerViewAdapter.ViewHolder> {

    Context mContext;
    List<CandidateData> candidateList;

    OnClickToVote onClickToVote;


    public CandidateRecyclerViewAdapter(Context mContext, List<CandidateData> candidateList, OnClickToVote onClickToVote) {
        this.mContext = mContext;
        this.candidateList = candidateList;
        this.onClickToVote = onClickToVote;
    }

    @Override
    public CandidateRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.card_view_candiate_details,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CandidateRecyclerViewAdapter.ViewHolder holder, int position) {
        CandidateData candidateData = candidateList.get(position);

        holder.candidateName.setText(candidateData.getUsername());
    }

    @Override
    public int getItemCount() {
        return candidateList==null?0:candidateList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        String candidateUsername;
        TextView candidateName;

        public ViewHolder(View itemView) {
            super(itemView);

            candidateName = (TextView) itemView.findViewById(R.id.candidateName);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickToVote.clickToVote(candidateUsername);
                }
            });
        }
    }
}
