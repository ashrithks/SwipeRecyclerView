package com.compass.ashrith.swipeablerecyclerview.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;


import com.compass.ashrith.swipeablerecyclerview.R;
import com.compass.ashrith.swipeablerecyclerview.adapter.ItemListRecyclerAdapter;
import com.compass.ashrith.swipeablerecyclerview.interfaces.IOnSwipe;
import com.compass.ashrith.swipeablerecyclerview.model.ItemModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements IOnSwipe {

    @Bind(R.id.mrecyclerView)
    RecyclerView recyclerView;
    ItemListRecyclerAdapter recyclerAdapter;
    List<ItemModel> itemList =new ArrayList<>();
    public static final int SWIPE_LEFT = -1;
    public static final int SWIPE_RIGHT = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setData();
        recyclerAdapter = new ItemListRecyclerAdapter(MainActivity.this, itemList,MainActivity.this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(recyclerAdapter);
    }



    private void setData() {
        for (int i=0;i<15;i++) {
            ItemModel itemModel = new ItemModel();
            itemModel.setText("Item "+i);
            itemModel.setItem(true);
            itemModel.setUndo(false);
            itemModel.setUndoIgnore(false);
            itemList.add(itemModel);
        }
    }

    @Override
    public void onSwipe(int position, int direction) {


        //Note:Direction of swipe
        if (direction==SWIPE_RIGHT){
            recyclerAdapter.remove(position);
        }
        if(direction==SWIPE_LEFT){
            recyclerAdapter.remove(position);
        }
    }


}
