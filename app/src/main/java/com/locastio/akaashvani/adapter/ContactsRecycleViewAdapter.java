package com.locastio.akaashvani.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.locastio.akaashvani.R;
import com.parse.ParseUser;

import java.util.List;

/**
 * Created by Sujit on 22/08/15.
 */
public class ContactsRecycleViewAdapter extends RecyclerView.Adapter<ContactsRecycleViewAdapter.MyViewHolder> {
    List<ParseUser> data;

    public ContactsRecycleViewAdapter(Context context, List<ParseUser> information) {
        data = information;
    }

    public void addNewContactNumber(ParseUser parseUser) {
        data.add(parseUser);
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
        ParseUser localParseUser = data.get(position);

        if (!TextUtils.isEmpty(localParseUser.getString("fullname"))) {
            holder.usernameTextView.setText(localParseUser.getString("fullname"));
        }
        if (!TextUtils.isEmpty(localParseUser.getUsername())) {
            holder.numberTextView.setText(localParseUser.getUsername());
        }
//        holder.image.setImageResource(current.iconID);
//        Log.i("Custom Message", "onBindViewHolder " + current);
//        Log.i("Custom Message", "onBindViewHolder " + current.iconID);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView numberTextView;
        TextView usernameTextView;
//        ImageView image;

        public MyViewHolder(View itemView) {
            super(itemView);
            numberTextView = (TextView) itemView.findViewById(R.id.number_textview);
            usernameTextView = (TextView) itemView.findViewById(R.id.username_textview);
//            image = (ImageView) itemView.findViewById(R.id.list_img);
        }
    }
}
