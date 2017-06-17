package com.example.seawar;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainMenuActivity extends AppCompatActivity implements IRefreshable {
    IController controller;
    IModel model;

    Button joinButton;
    Button createButton;
    Button statisticButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        controller = MyController.getInstance();
        model = MyModel.getInstance();
        controller.setCurrentActivity(this);
        joinButton = (Button) findViewById(R.id.joinButton);
        createButton = (Button) findViewById(R.id.createButton);

        statisticButton = (Button) findViewById(R.id.statisticButton);

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                controller.buttonCreateGameHandler();
            }
        });
        joinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                controller.buttonJoinHandler();
            }
        });

        statisticButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                controller.showStatiscticHandler();
                controller.refreshStatisticHandler(0);
            }
        });
    }

    public android.os.Handler handler = new android.os.Handler(new android.os.Handler.Callback() {
        @Override
        public boolean handleMessage(android.os.Message msg) {

            if (msg.what == IModel.ModelState.startFrame.ordinal()) {
                Activity tmp = new LoginActivity();
                Intent intent = new Intent(MainMenuActivity.this, tmp.getClass());
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
//                controller.setCurrentActivity((IRefreshable) tmp);
            }
            else if(msg.what == IModel.ModelState.mainMenuFrame.ordinal()){
                Activity tmp = new MainMenuActivity();
                Intent intent = new Intent(MainMenuActivity.this, tmp.getClass());
                startActivity(intent);
            }
            else if(msg.what == IModel.ModelState.createGameFrame.ordinal()){
                Activity tmp = new CreateGameActivity();
                Intent intent = new Intent(MainMenuActivity.this, tmp.getClass());
                startActivity(intent);
            }
            else if(msg.what == IModel.ModelState.lobbyFrame.ordinal()){
                Activity tmp = new LobbyActivity();
                Intent intent = new Intent(MainMenuActivity.this, tmp.getClass());
                startActivity(intent);
            }
            else if(msg.what == IModel.ModelState.statisticFrame.ordinal()){
                Activity tmp = new StatisticActivity();
                Intent intent = new Intent(MainMenuActivity.this, tmp.getClass());
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

    }

    @Override
    public Handler getHandler() {
        return handler;
    }

    @Override
    protected void onResume(){
        super.onResume();

        controller.resumeActivity(this);
    }

    @Override
    public IModel.ModelState getRelevantState() {
        return IModel.ModelState.mainMenuFrame;
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        System.err.println("MainMenuActivity DESTROY!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
    }
}
