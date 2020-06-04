package com.devabhi.circlecue.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.devabhi.circlecue.R;
import com.devabhi.circlecue.ui.gallery.DrawerModel;


import java.util.ArrayList;


public class DrawerRecyAdapter extends RecyclerView.Adapter<DrawerRecyAdapter.MyViewHolder> {

    Context context;
    private ArrayList<DrawerModel> bonusModelArrayList;

    public DrawerRecyAdapter(Context context, ArrayList<DrawerModel> bonusModelArrayList) {
        this.context = context;
        this.bonusModelArrayList = bonusModelArrayList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView textView;
        ImageView imageView;



        public MyViewHolder(View view){

            super(view);

            textView =  view.findViewById(R.id.tvTitle);
            imageView =  view.findViewById(R.id.ivMain);


        }
    }


    @Override
    public DrawerRecyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_drawer_list, parent, false);

        return new DrawerRecyAdapter.MyViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

        DrawerModel lists = bonusModelArrayList.get(position);

        holder.textView.setText(lists.getTitle());
        holder.imageView.setImageResource(lists.getImage());



    }

    @Override
    public int getItemCount() {

        return bonusModelArrayList.size();

    }

}


