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
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.Stack;

public class InGameActivity extends AppCompatActivity implements IRefreshable{
    IModel model;
    IController controller;

    TextView ship1Label;
    TextView ship2Label;
    TextView ship3Label;
    TextView ship4Label;

    AbsoluteLayout himselfBoard;
    AbsoluteLayout opponentBoard;
    ScrollView scrollChatInGame;

    Sprite[][] arrayOfSpritesHimselfBoard;

    Sprite[][] opponentSprites;

    EditText gameChat;
    EditText messageField;

    Button sendMessageButton;
    Button fireButton;
    Button surrender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_in_game);
        model = MyModel.getInstance();
        controller = MyController.getInstance();
        controller.setCurrentActivity(this);

        ship1Label = (TextView) findViewById(R.id.ship1CountLabel3);
        ship2Label = (TextView) findViewById(R.id.ship2CountLabel3);
        ship3Label = (TextView) findViewById(R.id.ship3CountLabel3);
        ship4Label = (TextView) findViewById(R.id.ship4CountLabel3);


        himselfBoard = (AbsoluteLayout)findViewById(R.id.himSelfBoard);
        opponentBoard = (AbsoluteLayout)findViewById(R.id.board3);

        scrollChatInGame = (ScrollView)findViewById(R.id.scrollChatInGame);
        gameChat = (EditText)findViewById(R.id.gameChat);
        gameChat.setEnabled(false);
        gameChat.setTextSize(13);
        gameChat.setTextColor(Color.BLACK);
        messageField = (EditText)findViewById(R.id.messageFieldInGame);

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
                gameChat.append("Я" + ": " + tmp.message + "\n");
            }
            else if(tmp.login.equals("SERVER")){
                String serverMessage = tmp.login +" --> " + tmp.message + "\n";
                gameChat.append(serverMessage.toUpperCase());
            }
            else{
                gameChat.append(tmp.login + ": "+tmp.message + "\n");
            }
            scrollChatInGame.scrollBy(0, gameChat.getBottom());
        }

        sendMessageButton = (Button)findViewById(R.id.sendMessageButton2);

        sendMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(messageField.getText().length() != 0) {
                    controller.buttonSendMessageToGameChat(messageField.getText().toString());
                    messageField.setText("");
                }
            }
        });
        fireButton = (Button)findViewById(R.id.fireButton);
        surrender = (Button)findViewById(R.id.surrenderButton);

        surrender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showYouWantSurrender();
            }
        });

        ship1Label.setText(model.getOpponentShipCount(IGame.ShipType.oneDeckShip) + "");
        ship2Label.setText(model.getOpponentShipCount(IGame.ShipType.twoDeckShip) + "");
        ship3Label.setText(model.getOpponentShipCount(IGame.ShipType.threeDeckShip) + "");
        ship4Label.setText(model.getOpponentShipCount(IGame.ShipType.fourDeckShip) + "");

        arrayOfSpritesHimselfBoard = new Sprite[10][10];
        int size = (int) pxFromDp(18);
        for(int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                Sprite one = new Sprite(himselfBoard.getContext(), model.getGame().getHimselfGameBoard(), controller.getNullController(), i, j);
                one.setLayoutParams(new AbsoluteLayout.LayoutParams(size, size, i*size, j*size));
                arrayOfSpritesHimselfBoard[i][j] = one;
                himselfBoard.addView(one);
            }
        }

        opponentSprites = new Sprite[10][10];
        int size2 = (int) pxFromDp(25);
        for(int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                Sprite one = new Sprite(opponentBoard.getContext(), model.getGame().getOpponentGameBoard(), controller.getOpponentGameBoardController(), i, j);
                one.setLayoutParams(new AbsoluteLayout.LayoutParams(size2, size2, i*size2, j*size2));
                opponentSprites[i][j] = one;
                opponentBoard.addView(one);
            }
        }
        fireButton.setEnabled(model.isNowMyTurn());

        fireButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                controller.buttonFireHandler();
            }
        });

    }

    public android.os.Handler handler = new android.os.Handler(new android.os.Handler.Callback() {
        @Override
        public boolean handleMessage(android.os.Message msg) {

            if (msg.what == IModel.ModelState.startFrame.ordinal()) {
                Activity tmp = new LoginActivity();
                Intent intent = new Intent(InGameActivity.this, tmp.getClass());
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
            else if(msg.what == IModel.ModelState.mainMenuFrame.ordinal()){
                Activity tmp = new MainMenuActivity();
                Intent intent = new Intent(InGameActivity.this, tmp.getClass());
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
        ship1Label.setText(model.getOpponentShipCount(IGame.ShipType.oneDeckShip) + "");
        ship2Label.setText(model.getOpponentShipCount(IGame.ShipType.twoDeckShip) + "");
        ship3Label.setText(model.getOpponentShipCount(IGame.ShipType.threeDeckShip) + "");
        ship4Label.setText(model.getOpponentShipCount(IGame.ShipType.fourDeckShip) + "");

        for(int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                arrayOfSpritesHimselfBoard[i][j].refresh();
            }
        }

        for(int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                opponentSprites[i][j].refresh();
            }
        }

        if(model.isMiss()){
            Toast.makeText(getApplicationContext(), "You miss...", Toast.LENGTH_SHORT).show();
        }
        if (model.isHit()) {
            Toast.makeText(getApplicationContext(), "You hit!", Toast.LENGTH_SHORT).show();
        }
        if(model.isOpponentHit()){
            Toast.makeText(getApplicationContext(), "Opponent hit! He turn", Toast.LENGTH_SHORT).show();
        }
        if(model.isOpponentMiss()){
            Toast.makeText(getApplicationContext(), "Opponent miss... You turn", Toast.LENGTH_SHORT).show();
        }

        if (model.isWinner())
            showWinner();

        if (model.isLoser())
            showLoser();

        fireButton.setEnabled(model.isNowMyTurn() && model.isPrepareToShot());


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
                gameChat.append("Я" + ": " + tmp.message + "\n");
            }
            else if(tmp.login.equals("SERVER")){
                String serverMessage = tmp.login +" --> " + tmp.message + "\n";
                gameChat.append(serverMessage.toUpperCase());
            }
            else{
                gameChat.append(tmp.login + ": "+tmp.message + "\n");
            }
            scrollChatInGame.scrollBy(0, gameChat.getBottom());
        }
    }

    @Override
    public Handler getHandler() {
        return handler;
    }

    private float dpFromPx(float px) {
        return px / getApplicationContext().getResources().getDisplayMetrics().density;
    }

    private float pxFromDp(float dp) {
        return dp * getApplicationContext().getResources().getDisplayMetrics().density;
    }

    private void showWinner(){
        AlertDialog.Builder builder = new AlertDialog.Builder(InGameActivity.this);
        builder.setTitle("Важное сообщение!").setMessage("Вы выиграли!").setCancelable(false).setNegativeButton("ОК", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                controller.endGameHandler();
                            }
                        });
        AlertDialog alert = builder.create();
        alert.show();
    }
    private void showLoser(){
        AlertDialog.Builder builder = new AlertDialog.Builder(InGameActivity.this);
        builder.setTitle("Важное сообщение!").setMessage("Вы проиграли(").setCancelable(false).setNegativeButton("ОК", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                controller.endGameHandler();
                            }
                        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void showYouWantSurrender(){
        AlertDialog.Builder builder = new AlertDialog.Builder(InGameActivity.this);
        builder.setTitle("Важное сообщение!").setMessage("Вы хотите сдаться?").setCancelable(false);
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
                controller.surrenderButtonHandle();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public IModel.ModelState getRelevantState() {
        return IModel.ModelState.inGameState;
    }

    protected void onDestroy(){
        super.onDestroy();
        if (model.getConnectionState() == IModel.ConnectionState.online)
            controller.cancelCreateGameButton();
        else
            System.err.println ("Предотвращена попытка отправки в уже отключенное состояние");
        System.err.println("InGameActivity.onDestroy()!");
    }
}
