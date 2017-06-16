package com.example.seawar;

/**
 * Created by Artem on 12.06.2017.
 */

public class SetShipRecordItem {
    int x;
    int y;
    IGame.ShipType type;
    boolean isOrientHorz;
    SetShipRecordItem(int coordX, int coordY, IGame.ShipType type, boolean isOrientHorz){
        this.x = coordX;
        this.y = coordY;
        this.type = type;
        this.isOrientHorz = isOrientHorz;
    }
}
