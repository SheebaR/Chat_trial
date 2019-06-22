package com.example.admin.chat_trial;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import com.example.admin.chat_trial.Adapter.ChatMessageAdapter;
import com.example.admin.chat_trial.Adapter.NewAdapter;
import com.example.admin.chat_trial.Common.Common;
import com.example.admin.chat_trial.Holder.QBChatMessagesHolder;
import com.quickblox.chat.QBChatService;
import com.quickblox.chat.QBIncomingMessagesManager;
import com.quickblox.chat.QBRestChatService;
import com.quickblox.chat.exception.QBChatException;
import com.quickblox.chat.listeners.QBChatDialogMessageListener;
import com.quickblox.chat.model.QBChatDialog;
import com.quickblox.chat.model.QBChatMessage;
import com.quickblox.chat.model.QBDialogType;
import com.quickblox.chat.request.QBMessageGetBuilder;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;

import org.jivesoftware.smackx.muc.DiscussionHistory;

import java.util.ArrayList;

public class ChatMessageActivity extends AppCompatActivity implements QBChatDialogMessageListener{
QBChatDialog qbChatDialog;
//ListView lstChatMessages;
ImageButton submitbutton;
EditText edtContent;
//ChatMessageAdapter adapter;
  NewAdapter adapter;
    private RecyclerView chatMessagesRecyclerView;
    @Override
    protected void onDestroy() {
        super.onDestroy();
        qbChatDialog.removeMessageListrener(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        qbChatDialog.removeMessageListrener(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_message);
        Log.e("chatmsg","entered here");
    initView();
    initChatDialogs();
    retriveAllMessage();

submitbutton.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        QBChatMessage chatMessage = new QBChatMessage();
        chatMessage.setBody(edtContent.getText().toString());
        chatMessage.setSenderId(QBChatService.getInstance().getUser().getId());
        chatMessage.setSaveToHistory(true);
        try {
            qbChatDialog.sendMessage(chatMessage);

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
       if(qbChatDialog.getType() == QBDialogType.PRIVATE)
       {
           QBChatMessagesHolder.getInstance().putMessage(qbChatDialog.getDialogId(),chatMessage);
           ArrayList<QBChatMessage> messages = QBChatMessagesHolder.getInstance().getChatMessageByDialog(chatMessage.getDialogId());
           Log.e("chat","message added ="+messages);
           //adapter = new ChatMessageAdapter(getBaseContext(),messages);
           adapter = new NewAdapter(getBaseContext(),messages);

           Log.e("chat","chat reset in submit");
           chatMessagesRecyclerView.setAdapter(adapter);


          adapter.notifyDataSetChanged();

       }
        edtContent.setText("");
        edtContent.setFocusable(true);
    }
});

    }

    private void retriveAllMessage() {

    QBMessageGetBuilder messageGetBuilder = new QBMessageGetBuilder();
    messageGetBuilder.setLimit(500);
    if(qbChatDialog != null)
    {
        QBRestChatService.getDialogMessages(qbChatDialog,messageGetBuilder).performAsync(new QBEntityCallback<ArrayList<QBChatMessage>>() {
            @Override
            public void onSuccess(ArrayList<QBChatMessage> qbChatMessages, Bundle bundle) {
                QBChatMessagesHolder.getInstance().putMessages(qbChatDialog.getDialogId(),qbChatMessages);
               Log.e("chatmessages"," retrieve messagwes ="+qbChatMessages);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getBaseContext());
                linearLayoutManager.setStackFromEnd(true);
                chatMessagesRecyclerView.setLayoutManager(linearLayoutManager);
                adapter = new NewAdapter(getBaseContext(),qbChatMessages);
                chatMessagesRecyclerView.setAdapter(adapter);
                Log.e("chat","chat reset in retrieve");

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onError(QBResponseException e) {
Log.e("chatmessage","error="+e);
            }
        });
    }

    }


    private void initChatDialogs() {
        qbChatDialog = (QBChatDialog)getIntent().getSerializableExtra(Common.DIALOG_EXTRA);
        qbChatDialog.initForChat(QBChatService.getInstance());
        QBIncomingMessagesManager incomingMessagesManager = QBChatService.getInstance().getIncomingMessagesManager();
        incomingMessagesManager.addDialogMessageListener(new QBChatDialogMessageListener() {
            @Override
            public void processMessage(String s, QBChatMessage qbChatMessage, Integer integer) {

            }

            @Override
            public void processError(String s, QBChatException e, QBChatMessage qbChatMessage, Integer integer) {

            }
        });

        //Add Join Group to enable group chat
if(qbChatDialog.getType() == QBDialogType.PUBLIC_GROUP || qbChatDialog.getType() == QBDialogType.GROUP)
{
    DiscussionHistory discussionHistory = new DiscussionHistory();
    discussionHistory.setMaxStanzas(0);
    qbChatDialog.join(discussionHistory, new QBEntityCallback() {
        @Override
        public void onSuccess(Object o, Bundle bundle) {

        }

        @Override
        public void onError(QBResponseException e) {

        }
    });
}
        qbChatDialog.addMessageListener(this);

    }

    private void initView()
    {
        chatMessagesRecyclerView =findViewById(R.id.list_of_message);
        submitbutton=(ImageButton)findViewById(R.id.send_button);
        edtContent=(EditText)findViewById(R.id.edt_content);


    }

    @Override
    public void processMessage(String s, QBChatMessage qbChatMessage, Integer integer) {
        QBChatMessagesHolder.getInstance().putMessage(qbChatMessage.getDialogId(),qbChatMessage);
        ArrayList<QBChatMessage> messages = QBChatMessagesHolder.getInstance().getChatMessageByDialog(qbChatMessage.getDialogId());
        Log.e("chat","chat reset in proccessMessage");

        adapter = new NewAdapter(getBaseContext(),messages);
        chatMessagesRecyclerView.setAdapter(adapter);

        adapter.notifyDataSetChanged();
    }

    @Override
    public void processError(String s, QBChatException e, QBChatMessage qbChatMessage, Integer integer) {

    }
}
