package com.example.admin.chat_trial.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.example.admin.chat_trial.R;
import com.quickblox.chat.model.QBChatDialog;

import java.util.ArrayList;

public class ChatDialogsAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<QBChatDialog> qbChatDialogs;

    public ChatDialogsAdapter(Context context, ArrayList<QBChatDialog> qbChatDialogs) {
        this.context = context;
        this.qbChatDialogs = qbChatDialogs;
    }

    @Override
    public int getCount() {
        return qbChatDialogs.size();
    }

    @Override
    public Object getItem(int i) {
        return qbChatDialogs.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View view1 = view;
        if (view1 == null)
        {
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view1 = inflater.inflate(R.layout.list_chat_dialog,null);
            TextView title,msg;
            ImageView imageView;
            title = view1.findViewById(R.id.list_chat_dialog_title);
            msg=view1.findViewById(R.id.list_chat_dialog_msg);
            imageView=view1.findViewById(R.id.image_chatdialog);
            msg.setText(qbChatDialogs.get(i).getLastMessage());
            title.setText(qbChatDialogs.get(i).getName());
            ColorGenerator generator = ColorGenerator.MATERIAL;
            int randomColor = generator.getRandomColor();
            TextDrawable.IBuilder builder = TextDrawable.builder().beginConfig().withBorder(4).endConfig().round();
            TextDrawable drawable = builder.build(title.getText().toString().substring(0,1).toUpperCase(),randomColor);

            imageView.setImageDrawable(drawable);
        }
        return  view1;
    }
}
