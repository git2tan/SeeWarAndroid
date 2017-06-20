package com.example.seawar;
import java.util.ArrayList;
import java.util.Queue;
import java.util.Stack;

/**
 * Created by Artem on 06.05.2017.
 */
public interface IModel {
    public String getLogin();
    public String getPassword();
    public ModelState getState();
    public boolean isOtherView();
    public String getDefaultIP();
    public int getDefaultPort();
    public String getCurrentIP();
    public ConnectionState getConnectionState();
    public ModelState getCurrentState();
    public Stack<ChatMessage> getLobbyMessage();
    public Stack<ChatMessage> getGameMessage();
    public RegistrationState getRegistrationState();
    public IGame getGame();
    public boolean isPrepareOrientationShipHorizontal();
    public boolean isAllShipOnBoard();
    public IGame.ShipType getSelectedTypeShip();
    public ArrayList<ServerGame> getListOFServersGames();
    public ArrayList<String> getListOfServerGamesString();
    ArrayList<String> getListOfStat();
    public boolean isNeedRefreshListOfGame();
    public boolean isOpponentReady();
    public boolean isThisReady();
    public boolean isNowMyTurn();
    public boolean isPrepareToShot();
    public int getCoordX();
    public int getCoordY();
    public void setConnectionState(ConnectionState connectionState);
    public void setCurState(ModelState state);
    public void setCurIP(String IP);
    public int getOpponentShipCount(IGame.ShipType type);
    public boolean isMiss();
    public boolean isHit();
    public boolean isOpponentHit();
    public boolean isOpponentMiss();
    public boolean isWinner();
    public boolean isLoser();
    public String getFirstGamerLogin();
    public String getSecondGamerLogin();
    public String getWinnerName();
    public int getOffsetForStats();
    enum ConnectionState{
        offline,
        tryToConnect,
        cantConnectToServer,
        online,
        cantLogin,
        isAuthorizedOnTheServer
    }
    enum ModelState{
        startFrame,
        loginFrame,
        registrationFrame,
        mainMenuFrame,
        lobbyFrame,
        createGameFrame,
        connectToGameFrame,
        inGameState,
        statisticFrame,
        observerFrame,
        onlyRefresh
    };
    enum RegistrationState{
        none,
        success,
        forbidden
    }
}
