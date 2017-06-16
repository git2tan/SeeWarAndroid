package com.example.seawar;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by Artem on 30.05.2017.
 */

public class Sprite extends android.support.v7.widget.AppCompatImageView {
    IClickedController controller;
    IGameBoard model;
    private int numberOfSprite;
    int coordX;
    int coordY;
    public Sprite(Context context, IGameBoard model, final IClickedController controller, final int coordX, final int coordY) {
        super(context);
        this.model = model;
        this.controller = controller;
        this.coordX = coordX;
        this.coordY = coordY;

        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(controller != null)
                    controller.mouseClickedSpriteHandler(coordX, coordY);
            }
        });
        loadSprite();
    }

    public void loadSprite(){
        numberOfSprite = model.getNumberOfSprite(coordX,coordY);

        String fileName = "i" + numberOfSprite;

        Context context = this.getContext();
        int id = context.getResources().getIdentifier(fileName, "drawable", context.getPackageName());
        this.setImageResource(id);
    }


    public void refresh(){
        if(numberOfSprite != model.getNumberOfSprite(coordX,coordY)) {
            loadSprite();
        }
    }
}
