package com.ucs.task2.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.ucs.task2.database.DatabaseHelper;
import com.ucs.task2.model.CheckInModels;
import com.ucs.task2.myapplication.CheckInAdapter;
import com.ucs.task2.myapplication.ItemActivity;
import com.ucs.task2.myapplication.MainActivity;
import com.ucs.task2.myapplication.R;
import com.ucs.task2.myapplication.RecyclerItemClickListener;

import java.util.ArrayList;

public class ListUIFragment extends Fragment {
    View view;
    RecyclerView rv_CheckInlist;
    ArrayList<CheckInModels> checkInList;
CheckInAdapter checkInAdapter;
    private DatabaseHelper databaseHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_list, container, false);

        rv_CheckInlist = view.findViewById(R.id.checkin_list);

        databaseHelper = new DatabaseHelper(getActivity());

        rv_CheckInlist.addOnItemTouchListener(
                new RecyclerItemClickListener(getActivity(), rv_CheckInlist ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {


                        Intent i = new Intent(getActivity(), ItemActivity.class);
                       i.putExtra("id", checkInList.get(position).getId());
                        startActivity(i);

                    }

                    @Override public void onLongItemClick(View view, int position) {
                        // do whatever
                    }
                })
        );


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        checkInList =   databaseHelper.getCheckInList();


        checkInAdapter = new CheckInAdapter(checkInList);


        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        rv_CheckInlist.setLayoutManager(mLayoutManager);
        rv_CheckInlist.setItemAnimator(new DefaultItemAnimator());
        rv_CheckInlist.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));

        rv_CheckInlist.setAdapter(checkInAdapter);




    }
}
