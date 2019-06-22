package com.example.admin.chat_trial;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.quickblox.auth.QBAuth;
import com.quickblox.auth.session.QBSession;
import com.quickblox.auth.session.QBSettings;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.core.helper.StringifyArrayList;
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;

public class Signup extends AppCompatActivity {
    static final String APP_ID = "77349";
    static final String AUTH_KEY = "ryjy9VnDcVtNHX9";
    static final String AUTH_SECRET = "Mz6mEF-5GLyLpy4";
    static final String ACCOUNT_KEY = "DxJnMDeVwjjTmK-b-JH9";
Button bt_signup,bt_signin;
EditText usr,pd,f_name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
register();
        usr=findViewById(R.id.signup_name);
        pd=findViewById(R.id.signup_pwd);
        f_name=findViewById(R.id.signup_fulname);
        bt_signin=findViewById(R.id.signin_signin);
        bt_signup=findViewById(R.id.signup_signup);
        bt_signup.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
String username=usr.getText().toString();
String pswd=pd.getText().toString();
String full_name=f_name.getText().toString();
String tag="Doctor";
                StringifyArrayList<String> tagsArray = new StringifyArrayList<>();
                tagsArray.add(tag);
                QBUser qbUser = new QBUser();
                qbUser.setLogin(username);
                qbUser.setPassword(pswd);
                qbUser.setFullName(full_name);
                qbUser.setTags(tagsArray);
                QBUsers.signUpSignInTask(qbUser).performAsync(new QBEntityCallback<QBUser>() {
                    @Override
                    public void onSuccess(QBUser qbUser, Bundle bundle) {

                        Toast.makeText(Signup.this,"User Regisered",Toast.LENGTH_SHORT).show();
                        Intent nxt = new Intent(Signup.this,MainActivity.class);
                        startActivity(nxt);
//                        DataHolder.getInstance().addQbUser(qbUser);
//                        DataHolder.getInstance().setSignInQbUser(qbUser);

                        setResult(RESULT_OK, new Intent());
                        finish();
                    }

                    @Override
                    public void onError(QBResponseException error) {

                        Toast.makeText(Signup.this,error.getMessage(),Toast.LENGTH_SHORT).show();

//                        View rootLayout = findViewById(R.id.activity_sign_up);
//                        showSnackbarError(rootLayout, R.string.errors, error, new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                signUp();
//                            }
//                        });
                    }
                });
            }

        });
        bt_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent nxt = new Intent(Signup.this,MainActivity.class);
                startActivity(nxt);
            }
        });

    }

    private void register() {
        QBAuth.createSession().performAsync(new QBEntityCallback<QBSession>() {
            @Override
            public void onSuccess(QBSession qbSession, Bundle bundle) {

            }

            @Override
            public void onError(QBResponseException e) {

            }
        });
    }






}
