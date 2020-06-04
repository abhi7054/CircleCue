package com.devabhi.circlecue.ui.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.devabhi.circlecue.Constants;
import com.devabhi.circlecue.MainActivity;
import com.devabhi.circlecue.R;
import com.devabhi.circlecue.ui.fragment.MyCircleFragment;
import com.devabhi.circlecue.ui.fragment.ViewProfileFragment;
import com.devabhi.circlecue.ui.model.CircleItemDataModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyCircleAdapter  extends RecyclerView.Adapter<MyCircleAdapter.CircleListViewHolder>{

    Context context;
    ArrayList<CircleItemDataModel> circleItemDataModelArrayList;
    SharedPreferences preferences;

    public MyCircleAdapter(Context context, ArrayList<CircleItemDataModel> circleItemDataModelArrayList) {
        this.context = context;
        this.circleItemDataModelArrayList = circleItemDataModelArrayList;

    }

    @NonNull
    @Override
    public CircleListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_my_circle_list, parent, false);

        final CircleListViewHolder viewHolder = new CircleListViewHolder(itemView);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences preferences = context.getSharedPreferences(Constants.preference, Context.MODE_PRIVATE);
                preferences.edit().putBoolean(Constants.isMyProfile, false).apply();

                preferences.edit().putString(Constants.othersID, circleItemDataModelArrayList.get(viewHolder.getLayoutPosition()).getId()).apply();
                preferences.edit().putString(Constants.othersPic, circleItemDataModelArrayList.get(viewHolder.getLayoutPosition()).getId()).apply();

                MainActivity mainActivity = (MainActivity) context;
                FragmentTransaction fragmentTransaction = mainActivity.getSupportFragmentManager().beginTransaction();
                fragmentTransaction.addToBackStack(null).replace(R.id.fragmentLoad, new ViewProfileFragment()).commit();
            }


        });

        return viewHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull CircleListViewHolder holder, int position) {

        final CircleItemDataModel circleItemDataModel = circleItemDataModelArrayList.get(position);
        holder.nameTextView.setText(circleItemDataModel.getUsername());
        holder.countryTextView.setText(circleItemDataModel.getCountry());

        preferences = context.getSharedPreferences(Constants.preference, Context.MODE_PRIVATE);

        if(circleItemDataModel.getStatus().equals("1")){

            holder.acceptButton.setText("Remove from circle");
            holder.declineButton.setVisibility(View.GONE);

            holder.acceptButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    declineRequest(circleItemDataModel.getRowId());
                    Toast.makeText(context, "Removed from Circle", Toast.LENGTH_SHORT).show();




                }
            });



        }else if(!circleItemDataModel.getFromID().equals(preferences.getString(Constants.id, ""))){


            holder.declineButton.setVisibility(View.VISIBLE);
            holder.acceptButton.setVisibility(View.VISIBLE);
            holder.acceptButton.setText("Accept");
            holder.declineButton.setText("Decline");

            holder.acceptButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    acceptRequest(circleItemDataModel.getRowId());
                }
            });

            holder.declineButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    declineRequest(circleItemDataModel.getRowId());
                    Toast.makeText(context, "Declined request", Toast.LENGTH_SHORT).show();

                }
            });

        }else{

            holder.declineButton.setVisibility(View.VISIBLE);
            holder.acceptButton.setVisibility(View.VISIBLE);
            holder.acceptButton.setText("Pending for approval");
            holder.declineButton.setText("Cancel Request");

            holder.declineButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    declineRequest(circleItemDataModel.getRowId());
                    Toast.makeText(context, "Request Cancelled", Toast.LENGTH_SHORT).show();


                }
            });

        }





        Picasso.get().load(Constants.downloadImageUrl + circleItemDataModel.getPic()).placeholder(R.drawable.profile_picture_placeholder).into(holder.profilePicImageView);


    }

    @Override
    public int getItemCount() {
        return circleItemDataModelArrayList.size();
    }

    void acceptRequest(String id){

        StringRequest acceptRequest = new StringRequest(Request.Method.GET, "http://circlecue.com/api/accept.php?id=" + id, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                notifyDataSetChanged();
                Toast.makeText(context, "Request Accepted", Toast.LENGTH_SHORT).show();

                MainActivity mainActivity = (MainActivity) context;
                FragmentTransaction fragmentTransaction = mainActivity.getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragmentLoad, new MyCircleFragment()).commit();


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

            acceptRequest.setRetryPolicy(new DefaultRetryPolicy(
                5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue loginQueue = Volley.newRequestQueue(context);

        loginQueue.add(acceptRequest);

    }

    void declineRequest(String id){

        StringRequest acceptRequest = new StringRequest(Request.Method.GET, "http://circlecue.com/api/decline.php?id=" + id, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                notifyDataSetChanged();

                MainActivity mainActivity = (MainActivity) context;
                FragmentTransaction fragmentTransaction = mainActivity.getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragmentLoad, new MyCircleFragment()).commit();


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        acceptRequest.setRetryPolicy(new DefaultRetryPolicy(
                5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue loginQueue = Volley.newRequestQueue(context);

        loginQueue.add(acceptRequest);


    }




    class CircleListViewHolder extends RecyclerView.ViewHolder {

        CircleImageView profilePicImageView;
        TextView nameTextView, countryTextView;
        Button acceptButton, declineButton;

        public CircleListViewHolder(@NonNull View itemView) {
            super(itemView);

            profilePicImageView = itemView.findViewById(R.id.circleProfileImageView);
            nameTextView = itemView.findViewById(R.id.personCircleNameTextView);
            countryTextView = itemView.findViewById(R.id.countryNameCircleTextView);
            acceptButton = itemView.findViewById(R.id.acceptButton);
            declineButton = itemView.findViewById(R.id.declineButton);

        }
    }
}
