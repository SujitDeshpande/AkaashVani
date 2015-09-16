package com.locastio.akaashvani.chat;

import android.app.ListActivity;
import android.content.SharedPreferences;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.locastio.akaashvani.R;
import com.locastio.akaashvani.data.Conversation;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by ketan on 30/08/15.
 */
public class ChatActivity extends ListActivity {

    private ArrayList<Conversation> convList;

    // TODO: change this to your own Firebase URL
//    private static final String FIREBASE_URL = "https://android-chat.firebaseio-demo.com";
    private static final String FIREBASE_URL = "https://akaashvani.firebaseio.com/";
    private Firebase mFirebaseRefChatLocation;

    private String mUsername;
    private Firebase mFirebaseRef;
    private ValueEventListener mConnectedListener;
    private ChatListAdapter mChatListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        mFirebaseRefChatLocation = new Firebase(FIREBASE_URL).child("chat");

        String strGroupObjId = getIntent().getStringExtra("groupObjId");

        // Make sure we have a mUsername
        setupUsername();

        setTitle("Chatting as " + mUsername);

/*        convList = new ArrayList<Conversation>();
        ListView list = (ListView) findViewById(R.id.list);
        adp = new ChatAdapter();
        list.setAdapter(adp);
        list.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        list.setStackFromBottom(true);*/

        // Setup our Firebase mFirebaseRef
//        mFirebaseRef = new Firebase(FIREBASE_URL).child("chat");
        mFirebaseRef = mFirebaseRefChatLocation.child(strGroupObjId);

        // Setup our input methods. Enter key on the keyboard or pushing the send button
        EditText inputText = (EditText) findViewById(R.id.messageInput);
        inputText.setInputType(InputType.TYPE_CLASS_TEXT
                | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        inputText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_NULL && keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                    sendMessage();
                }
                return true;
            }
        });

        findViewById(R.id.sendButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage();
            }
        });

    }

    /**
     * The Class ChatAdapter is the adapter class for Chat ListView. This
     * adapter shows the Sent or Receieved Chat message in each list item.
     */
//    private class ChatAdapter extends BaseAdapter {
//
//        /* (non-Javadoc)
//         * @see android.widget.Adapter#getCount()
//         */
//        @Override
//        public int getCount() {
//            return convList.size();
//        }
//
//        /* (non-Javadoc)
//         * @see android.widget.Adapter#getItem(int)
//         */
//        @Override
//        public Conversation getItem(int arg0) {
//            return convList.get(arg0);
//        }
//
//        /* (non-Javadoc)
//         * @see android.widget.Adapter#getItemId(int)
//         */
//        @Override
//        public long getItemId(int arg0) {
//            return arg0;
//        }
//
//        /* (non-Javadoc)
//         * @see android.widget.Adapter#getView(int, android.view.View, android.view.ViewGroup)
//         */
//        @Override
//        public View getView(int pos, View v, ViewGroup arg2) {
//            Conversation c = getItem(pos);
//            if (c.isSent())
//                v = getLayoutInflater().inflate(R.layout.chat_item_sent, null);
//            else
//                v = getLayoutInflater().inflate(R.layout.chat_item_rcv, null);
//
//            TextView lbl = (TextView) v.findViewById(R.id.lbl1);
//            lbl.setText(DateUtils.getRelativeDateTimeString(ChatActivity.this, c
//                            .getDate().getTime(), DateUtils.SECOND_IN_MILLIS,
//                    DateUtils.DAY_IN_MILLIS, 0));
//
//            lbl = (TextView) v.findViewById(R.id.lbl2);
//            lbl.setText(c.getMsg());
//
//            lbl = (TextView) v.findViewById(R.id.lbl3);
//            if (c.isSent()) {
//                if (c.getStatus() == Conversation.STATUS_SENT)
//                    lbl.setText("Delivered");
//                else if (c.getStatus() == Conversation.STATUS_SENDING)
//                    lbl.setText("Sending...");
//                else
//                    lbl.setText("Failed");
//            } else
//                lbl.setText("");
//
//            return v;
//        }
//
//    }
    @Override
    public void onStart() {
        super.onStart();
        // Setup our view and list adapter. Ensure it scrolls to the bottom as data changes
        final ListView listView = getListView();
        // Tell our list adapter that we only want 50 messages at a time
        mChatListAdapter = new ChatListAdapter(mFirebaseRef.limit(50), this, R.layout.chat_message, mUsername);
        listView.setAdapter(mChatListAdapter);
        mChatListAdapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                listView.setSelection(mChatListAdapter.getCount() - 1);
            }
        });

        // Finally, a little indication of connection status
        mConnectedListener = mFirebaseRef.getRoot().child(".info/connected").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean connected = (Boolean) dataSnapshot.getValue();
//                if (connected) {
//                    Toast.makeText(ChatActivity.this, "Connected to Firebase", Toast.LENGTH_SHORT).show();
//                } else {
//                    Toast.makeText(ChatActivity.this, "Disconnected from Firebase", Toast.LENGTH_SHORT).show();
//                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                // No-op
            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        mFirebaseRef.getRoot().child(".info/connected").removeEventListener(mConnectedListener);
        mChatListAdapter.cleanup();
    }

    private void setupUsername() {
        SharedPreferences prefs = getApplication().getSharedPreferences("ChatPrefs", 0);
        ParseUser currentUser = ParseUser.getCurrentUser();
        mUsername = (String) currentUser.get("fullname");
        if (mUsername == null) {
            Random r = new Random();
            // Assign a random user name if we don't have one saved.
            prefs.edit().putString("username", mUsername).commit();
        }
    }

    private void sendMessage() {
        EditText inputText = (EditText) findViewById(R.id.messageInput);
        String input = inputText.getText().toString();
        if (!input.equals("")) {
            // Create our 'model', a Chat object
            Chat chat = new Chat(input, mUsername);
            // Create a new, auto-generated child of that chat location, and save our chat data there
            mFirebaseRef.push().setValue(chat);
            inputText.setText("");
        }
    }
}
