package com.devabhi.circlecue.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.devabhi.circlecue.R;
import com.devabhi.circlecue.ui.model.socialmodel;

import java.util.ArrayList;


public class SocialRecyAdapter extends RecyclerView.Adapter<SocialRecyAdapter.MyViewHolder> {

    Context context;
    private ArrayList<socialmodel> bonusModelArrayList;

    public SocialRecyAdapter(Context context, ArrayList<socialmodel> bonusModelArrayList) {
        this.context = context;
        this.bonusModelArrayList = bonusModelArrayList;
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView textView, socialClickTextView;
        ImageView imageView;
        CheckBox privateCheckbox;



        MyViewHolder(View view){

            super(view);

            imageView =  view.findViewById(R.id.ivMain);
            textView = view.findViewById(R.id.socialTextView);
            socialClickTextView = view.findViewById(R.id.socialClickTextView);
            privateCheckbox = view.findViewById(R.id.privateCheckbox);



        }
    }


    @Override
    public SocialRecyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_social_list, parent, false);

        return new MyViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

        socialmodel lists = bonusModelArrayList.get(position);

        holder.imageView.setImageResource(lists.getImage());
        holder.textView.setText(lists.getText());
        holder.socialClickTextView.setText("Clicks "+lists.getSocialClickCount());

        if(Integer.parseInt(lists.getPrivateString()) == 0)
            holder.privateCheckbox.setChecked(false);
        else
            holder.privateCheckbox.setChecked(true);



    }

    @Override
    public int getItemCount() {

        return bonusModelArrayList.size();

    }

}


