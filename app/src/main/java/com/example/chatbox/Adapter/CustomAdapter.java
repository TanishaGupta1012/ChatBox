package com.example.chatbox.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.chatbox.R;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.chatbox.Activity.ChatRoomActivity;
import com.example.chatbox.pojo.UserPojo;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder>{

    ArrayList<UserPojo> arrayList;
    Context context;
    public CustomAdapter(ArrayList<UserPojo> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.list_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        final UserPojo userPojo = arrayList.get(position);
        holder.textNumber.setText(userPojo.getPhoneno());
        holder.textName.setText(userPojo.getName());
        Glide.with(context).load(userPojo.getImage()).into(holder.imageProfile);
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, ChatRoomActivity.class);
                intent.putExtra("imageUrl",userPojo.getImage());
                intent.putExtra("name",userPojo.getName());
                intent.putExtra("id",userPojo.getUserid());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageProfile;
        public TextView textName;
        public TextView textNumber;
        public LinearLayout linearLayout;
        public ViewHolder(View itemView) {
            super(itemView);
            imageProfile = itemView.findViewById(R.id.imageProfile);
            textName = itemView.findViewById(R.id.textName);
            textNumber = itemView.findViewById(R.id.textNumber);
            linearLayout = itemView.findViewById(R.id.linearLayout);
        }
    }
}

