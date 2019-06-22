package com.example.admin.chat_trial;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.quickblox.auth.session.QBSettings;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;

public class MainActivity extends AppCompatActivity {
    static final String APP_ID = "77349";
    static final String AUTH_KEY = "ryjy9VnDcVtNHX9";
    static final String AUTH_SECRET = "Mz6mEF-5GLyLpy4";
    static final String ACCOUNT_KEY = "DxJnMDeVwjjTmK-b-JH9";
    Button btn_signin,btn_signup;
    EditText user,pwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initial();
        btn_signin=(Button)findViewById(R.id.signin);
        btn_signup=(Button)findViewById(R.id.signup);
        user = findViewById(R.id.name);
        pwd = findViewById(R.id.pwd);
        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent nxt = new Intent(MainActivity.this,Signup.class);
                startActivity(nxt);
            }
        });
        btn_signin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                final String user_nme = user.getText().toString();
                final String password = pwd.getText().toString();


                QBUser qbUser = new QBUser(user_nme, password);
                QBUsers.signIn(qbUser).performAsync(new QBEntityCallback<QBUser>() {
                    @Override
                    public void onSuccess(QBUser qbUser, Bundle bundle) {


                        setResult(RESULT_OK);

//                        DataHolder.getInstance().setSignInQbUser(qbUser);
                        Toast.makeText(MainActivity.this,"Login Successfully",Toast.LENGTH_SHORT).show();
                        Intent nxt = new Intent(MainActivity.this,ChatDialogsActivity.class);
                        nxt.putExtra("user",user_nme);
                        nxt.putExtra("pwd",password);
                        startActivity(nxt);
                        finish();//close login after
                    }

                    @Override
                    public void onError(QBResponseException errors) {

                        Toast.makeText(MainActivity.this,errors.getMessage(),Toast.LENGTH_SHORT).show();
                        //View rootLayout = findViewById(R.id.activity_sign_in);
//                        showSnackbarError(rootLayout, R.string.errors, errors, new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                signIn();
//                            }
//                        });
                    }
                });
            }
        });
    }

    private void initial() {
        QBSettings.getInstance().init(getApplicationContext(),APP_ID,AUTH_KEY,AUTH_SECRET);
        QBSettings.getInstance().setAccountKey(ACCOUNT_KEY);
    }


}
