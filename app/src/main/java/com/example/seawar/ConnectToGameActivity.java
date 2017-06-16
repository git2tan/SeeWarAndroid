package com.example.seawar;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsoluteLayout;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

public class ConnectToGameActivity extends AppCompatActivity implements IRefreshable{
    IModel model;
    IController controller;

    AbsoluteLayout board;
    ImageView shipOrientationImage;
    ImageView ship1Image;
    ImageView ship2Image;
    ImageView ship3Image;
    ImageView ship4Image;
    Button turnButton;
    Button undoButton;
    Button resetButton;

    CheckBox readyCheckBox;

    TextView ship1CountLabel;
    TextView ship2CountLabel;
    TextView ship3CountLabel;
    TextView ship4CountLabel;
    TextView opponentStateLabel;
    TextView observersCountLabel;
    TextView opponentNameLabel;

    Sprite[][] arrayOfSprites;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect_to_game);

        model = MyModel.getInstance();
        controller = MyController.getInstance();
        controller.setCurrentActivity(this);

        board = (AbsoluteLayout)findViewById(R.id.board2connect);
        shipOrientationImage = (ImageView)findViewById(R.id.shipOrientationImage2);

        ship1Image = (ImageView)findViewById(R.id.ship1Image2);
        ship2Image = (ImageView)findViewById(R.id.ship2Image2);
        ship3Image = (ImageView)findViewById(R.id.ship3Image2);
        ship4Image = (ImageView)findViewById(R.id.ship4Image2);

        readyCheckBox = (CheckBox)findViewById(R.id.readyCheckBox2);

        ship1CountLabel     = (TextView)findViewById(R.id.ship1CountLabel2);
        ship2CountLabel     = (TextView)findViewById(R.id.ship2CountLabel2);
        ship3CountLabel     = (TextView)findViewById(R.id.ship3CountLabel2);
        ship4CountLabel     = (TextView)findViewById(R.id.ship4CountLabel2);
        opponentStateLabel  = (TextView)findViewById(R.id.opponentStateLabel2);
        opponentNameLabel   = (TextView)findViewById(R.id.opponentNameLabel2);
        observersCountLabel = (TextView)findViewById(R.id.observersCountLabel2);

        turnButton      = (Button)findViewById(R.id.turnButton2);
        undoButton      = (Button)findViewById(R.id.undoButton2);
        resetButton   = (Button)findViewById(R.id.resetButton2);

        undoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                controller.undoPlaceLastShip();
            }
        });

        turnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                controller.buttonTurnOrientationHandler();
            }
        });

        readyCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(readyCheckBox.isChecked()){
                    undoButton.setEnabled(false);
                    turnButton.setEnabled(false);
                    resetButton.setEnabled(false);

                    controller.checkBoxIsReadyHandler(true);
                }
                else{
                    undoButton.setEnabled(true);
                    turnButton.setEnabled(true);
                    resetButton.setEnabled(true);

                    controller.checkBoxIsReadyHandler(false);
                }
            }
        });

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                controller.resetBoard();
            }
        });

        ship1CountLabel.setText(model.getGame().getRemainsSetToPosition(IGame.ShipType.oneDeckShip) + "");
        ship2CountLabel.setText(model.getGame().getRemainsSetToPosition(IGame.ShipType.twoDeckShip) + "");
        ship3CountLabel.setText(model.getGame().getRemainsSetToPosition(IGame.ShipType.threeDeckShip) + "");
        ship4CountLabel.setText(model.getGame().getRemainsSetToPosition(IGame.ShipType.fourDeckShip) + "");

        ship1Image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                controller.shipSelectHandler(IGame.ShipType.oneDeckShip);
            }
        });

        ship2Image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                controller.shipSelectHandler(IGame.ShipType.twoDeckShip);
            }
        });

        ship3Image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                controller.shipSelectHandler(IGame.ShipType.threeDeckShip);
            }
        });

        ship4Image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                controller.shipSelectHandler(IGame.ShipType.fourDeckShip);
            }
        });

        arrayOfSprites = new Sprite[10][10];
        int size = (int) pxFromDp(25);
        for(int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                Sprite one = new Sprite(board.getContext(), model.getGame().getHimselfGameBoard(), controller.getHimselfGameBoardController(), i, j);
                one.setLayoutParams(new AbsoluteLayout.LayoutParams(size, size, i*size, j*size));
                arrayOfSprites[i][j] = one;
                board.addView(one);
            }
        }

        refresh();
    }


    public android.os.Handler handler = new android.os.Handler(new android.os.Handler.Callback() {
        @Override
        public boolean handleMessage(android.os.Message msg) {

            if (msg.what == IModel.ModelState.startFrame.ordinal()) {
                Activity tmp = new LoginActivity();
                Intent intent = new Intent(ConnectToGameActivity.this, tmp.getClass());
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
            else if(msg.what == IModel.ModelState.mainMenuFrame.ordinal()){
                Activity tmp = new MainMenuActivity();
                Intent intent = new Intent(ConnectToGameActivity.this, tmp.getClass());
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
            else if(msg.what == IModel.ModelState.inGameState.ordinal()){
                Activity tmp = new InGameActivity();
                Intent intent = new Intent(ConnectToGameActivity.this, tmp.getClass());
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
        // чекбокс
        if(model.isAllShipOnBoard()){
            readyCheckBox.setEnabled(true);
        }
        else{
            readyCheckBox.setEnabled(false);
        }

        // картинка отвечающая за ориентацию
        if (model.isPrepareOrientationShipHorizontal()){
            shipOrientationImage.setImageResource(R.drawable.static_4);
        }
        else{
            shipOrientationImage.setImageResource(R.drawable.static_4_v);
        }

        ship1CountLabel.setText(Integer.toString(model.getGame().getRemainsSetToPosition(IGame.ShipType.oneDeckShip)));
        ship2CountLabel.setText(Integer.toString(model.getGame().getRemainsSetToPosition(IGame.ShipType.twoDeckShip)));
        ship3CountLabel.setText(Integer.toString(model.getGame().getRemainsSetToPosition(IGame.ShipType.threeDeckShip)));
        ship4CountLabel.setText(Integer.toString(model.getGame().getRemainsSetToPosition(IGame.ShipType.fourDeckShip)));

        opponentStateLabel.setText(model.getGame().isOpponentReady()?"V":"X");
        observersCountLabel.setText(model.getGame().getObserverCount() + "");

        if(!opponentNameLabel.getText().equals(model.getGame().getOpponentName()))
            opponentNameLabel.setText(model.getGame().getOpponentName().isEmpty()?"[empty]":model.getGame().getOpponentName());

        opponentStateLabel.setText(model.getGame().isOpponentReady()?"V":"X");
        observersCountLabel.setText(model.getGame().getObserverCount() + "");

        for(int i = 0; i < 10; i++)
            for(int j = 0; j < 10; j++)
            {
                arrayOfSprites[i][j].refresh();
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

    @Override
    public IModel.ModelState getRelevantState() {
        return IModel.ModelState.connectToGameFrame;
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        controller.cancelCreateGameButton();
        System.err.println("CreateGameActivity.onDestroy() - disconnect from game!");
    }
}
