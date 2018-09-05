package com.example.alton.myapplication.Profile;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.alton.myapplication.R;
import com.example.alton.myapplication.Models.User;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by alton on 1/17/2018.
 */

class UsersDetailsAdapter extends RecyclerView.Adapter<UsersDetailsAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<User> mData;

    public UsersDetailsAdapter(Context mContext, ArrayList<User> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    private Context getContext() {
        return mContext;
    }

    @Override
    public UsersDetailsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.user_details_layout, parent, false);
        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(UsersDetailsAdapter.ViewHolder holder, int position) {
        User user=mData.get(position);
        TextView userName=holder.textViewUserName;
        ImageView userPic=holder.imgViewUserPic;

        userName.setText(user.getFirstName());
        if (user.getUserPicUrl()!=null && !user.getUserPicUrl().isEmpty()) {
            Picasso.with(getContext())
                    .load(user.getUserPicUrl())
                    .into(userPic);
        }else {
            userPic.setImageResource(R.drawable.com_facebook_profile_picture_blank_square);
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        TextView textViewUserName;
        ImageView imgViewUserPic;
        User user;

        public ViewHolder(View itemView) {
            super(itemView);
            textViewUserName= (TextView) itemView.findViewById(R.id.textViewUserName);
            imgViewUserPic= (ImageView) itemView.findViewById(R.id.imageUserPic);
        }
    }

}
