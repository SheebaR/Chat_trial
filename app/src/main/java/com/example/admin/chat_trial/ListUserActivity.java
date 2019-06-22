package com.example.admin.chat_trial;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.admin.chat_trial.Adapter.ListUserAdapter;
import com.example.admin.chat_trial.Common.Common;
import com.quickblox.chat.QBChatService;
import com.quickblox.chat.QBRestChatService;
import com.quickblox.chat.QBSystemMessagesManager;
import com.quickblox.chat.model.QBChatDialog;
import com.quickblox.chat.model.QBChatMessage;
import com.quickblox.chat.model.QBDialogType;
import com.quickblox.chat.utils.DialogUtils;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.core.helper.StringifyArrayList;
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;

import java.util.ArrayList;

public class ListUserActivity extends AppCompatActivity {

    ListView lstusers;
    Button btnCreateChat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_user);
        lstusers=findViewById(R.id.lstUsers_list);
        btnCreateChat=findViewById(R.id.create);
        retrieveallusers();
lstusers.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
btnCreateChat.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        int countchoice = lstusers.getCount();
        if(lstusers.getCheckedItemPositions().size()==1)
        {
            createPrivatechat(lstusers.getCheckedItemPositions());
        }
        else if(lstusers.getCheckedItemPositions().size()>1)
        {
            creategroupchat(lstusers.getCheckedItemPositions());
        }
        else
        {
            Toast.makeText(ListUserActivity.this,"Please select friend", Toast.LENGTH_SHORT).show();
        }
    }
});
    }

    private void creategroupchat(SparseBooleanArray checkedItemPositions) {
        final ProgressDialog mdd = new ProgressDialog(ListUserActivity.this);
mdd.setMessage("Please wait...");
mdd.setCanceledOnTouchOutside(false);
mdd.show();
int countchoice = lstusers.getCount();
ArrayList<Integer> occupantIdList = new ArrayList<>();
Log.e("Listuser","count=="+countchoice);
for(int i =0;i<countchoice;i++)
{
    if(checkedItemPositions.get(i))
    {
        QBUser user = (QBUser)lstusers.getItemAtPosition(i);

        occupantIdList.add(user.getId());

    }

}

        QBChatDialog dialog = new QBChatDialog();
        dialog.setName(Common.createChatDialogName(occupantIdList));
        dialog.setType(QBDialogType.GROUP);
        dialog.setOccupantsIds(occupantIdList);
        Log.e("ListActivity","entered function");
        QBRestChatService.createChatDialog(dialog).performAsync(new QBEntityCallback<QBChatDialog>() {
            @Override
            public void onSuccess(QBChatDialog qbChatDialog, Bundle bundle) {
                mdd.dismiss();
                Toast.makeText(ListUserActivity.this,"Create group chat dialog successfully",Toast.LENGTH_SHORT).show();
                QBSystemMessagesManager qbSystemMessagesManager = QBChatService.getInstance().getSystemMessagesManager();
                QBChatMessage qbChatMessage = new QBChatMessage();
                qbChatMessage.setBody(qbChatDialog.getDialogId());
                for(int i =0;i<qbChatDialog.getOccupants().size();i++)
                {
                    qbChatMessage.setRecipientId(qbChatDialog.getOccupants().get(i));
                    try
                    {
                        qbSystemMessagesManager.sendSystemMessage(qbChatMessage);
                    }
                    catch (Exception e)
                    {
                        Log.e("Listuser","error"+e);
                    }
                }



                finish();
            }

            @Override
            public void onError(QBResponseException e) {
Log.e("Listuser",e.getMessage());
            }
        });
    }

    private void createPrivatechat(SparseBooleanArray checkedItemPositions) {
        final ProgressDialog mdd1 = new ProgressDialog(ListUserActivity.this);
        mdd1.setMessage("Please wait...");
        mdd1.setCanceledOnTouchOutside(false);
        mdd1.show();
        int countchoice = lstusers.getCount();
        for(int i =0;i<countchoice;i++)
        {
            if(checkedItemPositions.get(i))
            {
                final QBUser user = (QBUser)lstusers.getItemAtPosition(i);
                QBChatDialog dialog = DialogUtils.buildPrivateDialog(user.getId());
                Log.e("ListActivity","entered function");
                QBRestChatService.createChatDialog(dialog).performAsync(new QBEntityCallback<QBChatDialog>() {
                    @Override
                    public void onSuccess(QBChatDialog qbChatDialog, Bundle bundle) {
                        mdd1.dismiss();
                        Toast.makeText(ListUserActivity.this,"Create chat dialog successfully",Toast.LENGTH_SHORT).show();
                        //send system messages to recepient Id User
                        QBSystemMessagesManager qbSystemMessagesManager = QBChatService.getInstance().getSystemMessagesManager();
                        QBChatMessage qbChatMessage = new QBChatMessage();
                        qbChatMessage.setRecipientId(user.getId());
                        qbChatMessage.setBody(qbChatDialog.getDialogId());
                        try
                        {
                            qbSystemMessagesManager.sendSystemMessage(qbChatMessage);
                        }
                        catch (Exception e)
                        {
                        Log.e("Listuser","error"+e);
                        }


                        finish();
                    }

                    @Override
                    public void onError(QBResponseException e) {

                    }
                });
            }
        }

    }

    private void retrieveallusers() {

        QBUsers.getUsers(null).performAsync(new QBEntityCallback<ArrayList<QBUser>>() {
            @Override
            public void onSuccess(ArrayList<QBUser> qbUsers, Bundle bundle) {
                ArrayList<QBUser> qbUserWithoutCurrent = new ArrayList<QBUser>();
                Log.e("Listuser","the list of users="+qbUsers);

                for(QBUser user: qbUsers)
                {

Log.e("Listuser","id="+QBChatService.getInstance().getUser().getLogin());
                    if(!user.getLogin().equals(QBChatService.getInstance().getUser().getLogin()))

                    {
                        // qbUserWithoutCurrent.add(user);
                        Log.e("Listuser", "login ===" + QBChatService.getInstance().getUser().getLogin());
                        Log.e("Listuser", "tag ===" + user.getTags());
                        if (user.getTags().size() > 0) {
                            Log.e("Listuser", "tag =" + user.getTags().get(0));
                            String user_tag = user.getTags().get(0);
                            Log.e("Listuser", "tag =" + user_tag);
                            //  if (user_tag.equals("Doctor")) {
                            qbUserWithoutCurrent.add(user);
                            //}
                        }
                    }
                }

                ListUserAdapter adapter = new ListUserAdapter(getBaseContext(),qbUserWithoutCurrent);
                lstusers.setAdapter(adapter);
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onError(QBResponseException e) {

            }
        });









    }
}
