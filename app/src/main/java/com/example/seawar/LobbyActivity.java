package com.example.seawar;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.constraint.solver.ArrayLinkedVariables;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ListViewCompat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Stack;

public class LobbyActivity extends AppCompatActivity implements IRefreshable{
    ListView listOfGame;
    EditText lobbyChat;
    IModel model;
    IController controller;

    ScrollView scrollView;
    EditText messageField;
    Button sendMessageButton;
    Button connectToGameButton;
    Button createGameButton;
    Button connectAsObsButton;
    int selectedGameIndx;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby);
        model = MyModel.getInstance();
        controller = MyController.getInstance();
        controller.setCurrentActivity(this);

        selectedGameIndx = -1;
        listOfGame = (ListView)findViewById(R.id.listOfGame);
        lobbyChat = (EditText)findViewById(R.id.lobbyChat);
        connectToGameButton = (Button)findViewById(R.id.connectToGameButton);
        messageField = (EditText)findViewById(R.id.messageField);
        connectAsObsButton = (Button)findViewById(R.id.connectToGameAsObsButton);
        sendMessageButton = (Button)findViewById(R.id.sendMessageButton);
        createGameButton = (Button)findViewById(R.id.createGameButton2);
        scrollView = (ScrollView)findViewById(R.id.scrollView123);
        connectToGameButton.setEnabled(selectedGameIndx != -1);
        connectAsObsButton.setEnabled(selectedGameIndx != -1);

        ArrayList<String> myListOfGame = model.getListOfServerGamesString();
        final ArrayAdapter<String> adapter;
        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, myListOfGame);
        listOfGame.setAdapter(adapter);
        listOfGame.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedGameIndx = position;
                Toast.makeText(getApplicationContext(), "Selected Item :" + position, Toast.LENGTH_SHORT).show();
                refresh();
            }
        });
        lobbyChat.setEnabled(false);
        lobbyChat.setTextSize(15);
        lobbyChat.setTextColor(Color.BLACK);

        createGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                controller.buttonCreateGameHandler();
            }
        });

        sendMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!(messageField.getText().length() == 0)){
                    controller.buttonSendHandler(messageField.getText().toString());
                    messageField.setText("");
                }
            }
        });

        connectToGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectedGameIndx != -1)
                    controller.buttonConnectToGameHandler(selectedGameIndx);
            }
        });

        connectAsObsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectedGameIndx != -1)
                    controller.buttonConnectToGameAsObsHandler(selectedGameIndx);
            }
        });
        refresh();
    }

    @Override
    public void refresh() {
        Stack<ChatMessage> stack = model.getLobbyMessage();
        Stack<ChatMessage> inversionStack = new Stack<ChatMessage>();
        while(!stack.isEmpty())
        {
            inversionStack.push(stack.pop());
        }
        while(!inversionStack.isEmpty()){
            ChatMessage tmp = inversionStack.pop();
            if(model.getLogin().equals(tmp.login)){
                lobbyChat.append("Я" + ": " + tmp.message + "\n");
            }
            else if(tmp.login.equals("SERVER")){
                String serverMessage = tmp.login +" --> " + tmp.message + "\n";
                lobbyChat.append(serverMessage.toUpperCase());
            }
            else{
                lobbyChat.append(tmp.login + ": "+tmp.message + "\n");
            }
            scrollView.scrollBy(0, lobbyChat.getBottom());
        }

        ArrayList<String> newMyListOfGame = model.getListOfServerGamesString();
        ArrayAdapter<String> newAdapter = new ArrayAdapter<String>(this,
            android.R.layout.simple_list_item_1, newMyListOfGame);
        listOfGame.setAdapter(newAdapter);

        connectToGameButton.setEnabled(selectedGameIndx != -1);
        connectAsObsButton.setEnabled(selectedGameIndx != -1);
    }

    public android.os.Handler handler = new android.os.Handler(new android.os.Handler.Callback() {
        @Override
        public boolean handleMessage(android.os.Message msg) {

            if (msg.what == IModel.ModelState.startFrame.ordinal()) {
                Activity tmp = new MainActivity();
                Intent intent = new Intent(LobbyActivity.this, tmp.getClass());
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
            else if (msg.what == IModel.ModelState.mainMenuFrame.ordinal()){
                Activity tmp = new MainMenuActivity();
                Intent intent = new Intent(LobbyActivity.this, tmp.getClass());
                startActivity(intent);
            }
            else if (msg.what == IModel.ModelState.connectToGameFrame.ordinal()){
                Activity tmp = new ConnectToGameActivity();
                Intent intent = new Intent(LobbyActivity.this, tmp.getClass());
                startActivity(intent);
            }
            else if (msg.what == IModel.ModelState.observerFrame.ordinal()){
                Activity tmp = new ObserverActivity();
                Intent intent = new Intent(LobbyActivity.this, tmp.getClass());
                startActivity(intent);
            }
            else if (msg.what == IModel.ModelState.createGameFrame.ordinal()){
                Activity tmp = new CreateGameActivity();
                Intent intent = new Intent(LobbyActivity.this, tmp.getClass());
                startActivity(intent);
            }
            else if (msg.what == IModel.ModelState.onlyRefresh.ordinal()){
                refresh();
            }
            return false;
        }
    });

    @Override
    public Handler getHandler() {
        return handler;
    }

    @Override
    public IModel.ModelState getRelevantState() {
        return IModel.ModelState.lobbyFrame;
    }

    @Override
    protected void onResume(){
        super.onResume();

        controller.resumeActivity(this);
        refresh();
    }

    @Override
    protected void onStop(){
        super.onStop();

        selectedGameIndx = -1;
    }
}
