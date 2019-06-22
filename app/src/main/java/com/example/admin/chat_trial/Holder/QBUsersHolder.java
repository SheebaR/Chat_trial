package com.example.admin.chat_trial.Holder;

import android.util.SparseArray;

import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 13-06-2019.
 */

public class QBUsersHolder {
    private static QBUsersHolder instance;
    private SparseArray<QBUser> qbUserSparseArray;
    public static synchronized QBUsersHolder getInstance()
    {
        if(instance == null)
            instance = new QBUsersHolder();
            return instance;
    }
    private QBUsersHolder()
    {
        qbUserSparseArray = new SparseArray<>();
    }
    public void putusers(List<QBUser> users)
    {
        for (QBUser user:users)
            putuser(user);


    }

    private void putuser(QBUser user) {
    qbUserSparseArray.put(user.getId(),user);
    }

    public QBUser getUserById(int id)
    {
        return qbUserSparseArray.get(id);
    }
    public List<QBUser> getUsersById(List<Integer> ids)
    {
        List<QBUser> qbUser = new ArrayList<>();
        for(Integer id:ids)
        {
            QBUser user = getUserById(id);
            if(user!= null)
                qbUser.add(user);
        }
return qbUser;
    }



}
