package com.ucs.task2.myapplication;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ucs.task2.model.CheckInModels;

import java.util.List;

public class CheckInAdapter extends RecyclerView.Adapter<CheckInAdapter.MyViewHolder> {
    private List<CheckInModels> checkInList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView row;

        public MyViewHolder(View view) {
            super(view);
            row = (TextView) view.findViewById(R.id.row);

        }
    }


    public CheckInAdapter(List<CheckInModels> slabsList) {
        this.checkInList = slabsList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_list, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        CheckInModels checkins = checkInList.get(position);

        String text = "At " + checkins.getTime()+" on " +
                checkins.getDate()+", I visited "+checkins.getPlace()+" ."+checkins.getDescription()
                +".A photo is attached"
                ;
        holder.row.setText(text);

    }

    @Override
    public int getItemCount() {
        return checkInList.size();
    }
}
