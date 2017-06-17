package com.example.seawar;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class StatisticActivity extends AppCompatActivity implements IRefreshable{

    IModel model;
    IController controller;

    ArrayList<String> stats;
    TextView header;
    ListView statisticView;
    Button buttonNext5;
    Button buttonPrev5;
    Button turnStatisticButton;
    boolean isNowShowMyStats;
    String headerText;

    int offset;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistic);
        offset = 0;
        stats = new ArrayList<String>();
        isNowShowMyStats = false;
        model = MyModel.getInstance();
        controller = MyController.getInstance();
        controller.setCurrentActivity(this);
        headerText = String.format("TOP " + (1 + offset) + " - " + (5 + offset));
        header = (TextView)findViewById(R.id.statisticHeader);
        header.setText(isNowShowMyStats?"My stat":headerText);
        statisticView = (ListView)findViewById(R.id.statisticList);
        buttonNext5 = (Button)findViewById(R.id.buttonNext5);
        buttonPrev5 = (Button)findViewById(R.id.buttonPrev5);
        buttonPrev5.setEnabled(!isNowShowMyStats);
        buttonNext5.setEnabled(!isNowShowMyStats);
        turnStatisticButton = (Button)findViewById(R.id.turnStatButton);

        turnStatisticButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isNowShowMyStats = !isNowShowMyStats;
                turnStatisticButton.setText(isNowShowMyStats?"ME":"ALL");
                if(isNowShowMyStats) {
                    buttonPrev5.setEnabled(!isNowShowMyStats);
                    buttonNext5.setEnabled(!isNowShowMyStats);
                    controller.showMyStatsHandler(isNowShowMyStats);
                }
                else {
                    buttonPrev5.setEnabled(!isNowShowMyStats);
                    buttonNext5.setEnabled(!isNowShowMyStats);
                    controller.showMyStatsHandler(isNowShowMyStats);
                }
            }
        });
        ArrayList<String> stats = model.getListOfStat();
        final ArrayAdapter<String> adapter;
        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, stats);
        statisticView.setAdapter(adapter);


        buttonNext5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                controller.refreshStatisticHandler(offset + 5);
            }
        });
        buttonPrev5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (offset - 5 >= 0){
                    controller.refreshStatisticHandler(offset - 5);
                }
            }
        });

    }

    public android.os.Handler handler = new android.os.Handler(new android.os.Handler.Callback() {
        @Override
        public boolean handleMessage(android.os.Message msg) {

            if (msg.what == IModel.ModelState.startFrame.ordinal()) {
                Activity tmp = new MainActivity();
                Intent intent = new Intent(StatisticActivity.this, tmp.getClass());
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            }
            else if(msg.what == IModel.ModelState.mainMenuFrame.ordinal()){
                Activity tmp = new MainMenuActivity();
                Intent intent = new Intent(StatisticActivity.this, tmp.getClass());
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
        offset = model.getOffsetForStats();
        stats = model.getListOfStat();
        final ArrayAdapter<String> adapter;
        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, stats);
        statisticView.setAdapter(adapter);
        headerText = String.format("TOP " + (1 + offset) + " - " + (5 + offset));
        header.setText(isNowShowMyStats?"My stat":headerText);
        buttonPrev5.setEnabled(!isNowShowMyStats);
        buttonNext5.setEnabled(!isNowShowMyStats);

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
        return IModel.ModelState.statisticFrame;
    }
}
