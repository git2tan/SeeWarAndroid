package com.example.seawar;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsoluteLayout;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Stack;

public class ObserverActivity extends AppCompatActivity implements IRefreshable{
    IController controller;
    IModel model;

    TextView gamer1LoginLabel;
    TextView gamer2LoginLabel;
    AbsoluteLayout gamer1Board;
    AbsoluteLayout gamer2Board;
    EditText inGameChat;
    Button sendMessageButton;
    EditText messageField;
    ScrollView inGameChatScroll;

    Sprite[][] gamer1Sprites;
    Sprite[][] gamer2Sprites;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_observer);

        model = MyModel.getInstance();
        controller = MyController.getInstance();
        controller.setCurrentActivity(this);

        gamer1LoginLabel = (TextView)findViewById(R.id.gamer1LoginLabel);
        gamer2LoginLabel = (TextView)findViewById(R.id.gamer2LoginLabel);

        gamer1Board =(AbsoluteLayout)findViewById(R.id.gamer1Board);
        gamer2Board = (AbsoluteLayout)findViewById(R.id.gamer2Board);

        inGameChat = (EditText)findViewById(R.id.inGameChatObs);
        sendMessageButton = (Button)findViewById(R.id.sendMessageObs);
        messageField = (EditText)findViewById(R.id.messageFieldObs);

        inGameChatScroll = (ScrollView)findViewById(R.id.inGameScroll);

        inGameChat.setEnabled(false);
        inGameChat.setTextSize(15);
        inGameChat.setTextColor(Color.BLACK);

        Stack<ChatMessage> stack = model.getGameMessage();
        Stack<ChatMessage> inversionStack = new Stack<ChatMessage>();
        while(!stack.isEmpty())
        {
            inversionStack.push(stack.pop());
        }
        while(!inversionStack.isEmpty()){
            ChatMessage tmp = inversionStack.pop();
            //TextArea
            if(model.getLogin().equals(tmp.login)){
                inGameChat.append("Я" + ": " + tmp.message + "\n");
            }
            else if(tmp.login.equals("SERVER")){
                String serverMessage = tmp.login +" --> " + tmp.message + "\n";
                inGameChat.append(serverMessage.toUpperCase());
            }
            else{
                inGameChat.append(tmp.login + ": "+tmp.message + "\n");
            }
            inGameChatScroll.scrollBy(0, inGameChat.getBottom());
        }

        gamer1LoginLabel.setText(model.getFirstGamerLogin());
        gamer2LoginLabel.setText(model.getSecondGamerLogin());

        gamer1Sprites = new Sprite[10][10];
        int size = (int) pxFromDp(18);
        for(int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                Sprite one = new Sprite(gamer1Board.getContext(), model.getGame().getHimselfGameBoard(), controller.getNullController(), i, j);
                one.setLayoutParams(new AbsoluteLayout.LayoutParams(size, size, i*size, j*size));
                gamer1Sprites[i][j] = one;
                gamer1Board.addView(one);
            }
        }

        gamer2Sprites = new Sprite[10][10];
        for(int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                Sprite one = new Sprite(gamer2Board.getContext(), model.getGame().getOpponentGameBoard(), controller.getNullController(), i, j);
                one.setLayoutParams(new AbsoluteLayout.LayoutParams(size, size, i*size, j*size));
                gamer2Sprites[i][j] = one;
                gamer2Board.addView(one);
            }
        }

        sendMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (messageField.getText().length() > 0) {
                    controller.buttonSendMessageToGameChat(messageField.getText().toString());
                    messageField.setText("");
                }
            }
        });
    }

    public android.os.Handler handler = new android.os.Handler(new android.os.Handler.Callback() {
        @Override
        public boolean handleMessage(android.os.Message msg) {

            if (msg.what == IModel.ModelState.startFrame.ordinal()) {
                Activity tmp = new MainActivity();
                Intent intent = new Intent(ObserverActivity.this, tmp.getClass());
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
            else if(msg.what == IModel.ModelState.mainMenuFrame.ordinal()){
                Activity tmp = new MainMenuActivity();
                Intent intent = new Intent(ObserverActivity.this, tmp.getClass());
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
            else if(msg.what == IModel.ModelState.onlyRefresh.ordinal()){
                refresh();
            }
            return false;
        }
    });

    @Override
    public void refresh() {
        Stack<ChatMessage> stack = model.getGameMessage();
        Stack<ChatMessage> inversionStack = new Stack<ChatMessage>();
        while(!stack.isEmpty())
        {
            inversionStack.push(stack.pop());
        }
        while(!inversionStack.isEmpty()){
            ChatMessage tmp = inversionStack.pop();
            //TextArea
            if(model.getLogin().equals(tmp.login)){
                inGameChat.append("Я" + ": " + tmp.message + "\n");
            }
            else if(tmp.login.equals("SERVER")){
                String serverMessage = tmp.login +" --> " + tmp.message + "\n";
                inGameChat.append(serverMessage.toUpperCase());
            }
            else{
                inGameChat.append(tmp.login + ": "+tmp.message + "\n");
            }
            inGameChatScroll.scrollBy(0, inGameChat.getBottom());
        }

        for(int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                gamer1Sprites[i][j].refresh();
                gamer2Sprites[i][j].refresh();
            }
        }

        if(model.isWinner())
            showWinner();

    }

    @Override
    public Handler getHandler() {
        return handler;
    }

    @Override
    public IModel.ModelState getRelevantState() {
        return IModel.ModelState.observerFrame;
    }

    private float pxFromDp(float dp) {
        return dp * getApplicationContext().getResources().getDisplayMetrics().density;
    }

    private void showWinner(){
        AlertDialog.Builder builder = new AlertDialog.Builder(ObserverActivity.this);
        builder.setTitle("Важное сообщение!").setMessage("Выиграл игрок под ником " + model.getWinnerName() + "!").setCancelable(false).setNegativeButton("ОК", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
                controller.endGameHandler();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    protected void onDestroy(){
        super.onDestroy();
        controller.stopObservGameHandler();
        System.err.println("ObserverActivity.onDestroy()!");
    }
}
