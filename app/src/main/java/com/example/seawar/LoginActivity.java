package com.example.seawar;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.*;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class LoginActivity extends AppCompatActivity implements IRefreshable{
    TextView errorLabel;
    EditText loginField;
    EditText passwordField;
    Button loginButton;
    Button registerButton;
    IController controller;
    IModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        controller = MyController.getInstance();
        model = MyModel.getInstance();
        controller.setCurrentActivity(this);

        errorLabel = (TextView) findViewById(R.id.errorLabel);
        loginField = (EditText) findViewById(R.id.loginField);
        passwordField = (EditText) findViewById(R.id.passwordField);
        passwordField.setText("1234");
        loginButton = (Button) findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (loginField.getText().length() < 3){
                    errorLabel.setText("Логин не может быть меньше 3 символов");
                }
                else if (passwordField.getText().length() < 1){
                    errorLabel.setText("Пароль не может быть пустым");
                }
                else{
                    controller.buttonLoginHandler(loginField.getText().toString(), passwordField.getText().toString());
                }
            }
        });
        registerButton = (Button) findViewById(R.id.registerButton);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                controller.buttonRegistrationHandler();
            }
        });
        //backButton = (Button) findViewById(R.id.backButton1);

    }

    @Override
    public void refresh() {
        if(model.getConnectionState() == IModel.ConnectionState.isAuthorizedOnTheServer){
            loginField.setText(model.getLogin());
            errorLabel.setText("");
        }else if (model.getConnectionState() == IModel.ConnectionState.cantLogin)
        {
            errorLabel.setText("Пользователь не найден");
        }
    }

    public android.os.Handler handler = new android.os.Handler(new android.os.Handler.Callback() {
        @Override
        public boolean handleMessage(android.os.Message msg) {

            if (msg.what == IModel.ModelState.startFrame.ordinal()) {
                Activity tmp = new MainActivity();
                Intent intent = new Intent(LoginActivity.this, tmp.getClass());
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            }
            else if(msg.what == IModel.ModelState.mainMenuFrame.ordinal()){
                Activity tmp = new MainMenuActivity();
                Intent intent = new Intent(LoginActivity.this, tmp.getClass());
                startActivity(intent);
            }
            else if (msg.what == IModel.ModelState.registrationFrame.ordinal()){
                Activity tmp = new RegistrationActivity();
                Intent intent = new Intent(LoginActivity.this, tmp.getClass());
                startActivity(intent);
            }
            else if(msg.what == IModel.ModelState.onlyRefresh.ordinal()){
                refresh();
            }
            return false;
        }
    });

    @Override
    public android.os.Handler getHandler() {
        return handler;
    }

    @Override
    public IModel.ModelState getRelevantState() {
        return IModel.ModelState.loginFrame;
    }

    @Override
    protected void onResume(){
        super.onResume();

        controller.resumeActivity(this);
        refresh();
    }

    @Override
    protected void onStart(){
        super.onStart();

        refresh();
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        System.err.println("LoginActivity.onDestroy()--------------------------------disconnect!!!!!!!!!!!!!!!!!!!!!");
        Log.i("Message","LoginActivity.onDestroy()");
        if (model.getConnectionState() != IModel.ConnectionState.offline && model.getConnectionState() != IModel.ConnectionState.cantConnectToServer)
            controller.disconnect();
    }
}
