package com.locastio.akaashvani.chat;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.client.Query;
import com.locastio.akaashvani.R;

/**
 * @author ketan
 * @since 6/21/13
 * <p/>
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

    /**
     * Bind an instance of the <code>Chat</code> class to our view. This method is called by <code>FirebaseListAdapter</code>
     * when there is a data change, and we are given an instance of a View that corresponds to the layout that we passed
     * to the constructor, as well as a single <code>Chat</code> instance that represents the current data to bind.
     *
     * @param view A view instance corresponding to the layout we passed to the constructor.
     * @param chat An instance representing the current state of a chat message
     */
    @Override
    protected void populateView(View view, Chat chat) {
        // Map a Chat object to an entry in our listview
/*        String author = chat.getAuthor();

        TextView authorText = (TextView) view.findViewById(R.id.author);
        TextView message = (TextView) view.findViewById(R.id.message);

        authorText.setText(author + ": ");
        // If the message was sent by this user, color it differently
        if (author != null && author.equals(mUsername)) {
            authorText.setTextColor(Color.RED);
            message.setBackground(mActivity.getResources().getDrawable(R.drawable.bubble_a));

        } else {
            authorText.setTextColor(Color.BLUE);
            message.setBackground(mActivity.getResources().getDrawable(R.drawable.bubble_b));

        }
        message.setText(chat.getMessage());*/
    }
    @Override
    public View getView(int pos, View v, ViewGroup viewGroup) {
        Chat c = (Chat) getItem(pos);
        String timestamp;

        LayoutInflater li = (LayoutInflater) viewGroup.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (c.getAuthor().equals(mUsername))
            v = li.inflate(R.layout.chat_item_sent, null);
        else
            v = li.inflate(R.layout.chat_item_rcv, null);

        TextView lbl = (TextView) v.findViewById(R.id.lbl1);
        lbl.setText(c.getAuthor());
        lbl.setTextAppearance(v.getContext(), R.style.MyChatText);

        /*lbl.setText(DateUtils.getRelativeDateTimeString(v.getContext(), c
                        .getDate().getTime(), DateUtils.SECOND_IN_MILLIS,
                DateUtils.DAY_IN_MILLIS, 0));*/

        lbl = (TextView) v.findViewById(R.id.lbl2);
        lbl.setText(c.getMessage());

        lbl = (TextView) v.findViewById(R.id.lbl3);
        if (c.getAuthor().equals(mUsername))
        {
            lbl.setText("Delivered");

        }
        else
            lbl.setText("");

        return v;
    }
}
