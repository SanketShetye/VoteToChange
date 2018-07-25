package com.codeforfun.himanshu.votetochange.Adapters.RecyclerViewAdapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.codeforfun.himanshu.votetochange.AppConstants;
import com.codeforfun.himanshu.votetochange.DataObjetcs.ElectionData;
import com.codeforfun.himanshu.votetochange.OnClickToDisplayElection;
import com.codeforfun.himanshu.votetochange.OnClickToVote;
import com.codeforfun.himanshu.votetochange.R;

import java.util.List;


public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.ViewHolder> {

    private List<ElectionData> list;
    private Context mContext;
    private OnClickToDisplayElection onClickToDisplayElection;

    public MyRecyclerViewAdapter(Context context, List<ElectionData> list, OnClickToDisplayElection onClickToDisplayElection) {
        mContext=context;
        this.list = list;
        this.onClickToDisplayElection=onClickToDisplayElection;
    }

    @Override
    public MyRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyRecyclerViewAdapter.ViewHolder holder, int position) {

        ElectionData data = list.get(position);

        holder.voteCount.setText(holder.voteCount.getText()+""+data.getVoteCount());
        holder.startDate.setText(holder.startDate.getText()+""+data.getStartDate());
        holder.electionName.setText(data.getElectionName());
    }

    @Override
    public int getItemCount() {
        return list==null?0:list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView electionName,startDate,voteCount;

        public ViewHolder(View itemView) {
            super(itemView);

            electionName = (TextView) itemView.findViewById(R.id.electionName);
            startDate = (TextView) itemView.findViewById(R.id.startDate);
            voteCount = (TextView) itemView.findViewById(R.id.voteCount);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int itemPosition = getLayoutPosition();
                    ElectionData item = list.get(itemPosition);
                    Log.i("MyRecyclerView",""+itemPosition);
                    onClickToDisplayElection.displayElection(item);
                }
            });
        }
    }
}
