package com.example.chatbox.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.chatbox.R;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatbox.pojo.MessagePojo;

import java.util.ArrayList;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    ArrayList<MessagePojo> arrayList;
    Context context;

    public MessageAdapter(ArrayList<MessagePojo> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @Override
    public MessageAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.recycler_item, parent, false);
        MessageAdapter.ViewHolder viewHolder = new MessageAdapter.ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MessageAdapter.ViewHolder holder, int position) {

        MessagePojo messagePojo = arrayList.get(position);
        holder.textViewTime.setText(messagePojo.getTime());
        holder.textMessage.setText(messagePojo.getMessage());


    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView textMessage;
        TextView textViewTime;

        public ViewHolder(View itemView) {
            super(itemView);
            textMessage = itemView.findViewById(R.id.textMessage);
            textViewTime = itemView.findViewById(R.id.textViewTime);
        }
    }
}

