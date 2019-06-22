package com.example.admin.chat_trial.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.admin.chat_trial.Holder.QBUsersHolder;
import com.example.admin.chat_trial.R;
import com.github.library.bubbleview.BubbleTextView;
import com.quickblox.chat.QBChatService;
import com.quickblox.chat.model.QBChatMessage;
import com.quickblox.users.QBUsers;

import java.util.ArrayList;

/**
 * Created by user on 14-06-2019.
 */

public class ChatMessageAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<QBChatMessage> qbChatMessages;

    public ChatMessageAdapter(Context context, ArrayList<QBChatMessage> qbChatMessages) {
        this.context = context;
        Log.e("chatmessages","the messages in adapter are"+qbChatMessages);
        this.qbChatMessages = qbChatMessages;
    }

    @Override
    public int getCount() {
        return qbChatMessages.size();
    }

    @Override
    public Object getItem(int position) {
        return qbChatMessages.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if(convertView == null)
        {
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            Log.e("the message ","chatmessage position="+qbChatMessages.get(position).getSenderId());
            Log.e("the message ","chatmessage position="+QBChatService.getInstance().getUser().getId());

            if(qbChatMessages.get(position).getSenderId().equals(QBChatService.getInstance().getUser().getId()))
            {
                view = inflater.inflate(R.layout.list_send_message,null);
                BubbleTextView bubbleTextView = (BubbleTextView)view.findViewById(R.id.message_content);
                bubbleTextView.setText(qbChatMessages.get(position).getBody());
            }
            else
            {

                view = inflater.inflate(R.layout.list_recv_message  ,null);
                BubbleTextView bubbleTextView = (BubbleTextView)view.findViewById(R.id.message_content);
                bubbleTextView.setText(qbChatMessages.get(position).getBody());
                TextView txtName = (TextView)view.findViewById(R.id.message_user);
                txtName.setText(QBUsersHolder.getInstance().getUserById(qbChatMessages.get(position).getSenderId()).getFullName());
            }
        }
        return view;
    }
}
