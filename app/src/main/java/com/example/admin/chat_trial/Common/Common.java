package com.example.admin.chat_trial.Common;

import com.example.admin.chat_trial.Holder.QBUsersHolder;
import com.quickblox.users.model.QBUser;

import java.util.List;

/**
 * Created by user on 13-06-2019.
 */

public class Common {
    public static final String DIALOG_EXTRA = "Dialogs";
    public static String createChatDialogName(List<Integer> qbUsers)
    {
       List<QBUser> qbUsers1 = QBUsersHolder.getInstance().getUsersById(qbUsers);
       StringBuilder name = new StringBuilder();
       for(QBUser user:qbUsers1)
           name.append(user.getFullName()).append(" ");
       if(name.length()>30)
           name = name.replace(30,name.length()-1,"...");
       return name.toString();
    }

}
