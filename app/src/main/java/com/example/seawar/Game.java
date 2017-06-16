package com.example.seawar;
import java.util.Map;

/**
 * Created by Artem on 09.05.2017.
 */
public class Game implements IGame {
    private String opponent;
    private String opponent2;   //для реализации наблюдения
    private boolean isOpponentReady;
    private boolean isThisReady;
    private int[] initializeShipMap;
    private int[] initializedShipMap;
    private int[] opponentShips;
    private int observerCount;
    private GameBoard himselfGameBoard;
    private GameBoard opponentGameBoard;
    public Game(){
        initializeShipMap = new int[4];
        initializeShipMap[0] = 4;
        initializeShipMap[1] = 3;
        initializeShipMap[2] = 2;
        initializeShipMap[3] = 1;
        himselfGameBoard = new GameBoard();
        opponentGameBoard = new GameBoard();
        opponent = "[empty]";
        observerCount = 0;
        opponentShips = new int[4];
        opponentShips[0] = 4;
        opponentShips[1] = 3;
        opponentShips[2] = 2;
        opponentShips[3] = 1;
    }

    public Game(GameInfo info){
        opponent = info.login1;
        isThisReady = info.isReady1;
        himselfGameBoard = new GameBoard();
        if (info.login2 != null){
            opponent2  = info.login2;
            isOpponentReady = info.isReady2;
        }
        else{
            opponent2 = "[empty]";
            isOpponentReady = false;
        }
        opponentGameBoard = new GameBoard();
        observerCount = info.observerCount;

    }
    @Override
    public int getRemainsSetToPosition(ShipType type) {
        return initializeShipMap[type.ordinal()];
    }

    @Override
    public boolean isAllShipOnBoard() {
        boolean answer = true;
        for(int i = 0; i < 4; i++)
            if(initializeShipMap[i] != 0)
                answer = false;
        return answer;
    }

    @Override
    public GameBoard getHimselfGameBoard() {
        return himselfGameBoard ;
    }

    @Override
    public GameBoard getOpponentGameBoard() {
        return opponentGameBoard;
    }

    public void placeTheShip(int coordX, int coordY, IGame.ShipType type, boolean isHorizontal){
        himselfGameBoard.setShip(coordX, coordY, type, isHorizontal);
        --initializeShipMap[type.ordinal()];
    }

    public void unplaceTheShip(int coordX, int coordY, IGame.ShipType type, boolean isHorizontal){
        himselfGameBoard.unsetShip(coordX, coordY, type, isHorizontal);
        ++initializeShipMap[type.ordinal()];
    }

    @Override
    public String getOpponentName() {
        return opponent;
    }

    @Override
    public String getOpponent2Name() {
        return opponent2;
    }

    public void setOpponent(String login){
        opponent = login;
    }

    public int getObserverCount() {
        return observerCount;
    }

    public void setObserverCount(int observerCount) {
        this.observerCount = observerCount;
    }

    public boolean isOpponentReady() {
        return isOpponentReady;
    }

    public boolean isThisReady() {
        return isThisReady;
    }

    public void setOpponentReady(boolean opponentReady) {
        isOpponentReady = opponentReady;
    }

    public void setThisReady(boolean thisReady) {
        isThisReady = thisReady;
    }

    public int getOpponentShipCount(IGame.ShipType type){
        switch (type){
            case oneDeckShip:
                return opponentShips[0];
            case twoDeckShip:
                return opponentShips[1];
            case threeDeckShip:
                return opponentShips[2];
            case fourDeckShip:
                return opponentShips[3];
        }
        return -1;
    }
    public void markOpponentShipAsDestroyed(int coordX, int coordY){
        int shipSize = opponentGameBoard.markShipAsDestroyed(coordX, coordY);
        reduceOpponentShips(shipSize);
    }

    private void reduceOpponentShips(int size){
        opponentShips[size - 1]--;
    }

    public void actualizeGameBoardsForObs(int [][] gameBoard1, int[][] gameBoard2){
        himselfGameBoard.actualizeForObs(gameBoard1);
        opponentGameBoard.actualizeForObs(gameBoard2);
    }
}
