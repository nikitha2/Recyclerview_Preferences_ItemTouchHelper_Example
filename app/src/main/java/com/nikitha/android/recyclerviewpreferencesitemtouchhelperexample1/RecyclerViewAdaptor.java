package com.nikitha.android.recyclerviewpreferencesitemtouchhelperexample1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerViewAdaptor extends RecyclerView.Adapter {
    ArrayList<ListItem> data;
    Context context;
    ListItemOnClickListener mListItemOnClickListener;

    public RecyclerViewAdaptor(Context mainActivity, ArrayList<ListItem> data, ListItemOnClickListener mListItemOnClickListener) {
            this.data=data;
            context=mainActivity;
            this.mListItemOnClickListener=mListItemOnClickListener;
        }
        interface ListItemOnClickListener{
           void OnItemClick(int position);
        }

        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        View itemView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.itemView=itemView;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mListItemOnClickListener.OnItemClick(getAdapterPosition());
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.listitem, parent, false);
        ViewHolder viewHolder=new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        View itemView = holder.itemView;
        TextView text = itemView.findViewById(R.id.text);
        text.setText(data.get(position).getText());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setData(ArrayList<ListItem> data) {
        data.addAll(data);
        notifyDataSetChanged();
    }
}
