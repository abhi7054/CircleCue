package com.devabhi.circlecue.ui.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.devabhi.circlecue.Constants;
import com.devabhi.circlecue.MainActivity;
import com.devabhi.circlecue.R;
import com.devabhi.circlecue.ui.fragment.ViewProfileFragment;
import com.devabhi.circlecue.ui.model.GetSearch;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.MyViewHolder> implements Filterable {

    Context context;
    String id;
    private ArrayList<GetSearch> bonusModelArrayList;

    private ArrayList<GetSearch> filteredNameList;


    public SearchAdapter(Context context, ArrayList<GetSearch> bonusModelArrayList) {
        this.context = context;
        this.bonusModelArrayList = bonusModelArrayList;
        this.filteredNameList = new ArrayList<>(bonusModelArrayList);
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    private Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            ArrayList<GetSearch> filtered = new ArrayList<>();

            if(constraint.toString().isEmpty()){
                filtered.addAll(bonusModelArrayList);
            }else{

                for (GetSearch getSearch: bonusModelArrayList){

                    if(getSearch.getName().toLowerCase().contains(constraint.toString().toLowerCase())){
                        filtered.add(getSearch);
                    }

                }
            }

            FilterResults filterResults = new FilterResults();
            filterResults.values = filtered;

            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {

            filteredNameList.clear();
            filteredNameList.addAll((Collection<? extends GetSearch>) results.values);
            notifyDataSetChanged();

        }
    };


    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tv_name, tv_country;
        ImageView iv_image;


        public MyViewHolder(View view) {

            super(view);

            iv_image = view.findViewById(R.id.iv_image);
            tv_name = view.findViewById(R.id.tv_name);
            tv_country = view.findViewById(R.id.tv_country);

           int position = getAdapterPosition();


        }
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        final View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_search_list, parent, false);

        final MyViewHolder viewHolder = new MyViewHolder(itemView);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences preferences = context.getSharedPreferences(Constants.preference, Context.MODE_PRIVATE);
                preferences.edit().putBoolean(Constants.isMyProfile, false).apply();

                if(filteredNameList.size() != 0){
                    preferences.edit().putString(Constants.othersID, filteredNameList.get(viewHolder.getLayoutPosition()).getId()).apply();
                    preferences.edit().putString(Constants.othersPic, filteredNameList.get(viewHolder.getLayoutPosition()).getId()).apply();

                }else {
                    preferences.edit().putString(Constants.othersID, bonusModelArrayList.get(viewHolder.getLayoutPosition()).getId()).apply();
                    preferences.edit().putString(Constants.othersPic, bonusModelArrayList.get(viewHolder.getLayoutPosition()).getId()).apply();
                }
                MainActivity mainActivity = (MainActivity) context;
                FragmentTransaction fragmentTransaction = mainActivity.getSupportFragmentManager().beginTransaction();
                fragmentTransaction.addToBackStack(null).replace(R.id.fragmentLoad, new ViewProfileFragment()).commit();

            }
        });

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {


        GetSearch lists;

        if(filteredNameList.size() != 0)
           lists = filteredNameList.get(position);
        else
            lists = bonusModelArrayList.get(position);

        holder.tv_country.setText(lists.getCountry());
        holder.tv_name.setText(lists.getName());

        Picasso.get().load(Constants.downloadImageUrl + lists.getImage()).placeholder(R.drawable.profile_picture_placeholder).into(holder.iv_image);
        id = lists.getId();

    }

    @Override
    public int getItemCount() {

        if(filteredNameList.size() != 0)
            return filteredNameList.size();
        else
            return bonusModelArrayList.size();

    }



}


