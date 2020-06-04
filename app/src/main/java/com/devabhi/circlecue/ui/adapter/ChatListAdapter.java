package com.devabhi.circlecue.ui.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.devabhi.circlecue.Constants;
import com.devabhi.circlecue.MainActivity;
import com.devabhi.circlecue.R;
import com.devabhi.circlecue.ui.fragment.ChatRoomFragment;
import com.devabhi.circlecue.ui.fragment.ViewProfileFragment;
import com.devabhi.circlecue.ui.model.ChatListModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.ChatListViewHolder> {

    Context context;
    ArrayList<ChatListModel> chatListModelArrayList;
    SharedPreferences preferences;

    public ChatListAdapter(Context context, ArrayList<ChatListModel> chatListModelArrayList) {
        this.context = context;
        this.chatListModelArrayList = chatListModelArrayList;
    }

    class ChatListViewHolder extends RecyclerView.ViewHolder {

        TextView personNameTextView, timeTextView, placeHolderMessageTextView;
        ImageView profileImageView;


        public ChatListViewHolder(@NonNull View itemView) {
            super(itemView);

            personNameTextView = itemView.findViewById(R.id.personNameTextView);
            timeTextView = itemView.findViewById(R.id.timeTextView);
            placeHolderMessageTextView = itemView.findViewById(R.id.placeHolderMessageTextView);
            profileImageView = itemView.findViewById(R.id.chatHeadProfileImageView);
        }
    }


    @NonNull
    @Override
    public ChatListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        final View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.message_persons_list_item, parent, false);

        final ChatListViewHolder viewHolder = new ChatListViewHolder(itemView);

        preferences = context.getSharedPreferences(Constants.preference, Context.MODE_PRIVATE);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                preferences.edit().putString(Constants.othersID, chatListModelArrayList.get(viewHolder.getLayoutPosition()).getId()).apply();

                MainActivity mainActivity = (MainActivity) context;
                FragmentTransaction fragmentTransaction = mainActivity.getSupportFragmentManager().beginTransaction();
                fragmentTransaction.addToBackStack(null).replace(R.id.fragmentLoad, new ChatRoomFragment()).commit();

            }
        });


        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ChatListViewHolder holder, int position) {

        ChatListModel chatListModel = chatListModelArrayList.get(position);

        Picasso.get().load(Constants.downloadImageUrl + chatListModel.getPic()).placeholder(R.drawable.profile_picture_placeholder).into(holder.profileImageView);

        holder.personNameTextView.setText(chatListModel.getUsername());
        holder.placeHolderMessageTextView.setText(chatListModel.getMsg());
        holder.timeTextView.setText(chatListModel.getTime());


    }

    @Override
    public int getItemCount() {
        return chatListModelArrayList.size();
    }
}
