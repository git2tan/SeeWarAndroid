package com.example.seawar;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsoluteLayout;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;

import android.widget.TextView;

public class CreateGameActivity extends AppCompatActivity implements IRefreshable{

    AbsoluteLayout board;
    ImageView shipOrientationImage;
    ImageView ship1Image;
    ImageView ship2Image;
    ImageView ship3Image;
    ImageView ship4Image;
    TextView opponentNameLabel;
    TextView opponentStateLabel;
    TextView observersCountLabel;
    TextView ship1CountLabel;
    TextView ship2CountLabel;
    TextView ship3CountLabel;
    TextView ship4CountLabel;
    CheckBox readyCheckBox;
    Button turnButton;
    Button startButton;
    Button undoButton;
    Button resetButton;
    IController controller;
    IModel model;
    Sprite[][] arrayOfSprites;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_game);
        controller = MyController.getInstance();
        model = MyModel.getInstance();
        controller.setCurrentActivity(this);

        shipOrientationImage = (ImageView)findViewById(R.id.shipOrientationImage);
        turnButton = (Button)findViewById(R.id.turnButton);
        ship1Image = (ImageView)findViewById(R.id.ship1Image);
        ship2Image = (ImageView)findViewById(R.id.ship2Image);
        ship3Image = (ImageView)findViewById(R.id.ship3Image);
        ship4Image = (ImageView)findViewById(R.id.ship4Image);

        readyCheckBox = (CheckBox)findViewById(R.id.readyCheckBox);
        startButton = (Button)findViewById(R.id.startButton);
        undoButton = (Button)findViewById(R.id.undoButton);
        resetButton = (Button)findViewById(R.id.resetButton);
        opponentNameLabel = (TextView)findViewById(R.id.opponentNameLable);
        opponentStateLabel = (TextView)findViewById(R.id.opponentStateLabel);
        observersCountLabel = (TextView)findViewById(R.id.observersCountLabel);

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

        ship1CountLabel = (TextView)findViewById(R.id.ship1CountLabel);
        ship2CountLabel = (TextView)findViewById(R.id.ship2CountLabel);
        ship3CountLabel = (TextView)findViewById(R.id.ship3CountLabel);
        ship4CountLabel = (TextView)findViewById(R.id.ship4CountLabel);


        ship1CountLabel.setText(model.getGame().getRemainsSetToPosition(IGame.ShipType.oneDeckShip) + "");
        ship2CountLabel.setText(model.getGame().getRemainsSetToPosition(IGame.ShipType.twoDeckShip) + "");
        ship3CountLabel.setText(model.getGame().getRemainsSetToPosition(IGame.ShipType.threeDeckShip) + "");
        ship4CountLabel.setText(model.getGame().getRemainsSetToPosition(IGame.ShipType.fourDeckShip) + "");


        turnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                controller.buttonTurnOrientationHandler();
            }
        });

        undoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                controller.undoPlaceLastShip();
            }
        });

        startButton.setEnabled(false);

        board = (AbsoluteLayout) findViewById(R.id.board);

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

        if(model.isPrepareOrientationShipHorizontal())
            shipOrientationImage.setImageResource(R.drawable.static_4);
        else
            shipOrientationImage.setImageResource(R.drawable.static_4_v);

        readyCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(readyCheckBox.isChecked()){
                    turnButton.setEnabled(false);
                    resetButton.setEnabled(false);
                    undoButton.setEnabled(false);
                    startButton.setEnabled(model.isOpponentReady());

                    controller.checkBoxIsReadyHandler(true);
                }
                else{
                    turnButton.setEnabled(true);
                    resetButton.setEnabled(true);
                    undoButton.setEnabled(true);
                    startButton.setEnabled(false);

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

        readyCheckBox.setEnabled(false);

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                controller.startGameButtonHandler();
            }
        });
    }

    @Override
    public void refresh() {
        if(model.isPrepareOrientationShipHorizontal())
            shipOrientationImage.setImageResource(R.drawable.static_4);
        else
            shipOrientationImage.setImageResource(R.drawable.static_4_v);

        for(int i = 0; i < 10; i++)
            for(int j = 0; j < 10; j++)
            {
                arrayOfSprites[i][j].refresh();
            }

        ship1CountLabel.setText(model.getGame().getRemainsSetToPosition(IGame.ShipType.oneDeckShip) + "");
        ship2CountLabel.setText(model.getGame().getRemainsSetToPosition(IGame.ShipType.twoDeckShip) + "");
        ship3CountLabel.setText(model.getGame().getRemainsSetToPosition(IGame.ShipType.threeDeckShip) + "");
        ship4CountLabel.setText(model.getGame().getRemainsSetToPosition(IGame.ShipType.fourDeckShip) + "");

        if(!model.getGame().getOpponentName().isEmpty()) {
            if (!opponentNameLabel.getText().equals(model.getGame().getOpponentName()))
                opponentNameLabel.setText(model.getGame().getOpponentName().isEmpty() ? "[empty]" : model.getGame().getOpponentName());
        }
        opponentStateLabel.setText(model.getGame().isOpponentReady()?"V":"X");
        observersCountLabel.setText(model.getGame().getObserverCount() + "");

        if(model.isAllShipOnBoard()){
//            message.setText("");
            readyCheckBox.setEnabled(true);
        }
        else{
            readyCheckBox.setEnabled(false);
//            message.setText("Не все корабли установлены на поле");
        }

        startButton.setEnabled(readyCheckBox.isChecked() && model.isOpponentReady());
    }

    public android.os.Handler handler = new android.os.Handler(new android.os.Handler.Callback() {
        @Override
        public boolean handleMessage(android.os.Message msg) {

            if (msg.what == IModel.ModelState.startFrame.ordinal()) {
                Activity tmp = new LoginActivity();
                Intent intent = new Intent(CreateGameActivity.this, tmp.getClass());
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
            else if(msg.what == IModel.ModelState.mainMenuFrame.ordinal()){
                Activity tmp = new MainMenuActivity();
                Intent intent = new Intent(CreateGameActivity.this, tmp.getClass());
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
            else if(msg.what == IModel.ModelState.inGameState.ordinal()){
                Activity tmp = new InGameActivity();
                Intent intent = new Intent(CreateGameActivity.this, tmp.getClass());
                startActivity(intent);
            }
            else if(msg.what == IModel.ModelState.onlyRefresh.ordinal()){
                refresh();
            }
            return false;
        }
    });

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
        return IModel.ModelState.createGameFrame;
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        controller.cancelCreateGameButton();
        System.err.println("CreateGameActivity.onDestroy() - disconnect from game!");
    }
}
