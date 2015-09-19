package com.locastio.akaashvani.screen.fragment;

import android.app.Activity;
import android.content.SharedPreferences;
import android.database.DataSetObserver;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.locastio.akaashvani.AkaashvaniApplication;
import com.locastio.akaashvani.R;
import com.locastio.akaashvani.chat.Chat;
import com.locastio.akaashvani.chat.ChatListAdapter;
import com.locastio.akaashvani.data.Conversation;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ChatFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ChatFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChatFragment extends Fragment {

    private ArrayList<Conversation> convList;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static String strGroupObjId;

    // TODO: Rename and change types of parameters
    private String mParam1;

    private static final String FIREBASE_URL = "https://akaashvaani.firebaseio.com/";
    private Firebase mFirebaseRefChatLocation;

    private String mUsername;
    private Firebase mFirebaseRef;
    private ValueEventListener mConnectedListener;
    private ChatListAdapter mChatListAdapter;

    private OnFragmentInteractionListener mListener;
    private ListView mListView;
    private EditText inputText;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment ChatFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ChatFragment newInstance(String param1, String param2) {
        ChatFragment fragment = new ChatFragment();

        strGroupObjId = param2;
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    public ChatFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_chat, container, false);

        mFirebaseRefChatLocation = new Firebase(FIREBASE_URL).child("chat");

        // Make sure we have a mUsername
        setupUsername();

        getActivity().setTitle("Chatting as " + mUsername);

        // Setup our Firebase mFirebaseRef
//        mFirebaseRef = new Firebase(FIREBASE_URL).child("chat");
        mFirebaseRef = mFirebaseRefChatLocation.child(strGroupObjId);

        // Setup our input methods. Enter key on the keyboard or pushing the send button
        inputText = (EditText) view.findViewById(R.id.messageInput);
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

        view.findViewById(R.id.sendButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage();
            }
        });

        convList = new ArrayList<Conversation>();

      /*  ChatAdapter adp = new ChatAdapter();
        mListView.setAdapter(adp);*/
        mListView = (ListView)view.findViewById(android.R.id.list);
        mListView.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        mListView.setStackFromBottom(true);
//        final ListView listView = (ListView)getActivity().getview.getListView();

        // Inflate the layout for this fragment
        return view;
    }

    /**
     * The Class ChatAdapter is the adapter class for Chat ListView. This
     * adapter shows the Sent or Receieved Chat message in each list item.
     */


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }



    @Override
    public void onStart() {
        super.onStart();
        // Setup our view and list adapter. Ensure it scrolls to the bottom as data changes

        // Tell our list adapter that we only want 50 messages at a time
        mChatListAdapter = new ChatListAdapter(mFirebaseRef.limit(50), getActivity(), R.layout.chat_item_rcv, mUsername);
        mListView.setAdapter(mChatListAdapter);
        mChatListAdapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                mListView.setSelection(mChatListAdapter.getCount() - 1);
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
        SharedPreferences prefs = AkaashvaniApplication.getApplication().getSharedPreferences("ChatPrefs", 0);
        ParseUser currentUser = ParseUser.getCurrentUser();
        mUsername = (String) currentUser.get("fullname");
        if (mUsername == null) {
            Random r = new Random();
            // Assign a random user name if we don't have one saved.
            prefs.edit().putString("username", mUsername).commit();
        }
    }

    private void sendMessage() {

        String input = inputText.getText().toString();
        if (!input.equals("")) {
            // Create our 'model', a Chat object
            Chat chat = new Chat(input, mUsername, new Date().getTime());
            // Create a new, auto-generated child of that chat location, and save our chat data there
            mFirebaseRef.push().setValue(chat);
            inputText.setText("");
        }
    }

}
