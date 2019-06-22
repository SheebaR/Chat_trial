package com.example.admin.chat_trial;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.admin.chat_trial.Adapter.ChatDialogsAdapter;
import com.example.admin.chat_trial.Common.Common;
import com.example.admin.chat_trial.Holder.QBChatDialogHolder;
import com.example.admin.chat_trial.Holder.QBUsersHolder;
import com.quickblox.auth.QBAuth;
import com.quickblox.auth.session.BaseService;
import com.quickblox.auth.session.QBSession;
import com.quickblox.auth.session.QBSettings;
import com.quickblox.chat.QBChatService;
import com.quickblox.chat.QBRestChatService;
import com.quickblox.chat.QBSystemMessagesManager;
import com.quickblox.chat.exception.QBChatException;
import com.quickblox.chat.listeners.QBSystemMessageListener;
import com.quickblox.chat.model.QBChatDialog;
import com.quickblox.chat.model.QBChatMessage;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.BaseServiceException;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.core.request.QBRequestGetBuilder;
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;

import java.util.ArrayList;

import static com.example.admin.chat_trial.Signup.ACCOUNT_KEY;
import static com.example.admin.chat_trial.Signup.APP_ID;
import static com.example.admin.chat_trial.Signup.AUTH_KEY;
import static com.example.admin.chat_trial.Signup.AUTH_SECRET;

public class ChatDialogsActivity extends AppCompatActivity implements QBSystemMessageListener {
FloatingActionButton floatingActionButton;
ListView lstchat;


    @Override
    protected void onResume() {
        super.onResume();
        loadChatDialogs();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_dialogs);
        createSessionForChat();
        initial();
        loadChatDialogs();

        floatingActionButton=findViewById(R.id.chatdialog_adduser);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent n = new Intent(ChatDialogsActivity.this,ListUserActivity.class);
                startActivity(n);

            }
        });
lstchat= findViewById(R.id.lstChatDialogs);
        lstchat.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                QBChatDialog qbChatDialog =(QBChatDialog)lstchat.getAdapter().getItem(position);
                Intent intent = new Intent(ChatDialogsActivity.this,ChatMessageActivity.class);
                intent.putExtra(Common.DIALOG_EXTRA,qbChatDialog);
                startActivity(intent);
            }
        });
    }

    private void loadChatDialogs() {
        QBRequestGetBuilder requestGetBuilder = new QBRequestGetBuilder();
        requestGetBuilder.setLimit(100);
        QBRestChatService.getChatDialogs(null,requestGetBuilder).performAsync(new QBEntityCallback<ArrayList<QBChatDialog>>() {
            @Override
            public void onSuccess(ArrayList<QBChatDialog> qbChatDialogs, Bundle bundle) {
                QBChatDialogHolder.getInstance().putDialogs(qbChatDialogs);
                Log.e("chatdialogactivity","after login loaded =");
                ChatDialogsAdapter adapter = new ChatDialogsAdapter(getBaseContext(),qbChatDialogs);
                lstchat.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onError(QBResponseException e) {
Log.e("chatdialogactivity","after login errror ="+e);
            }
        });
    }

    private void createSessionForChat() {
        final ProgressDialog md= new ProgressDialog(ChatDialogsActivity.this);
        md.setMessage("Please wait..");
        md.setCanceledOnTouchOutside(false);
        md.show();
        String user,pswd;
        user = getIntent().getStringExtra("user");
        pswd = getIntent().getStringExtra("pwd");

        QBUsers.getUsers(null).performAsync(new QBEntityCallback<ArrayList<QBUser>>() {
            @Override
            public void onSuccess(ArrayList<QBUser> qbUsers, Bundle bundle) {
                QBUsersHolder.getInstance().putusers(qbUsers);
                Log.e("chatdialogactivity","after login session successs =");
            }

            @Override
            public void onError(QBResponseException e) {
                Log.e("chatdialogactivity","after login session created ="+e);
            }
        });


final QBUser qbUser = new QBUser(user,pswd);
        QBAuth.createSession(qbUser).performAsync(new QBEntityCallback<QBSession>() {
            @Override
            public void onSuccess(QBSession qbSession, Bundle bundle) {
                qbUser.setId(qbSession.getUserId());
                try {
                    qbUser.setPassword(BaseService.getBaseService().getToken());
                } catch (BaseServiceException e) {
                    e.printStackTrace();
                }
                QBChatService.getInstance().login(qbUser, new QBEntityCallback() {
                    @Override
                    public void onSuccess(Object o, Bundle bundle) {
                        md.dismiss();
                        QBSystemMessagesManager qbSystemMessagesManager =QBChatService.getInstance().getSystemMessagesManager();
                        qbSystemMessagesManager.addSystemMessageListener(ChatDialogsActivity.this);
                    }

                    @Override
                    public void onError(QBResponseException e) {

                    }
                });
            }

            @Override
            public void onError(QBResponseException e) {

            }
        });
    }
    private void initial() {
        QBSettings.getInstance().init(getApplicationContext(),APP_ID,AUTH_KEY,AUTH_SECRET);
        QBSettings.getInstance().setAccountKey(ACCOUNT_KEY);
    }

    @Override
    public void processMessage(QBChatMessage qbChatMessage) {

        QBRestChatService.getChatDialogById(qbChatMessage.getBody()).performAsync(new QBEntityCallback<QBChatDialog>() {
            @Override
            public void onSuccess(QBChatDialog qbChatDialog, Bundle bundle) {
                QBChatDialogHolder.getInstance().putDialog(qbChatDialog);
                ArrayList<QBChatDialog> adapterSource = QBChatDialogHolder.getInstance().getAlChatDialogs();
                ChatDialogsAdapter adapter = new ChatDialogsAdapter(getBaseContext(),adapterSource);
                lstchat.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onError(QBResponseException e) {

            }
        });

    }

    @Override
    public void processError(QBChatException e, QBChatMessage qbChatMessage) {
Log.e("chat","error ="+e);
    }


}
