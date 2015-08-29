package com.locastio.akaashvani.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.locastio.akaashvani.Information;
import com.locastio.akaashvani.R;

import java.util.List;

/**
 * Created by Sujit on 22/08/15.
 */
public class GroupRecycleViewAdapter extends RecyclerView.Adapter<GroupRecycleViewAdapter.MyViewHolder> {
    List<Information> data;

    public GroupRecycleViewAdapter(Context context, List<Information> information) {
        data = information;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_grp_layout, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Information current = data.get(position);
        holder.title.setText(current.title);
//        holder.image.setImageResource(current.iconID);
        Log.i("Custom Message", "onBindViewHolder " + current.title);
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
