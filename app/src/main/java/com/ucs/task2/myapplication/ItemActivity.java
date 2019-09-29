package com.ucs.task2.myapplication;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.ucs.task2.database.DatabaseHelper;
import com.ucs.task2.fragments.ItemFragment;

public class ItemActivity extends AppCompatActivity {
    Intent intent;
int id = -1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);
      //  DatabaseHelper databaseHelper = new DatabaseHelper(ItemActivity.this);
        intent = getIntent();
        if(intent!=null){

            id= intent.getIntExtra("id",-1);
        }


        loadFragment(new ItemFragment(),id);


    }


    private void loadFragment(Fragment fragment,int id) {

        Bundle bundle = new Bundle();
            bundle.putInt("id",id);
            fragment.setArguments(bundle);
        FragmentManager fm = getSupportFragmentManager();

        FragmentTransaction fragmentTransaction = fm.beginTransaction();

        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit(); // save the changes
    }

}
