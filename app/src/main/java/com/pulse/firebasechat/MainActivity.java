package com.pulse.firebasechat;

import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,
        MessageDataSource.MessagesCallbacks{

    public static final String USER_EXTRA = "USER";

    public static final String TAG = "ChatActivity";

    private ArrayList<Message> mMessages;
    private MessagesAdapter mAdapter;
    private String mRecipient,mSender;
    private ListView mListView;
    private Date mLastMessageDate = new Date();
    private MessageDataSource.MessagesListener mListener;
    private String chatType;
    private SharedPreferences userPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        userPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mSender = userPreferences.getString("userName","User");
        mRecipient = "recipient";
        mListView = (ListView)findViewById(com.pulse.firebasechat.R.id.messages_list);
        mMessages = new ArrayList<>();
        mAdapter = new MessagesAdapter(mMessages);
        mListView.setAdapter(mAdapter);

        setTitle(mRecipient);
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        Button sendMessage = (Button)findViewById(R.id.send_message);
        sendMessage.setOnClickListener(this);
        chatType = "Support Chat";
        mListener = MessageDataSource.addMessagesListener(chatType, this);

    }

    public void onClick(View v) {
        EditText newMessageView = (EditText)findViewById(R.id.new_message);
        String newMessage = newMessageView.getText().toString();
        newMessageView.setText("");
        Message msg = new Message();
        msg.setDate(new Date());
        msg.setText(newMessage);
        msg.setSender(mSender);

        MessageDataSource.saveMessage(msg, chatType, mSender);
    }

    @Override
    public void onMessageAdded(Message message) {
        mMessages.add(message);
        runOnUiThread(new Runnable() {
            public void run() {
                mAdapter.notifyDataSetChanged();
                scrollMyListViewToBottom();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MessageDataSource.stop(mListener);
    }
    private void scrollMyListViewToBottom() {
        mListView.post(new Runnable() {
            @Override
            public void run() {
                // Select the last row so it will scroll into view...
                mListView.setSelection(mAdapter.getCount() - 1);
            }
        });
    }

    private class MessagesAdapter extends ArrayAdapter<Message> {
        MessagesAdapter(ArrayList<Message> messages){
            super(MainActivity.this, R.layout.message, R.id.message, messages);
        }
        @NonNull
        @Override
        public View getView(int position, View convertView, @NonNull ViewGroup parent) {
            convertView = super.getView(position, convertView, parent);
            Message message = getItem(position);

            TextView nameView = (TextView)convertView.findViewById(R.id.message);
            assert message != null;
            nameView.setText(message.getText());

            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams)nameView.getLayoutParams();

            int sdk = Build.VERSION.SDK_INT;
            if (message.getSender().equals(mSender)){
                if (sdk >= Build.VERSION_CODES.JELLY_BEAN) {
                    nameView.setBackground(getDrawable(R.drawable.bubble_right_green));
                } else{
                    nameView.setBackgroundDrawable(getDrawable(R.drawable.bubble_right_green));
                }
                layoutParams.gravity = Gravity.RIGHT;
            }else{
                if (sdk >= Build.VERSION_CODES.JELLY_BEAN) {
                    nameView.setBackground(getDrawable(R.drawable.bubble_left_gray));
                } else{
                    nameView.setBackgroundDrawable(getDrawable(R.drawable.bubble_left_gray));
                }
                layoutParams.gravity = Gravity.LEFT;
            }

            nameView.setLayoutParams(layoutParams);


            return convertView;
        }
    }
}
