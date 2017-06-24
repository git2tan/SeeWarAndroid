package com.example.seawar;

import android.app.Activity;
import android.content.Intent;
import android.os.*;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

public class MainActivity extends AppCompatActivity implements IRefreshable {

    TextView connectStateLabel;
    Button connectButton;
    Button setDefaultIPButton;
    EditText connectIPField;
    EditText connectPortField;
    MyController controller;
    IModel model;
    boolean isFirstClick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        controller = MyController.getInstance();
        model = MyModel.getInstance();
        controller.setCurrentActivity(this);
        isFirstClick = true;
        connectStateLabel = (TextView) findViewById(R.id.connectStateLabel);
        connectButton = (Button) findViewById(R.id.connectButton);
        setDefaultIPButton = (Button) findViewById(R.id.setDefaultIPButton);
        connectIPField = (EditText) findViewById(R.id.connectIPField);
        connectIPField.setText(model.getDefaultIP());

        connectPortField = (EditText) findViewById(R.id.portField);
        connectPortField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isFirstClick)
                    connectPortField.setText("");
                isFirstClick = false;
            }
        });

        connectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                controller.buttonConnectHandler(String.valueOf(connectIPField.getText()), Integer.parseInt(String.valueOf(connectPortField.getText())));
            }
        });

        setDefaultIPButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                connectIPField.setText(model.getDefaultIP());
            }
        });

        switch(model.getConnectionState()){
            case offline:{
                connectStateLabel.setText("Отключен от сервера");
            } break;
            case tryToConnect:{
                connectStateLabel.setText("Пытаюсь подключиться");
            } break;
            case cantConnectToServer:{
                connectStateLabel.setText("Сервер не отвечает");
            } break;
            case online:{
                connectStateLabel.setText("Подключен");
            } break;
        }
    }

    @Override
    public void refresh() {
        switch(model.getConnectionState()){
            case offline:{
                connectStateLabel.setText("Отключен от сервера");
            } break;
            case tryToConnect:{
                connectStateLabel.setText("Пытаюсь подключиться");
            } break;
            case cantConnectToServer:{
                connectStateLabel.setText("Сервер не отвечает");
            }break;
            case online:{
                connectStateLabel.setText("Подключен");
            }
        }
        if(!model.getCurrentIP().isEmpty())
            connectIPField.setText(model.getCurrentIP());
        else
            connectIPField.setText(model.getDefaultIP());
    }

    public android.os.Handler handler = new android.os.Handler(new android.os.Handler.Callback() {
        @Override
        public boolean handleMessage(android.os.Message msg) {
            int caseState = msg.what;
            if (caseState == IModel.ModelState.loginFrame.ordinal()) {
                Activity tmp = new LoginActivity();
                Intent intent = new Intent(MainActivity.this, tmp.getClass());
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            }
            else if (caseState == IModel.ModelState.startFrame.ordinal()){
                refresh();
            }
            else if (caseState == IModel.ModelState.onlyRefresh.ordinal()){
                refresh();
            }
            System.err.println("Вернул true");
            return true;

        }
    });

    @Override
    public android.os.Handler getHandler() {
        return handler;
    }

    @Override
    public IModel.ModelState getRelevantState() {
        return IModel.ModelState.startFrame;
    }

    @Override
    protected void onResume(){
        super.onResume();
        System.err.println("MainActivity.onResume()  - -- --");
        controller.resumeActivity(this);
        refresh();
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        System.err.println("MainActivity DESTROY!");
        controller.closeApp();
        //controller.disconnect();
    }
}
