package com.devabhi.circlecue.ui.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.devabhi.circlecue.Constants;
import com.devabhi.circlecue.R;
import com.devabhi.circlecue.ui.model.ChatRoomDataModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ChatRoomAdapter extends RecyclerView.Adapter<ChatRoomAdapter.ViewHolder> {

    Context context;
    SharedPreferences preferences;

    public ChatRoomAdapter(Context context, ArrayList<ChatRoomDataModel> chatRoomDataModelArrayList) {
        this.context = context;
        this.chatRoomDataModelArrayList = chatRoomDataModelArrayList;
    }

    ArrayList<ChatRoomDataModel> chatRoomDataModelArrayList;

    class ViewHolder extends RecyclerView.ViewHolder{

        TextView sentMessageTextView, receivedMessageTextView;
        ImageView sentImageView, receivedImageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            sentMessageTextView = itemView.findViewById(R.id.sentMessageTextView);
            receivedMessageTextView = itemView.findViewById(R.id.receivedMessageTextView);
            sentImageView = itemView.findViewById(R.id.sentImageView);
            receivedImageView = itemView.findViewById(R.id.receivedImageView);
        }
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        final View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_chat_room, parent, false);

        ViewHolder viewHolder = new ViewHolder(itemView);



        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        preferences = context.getSharedPreferences(Constants.preference, Context.MODE_PRIVATE);

        holder.sentImageView.setVisibility(View.GONE);
        holder.receivedImageView.setVisibility(View.GONE);

        ChatRoomDataModel chatRoomData = chatRoomDataModelArrayList.get(position);

        if(chatRoomData.getSenderId().equals(preferences.getString(Constants.id, ""))){
            holder.sentMessageTextView.setText(chatRoomData.getMessage());
            holder.sentMessageTextView.setVisibility(View.VISIBLE);
            holder.receivedMessageTextView.setVisibility(View.GONE);
            holder.sentImageView.setVisibility(View.GONE);

            if(!chatRoomData.getMedia().equals("")){
                holder.sentImageView.setVisibility(View.VISIBLE);
                Picasso.get().load(Constants.downloadImageUrl+chatRoomData.getMedia()).into(holder.sentImageView);

            }
        }

        else {
            holder.receivedMessageTextView.setText(chatRoomData.getMessage());

            holder.sentMessageTextView.setVisibility(View.GONE);
            holder.receivedMessageTextView.setVisibility(View.VISIBLE);

            if(!chatRoomData.getMedia().equals("")){
                holder.receivedImageView.setVisibility(View.VISIBLE);
                Picasso.get().load(Constants.downloadImageUrl+chatRoomData.getMedia()).into(holder.receivedImageView);

            }
        }

    }


    @Override
    public int getItemCount() {
        return chatRoomDataModelArrayList.size();
    }
}
