package com.example.seawar;

import android.app.Activity;

/**
 * Created by Artem on 06.05.2017.
 */
public interface IController {
    void buttonConnectHandler(String IP);
    void buttonRegistrationHandler();
    void buttonRegistrationNewAccount(String login, String pass);
    void buttonLoginHandler(String login, String pass);
    void buttonSendHandler(String message);
    void buttonSendMessageToGameChat(String message);
    void connectErrorHandler();
    void disconnect();
    void activateViewHandler(IModel.ModelState state);
    void handleMessageFromServer(Message message);
    void buttonJoinHandler();
    void buttonCreateGameHandler();
    void buttonTurnOrientationHandler();
    void shipSelectHandler(IGame.ShipType type);
    void mouseClickedSpriteHandler(int coordX, int coordY);
    void buttonConnectToGameHandler(int indx);
    void buttonConnectToGameAsObsHandler(int indx);
    void stopObservGameHandler();
    void undoPlaceLastShip();
    void surrenderButtonHandle();
    void showMyStatsHandler(boolean isNowMyStatsTurn);
    void cancelCreateGameButton();
    void checkBoxIsReadyHandler(boolean flag);
    void startGameButtonHandler();
    void mouseClickedSpriteOpponentHandler(int coordX, int coordY);
    void buttonFireHandler();
    void endGameHandler();
    IClickedController getHimselfGameBoardController();
    IClickedController getOpponentGameBoardController();
    IClickedController getNullController();
    void setCurrentActivity(IRefreshable activity);
    void resumeActivity(IRefreshable activity);
    void showStatiscticHandler();
    void refreshStatisticHandler(int offset);
    void resetBoard();
}
