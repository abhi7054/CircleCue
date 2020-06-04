package com.devabhi.circlecue.ui.adapter;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.devabhi.circlecue.R;
import com.devabhi.circlecue.ui.model.ViewProfileModel;

import java.util.ArrayList;


public class ViewProfileAdapter extends RecyclerView.Adapter<ViewProfileAdapter.ProfileViewHolder> {

    Context context;
    ArrayList<ViewProfileModel> viewProfileModelArrayList;


    public ViewProfileAdapter(Context context, ArrayList<ViewProfileModel> viewProfileModelArrayList) {
        this.context = context;
        this.viewProfileModelArrayList = viewProfileModelArrayList;

    }


    @NonNull
    @Override
    public ProfileViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View profileItemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view_profile_model, parent, false);

        profileItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



            }
        });


        return new ProfileViewHolder(profileItemView);

    }

    @Override
    public void onBindViewHolder(@NonNull final ProfileViewHolder holder, final int position) {

        ViewProfileModel viewProfileModel = viewProfileModelArrayList.get(position);

        holder.imageView.setImageResource(viewProfileModel.getImageID());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String url = viewProfileModelArrayList.get(position).getUrl();


                if(url.contains("http")){

                    Intent browserIntent = new Intent(Intent.ACTION_VIEW);
                    browserIntent.setData(Uri.parse(viewProfileModelArrayList.get(position).getUrl()));
                    context.startActivity(browserIntent);

                }else{

                    ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText(url, url);
                    clipboard.setPrimaryClip(clip);

                    Toast.makeText(context, "Username copied: "+ url, Toast.LENGTH_LONG).show();


                }

            }
        });


    }

    @Override
    public int getItemCount() {
        return viewProfileModelArrayList.size();
    }

    static class ProfileViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;

        ProfileViewHolder(@NonNull View itemView) {

            super(itemView);

            imageView = itemView.findViewById(R.id.viewProfileSocialImageView);
        }


    }

}
