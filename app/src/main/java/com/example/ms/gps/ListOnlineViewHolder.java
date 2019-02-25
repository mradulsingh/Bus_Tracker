package com.example.ms.gps;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

public class ListOnlineViewHolder extends RecyclerView.ViewHolder {

    public TextView txtEmail;
    public ListOnlineViewHolder(View itemView){
        super(itemView);
        txtEmail = itemView.findViewById(R.id.item_title);
    }
}
