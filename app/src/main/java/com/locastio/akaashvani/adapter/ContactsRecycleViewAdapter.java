package com.locastio.akaashvani.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.locastio.akaashvani.R;

import java.util.List;

/**
 * Created by Sujit on 22/08/15.
 */
public class ContactsRecycleViewAdapter extends RecyclerView.Adapter<ContactsRecycleViewAdapter.MyViewHolder> {
    List<String> data;

    public ContactsRecycleViewAdapter(Context context, List<String> information) {
        data = information;
    }

    public void addNewContactNumber(String number) {
        data.add(number);
        notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_conatcts_layout, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        String current = data.get(position);
        holder.title.setText(current);
//        holder.image.setImageResource(current.iconID);
        Log.i("Custom Message", "onBindViewHolder " + current);
//        Log.i("Custom Message", "onBindViewHolder " + current.iconID);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView title;
//        ImageView image;

        public MyViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.list_msg);
//            image = (ImageView) itemView.findViewById(R.id.list_img);
        }
    }
}
