package com.locastio.akaashvani.chat;

import android.app.Activity;
import android.content.Context;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.client.Query;
import com.locastio.akaashvani.R;

import java.util.Date;

/**
 * @author ketan
 * @since 6/21/13
 * <p>
 * This class is an example of how to use FirebaseListAdapter. It uses the <code>Chat</code> class to encapsulate the
 * data for each individual chat message
 */
public class ChatListAdapter extends FirebaseListAdapter<Chat> {

    // The mUsername for this client. We use this to indicate which messages originated from this user
    private String mUsername;
    private Activity mActivity;

    public ChatListAdapter(Query ref, Activity activity, int layout, String mUsername) {
        super(ref, Chat.class, layout, activity);
        this.mUsername = mUsername;
        this.mActivity = activity;
    }

    @Override
    public View getView(int pos, View v, ViewGroup viewGroup) {
        Chat c = (Chat) getItem(pos);
        Date currentTime = new Date();

        LayoutInflater li = (LayoutInflater) viewGroup.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (c.getAuthor().equals(mUsername))
            v = li.inflate(R.layout.chat_item_sent, null);
        else
            v = li.inflate(R.layout.chat_item_rcv, null);

        TextView lbl = (TextView) v.findViewById(R.id.lbl1);
        lbl.setText(c.getAuthor());
        lbl.setTextAppearance(v.getContext(), R.style.MyChatText);
        lbl = (TextView) v.findViewById(R.id.lbl2);
        lbl.setText(c.getMessage());

        lbl = (TextView) v.findViewById(R.id.lbl3);
        lbl.setText(DateUtils.getRelativeDateTimeString(v.getContext(),
                c.getDate(),
                DateUtils.SECOND_IN_MILLIS,
                DateUtils.DAY_IN_MILLIS, 0));


        return v;
    }
}
