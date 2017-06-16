package com.example.seawar;
/**
 * Created by Artem on 09.05.2017.
 */
public interface IGame {
    enum GameState{
        prepare,
    }
    enum ShipType{
        oneDeckShip,
        twoDeckShip,
        threeDeckShip,
        fourDeckShip
    }
    public int getRemainsSetToPosition(ShipType type);
    public boolean isAllShipOnBoard();
    public GameBoard getHimselfGameBoard();
    public GameBoard getOpponentGameBoard();
    public String getOpponent2Name();
    public String getOpponentName();
    public boolean isOpponentReady();
    public boolean isThisReady();
    public int getObserverCount();
}
