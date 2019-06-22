package com.example.admin.chat_trial.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.admin.chat_trial.Holder.QBUsersHolder;
import com.example.admin.chat_trial.R;
import com.github.library.bubbleview.BubbleTextView;
import com.quickblox.chat.QBChatService;
import com.quickblox.chat.model.QBChatMessage;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersAdapter;

import java.util.ArrayList;

/**
 * Created by user on 20-06-2019.
 */

public class NewAdapter extends RecyclerView.Adapter {
    private Context context;
    private ArrayList<QBChatMessage> qbChatMessages;
    public NewAdapter(Context context, ArrayList<QBChatMessage> qbChatMessages) {
        this.context = context;
        Log.e("chatmessages","the messages in adapter are"+qbChatMessages);
        this.qbChatMessages = qbChatMessages;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.list_send_message, null);

        // View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.approved_appointments, parent, false);
        // set the view's size, margins, paddings and layout parameters
        NewAdapter.MyViewHolder vh = new NewAdapter.MyViewHolder(view); // pass the view to View Holder

        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        final NewAdapter.MyViewHolder myHolder = (MyViewHolder) viewHolder;
//        myHolder.content_rec.setText(qbChatMessages.get(i).getBody());
        Log.e("NewAdpater"," qbchatmessageid="+qbChatMessages.get(i).getSenderId());
        Log.e("NewAdpater","QBchatmessageid="+QBChatService.getInstance().getUser().getId());
        int user_id = qbChatMessages.get(i).getSenderId();
        int other_id = QBChatService.getInstance().getUser().getId();

            myHolder.content_send.setText(qbChatMessages.get(i).getBody());



        if(qbChatMessages.get(i).getSenderId().equals(QBChatService.getInstance().getUser().getId())) {
            Log.e("Newadpter","entered sendmessage");
           myHolder.content_send.setVisibility(View.VISIBLE);
            myHolder.content_send.setText(qbChatMessages.get(i).getBody());
            myHolder.content_rec.setVisibility(View.INVISIBLE);
            myHolder.name.setVisibility(View.INVISIBLE);
        }
        else
        {
            myHolder.content_rec.setVisibility(View.VISIBLE);
            myHolder.name.setVisibility(View.VISIBLE);
            Log.e("Newadpter","entered receivemessage");
            myHolder.content_rec.setText(qbChatMessages.get(i).getBody());
            myHolder.name.setText(QBUsersHolder.getInstance().getUserById(qbChatMessages.get(i).getSenderId()).getFullName());
            myHolder.content_send.setVisibility(View.INVISIBLE);

        }






    }

    @Override
    public int getItemCount() {
        return qbChatMessages.size();
    }
    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        BubbleTextView content_rec,content_send;
        RelativeLayout rec_rel,send_rel;
        public MyViewHolder(View itemView) {
            super(itemView);
          content_rec = itemView.findViewById(R.id.message_content_receive);
            content_send = itemView.findViewById(R.id.message_content);
            rec_rel=itemView.findViewById(R.id.rec);
            send_rel=itemView.findViewById(R.id.rel_send);
            name = itemView.findViewById(R.id.message_user);
        }
    }
}
