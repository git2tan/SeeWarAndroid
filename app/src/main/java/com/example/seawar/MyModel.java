package com.example.seawar;

import java.util.*;

/**
 * Created by Artem on 06.05.2017.
 */
public class MyModel extends Observable implements IModel{
    private static MyModel instance;

    private MyModel(){
        connectionState = ConnectionState.offline;
        curState = ModelState.startFrame;
        isOtherView = false;
        curIP ="";
        lobbyMessage = new Stack<ChatMessage>();
        gameMessage = new Stack<ChatMessage>();
        placeShipRecords = new Stack<SetShipRecordItem>();
        curRegistrationState = RegistrationState.none;
        isPrepareOrientationShipHorizontal = true;
        selectedTypeShip = null;
        serverGamesList = new ArrayList<ServerGame>();
        serverGameListString = new ArrayList<String>();
        statisticList = new ArrayList<String>();
        isNeedToRefreshListOfGame = false;
        isNowMyTurn = false;
        isPrepareToShot = false;
        isOpponentMiss = false;
        isOpponentHit = false;
        isWinner = false;
        isLoser = false;
        offsetForStats = 0;
    }

    public void resetData(){
        connectionState = ConnectionState.offline;
        curState = ModelState.startFrame;
        isOtherView = true;
        curIP ="";
        lobbyMessage = new Stack<ChatMessage>();
        gameMessage = new Stack<ChatMessage>();
        placeShipRecords = new Stack<SetShipRecordItem>();
        curRegistrationState = RegistrationState.none;
        isPrepareOrientationShipHorizontal = true;
        selectedTypeShip = null;
        serverGamesList = new ArrayList<ServerGame>();
        serverGameListString = new ArrayList<String>();
        statisticList = new ArrayList<String>();
        isNeedToRefreshListOfGame = false;
        isNowMyTurn = false;
        isPrepareToShot = false;
        isOpponentMiss = false;
        isOpponentHit = false;
        isWinner = false;
        isLoser = false;
        login = null;
        password = null;
        offsetForStats = 0;

        setChanged();
        notifyObservers();
    }

    public static MyModel getInstance() {
        if(instance == null)
            instance = new MyModel();
        return instance;
    }

    @Override
    public RegistrationState getRegistrationState() {
        RegistrationState tmp = curRegistrationState;
        if(tmp != RegistrationState.none)
            curRegistrationState = RegistrationState.none;
        return tmp;
    }

    @Override
    public IGame getGame() {
        return game;
    }

    @Override
    public String getLogin() {
        return login;
    }

    @Override
    public ModelState getState() {
        return curState;
    }

    @Override
    public boolean isOtherView() {
        return isOtherView;
    }

    @Override
    public String getDefaultIP() {
        return "192.168.1.39";
    }

    @Override
    public String getCurrentIP() {
        return curIP;
    }

    @Override
    public ConnectionState getConnectionState() {
        return connectionState;
    }

    @Override
    public ModelState getCurrentState() {
        return curState;
    }

    @Override
    public String getPassword(){
        return password;
    }

    @Override
    public boolean isPrepareOrientationShipHorizontal() {
        return isPrepareOrientationShipHorizontal;
    }

    @Override
    public boolean isAllShipOnBoard() {
        return game.isAllShipOnBoard();
    }

    @Override
    public IGame.ShipType getSelectedTypeShip() {
        return selectedTypeShip;
    }

    @Override
    public ArrayList<ServerGame> getListOFServersGames() {
        return serverGamesList;
    }

    @Override
    public boolean isNeedRefreshListOfGame() {
        return isNeedToRefreshListOfGame;
    }

    public void setConnectionState(ConnectionState connectionState) {
        if(this.connectionState != connectionState)
        {
            this.connectionState = connectionState;
            setChanged();
        }
        notifyObservers();
    }
    public void setCurIP(String IP){
        this.curIP = IP;
    }
    public void setChangedAndNeedNotify(){
        setChanged();
        notifyObservers();
    }
    public void setCurState(ModelState state){
        if(this.curState != state)
        {
            curState = state;
            isOtherView = true;
            setChanged();
        }
        notifyObservers();
    }
    public void isOtherView(boolean isOtherView){
        this.isOtherView = isOtherView;
    }
    public void setLogin(String login){
        this.login = login;
    }
    public void setPassword(String password){
        this.password = password;
    }

    public void setPrepareOrientationShipHorizontal(boolean prepareOrientationShipHorizontal) {
        if(isPrepareOrientationShipHorizontal != prepareOrientationShipHorizontal)
            setChanged();
        isPrepareOrientationShipHorizontal = prepareOrientationShipHorizontal;
        notifyObservers();
    }

    @Override
    public Stack<ChatMessage> getLobbyMessage() {
        return lobbyMessage;
    }

    @Override
    public Stack<ChatMessage> getGameMessage() {
        return gameMessage;
    }

    @Override
    public boolean isOpponentReady() {
        return game.isOpponentReady();
    }

    @Override
    public boolean isThisReady() {
        return game.isThisReady();
    }

    @Override
    public boolean isNowMyTurn() {
        return isNowMyTurn;
    }

    @Override
    public boolean isPrepareToShot() {
        return isPrepareToShot;
    }

    @Override
    public int getCoordX() {
        return coordX;
    }

    @Override
    public int getCoordY() {
        return coordY;
    }

    public void addMessageToLobbyChat(ChatMessage message){
        lobbyMessage.push(message);
        //тест стека на работу вместимости
        setChanged();
        notifyObservers();
    }

    public void addMessageToGameChat(ChatMessage message){
        gameMessage.push(message);
        setChanged();
        notifyObservers();
    }

    public void setRegistrationState(RegistrationState state){
        if(this.curRegistrationState != state){
            curRegistrationState = state;
            setChanged();
        }
        notifyObservers();
    }
    public void CreateGame(){
        game = new Game();
    }
    public void CreateObservableGame(GameInfo info){
        game = new Game(info);
    }

    public void setSelectedTypeShip(IGame.ShipType type){
        selectedTypeShip = type;
    }

    public void placeTheShip(int coordX, int coordY){
        game.placeTheShip(coordX, coordY, selectedTypeShip, isPrepareOrientationShipHorizontal);

        placeShipRecords.push(new SetShipRecordItem(coordX, coordY, selectedTypeShip, isPrepareOrientationShipHorizontal));

        setChanged();
        notifyObservers();
    }

    public void setListOFServersGames(ArrayList<ServerGame> list){
        serverGamesList = list;
        ArrayList<String> listString = new ArrayList<String>();
        for(ServerGame one : list){
            listString.add(one.toString());
        }
        serverGameListString = listString;
        isNeedToRefreshListOfGame = true;
        setChanged();
        notifyObservers();
    }

    @Override
    public ArrayList<String> getListOfServerGamesString() {
        return serverGameListString;
    }

    @Override
    public ArrayList<String> getListOfStat() {
        return statisticList;
    }

    public void setStatisticList(ArrayList<String> list){
        statisticList = list;
        setChanged();
        notifyObservers();
    }

    public void setOpponent(String login){
        if(game.getOpponentName() != login)
            setChanged();
        game.setOpponent(login);
        notifyObservers();
    }

    public void setObserverCount(int count){
        game.setObserverCount(count);
        setChanged();
        notifyObservers();
    }
    public void setThisReady(boolean flag){
        game.setThisReady(flag);
    }
    public void setOpponentReady(boolean flag){
        game.setOpponentReady(flag);
        setChanged();
        notifyObservers();
    }

    public void setNowMyTurn(boolean flag) {
        if(isNowMyTurn != flag)
            setChanged();

        isNowMyTurn = flag;
        notifyObservers();
    }
    public void setPrepareToShot(int coordX, int coordY){
        isPrepareToShot = true;
        this.coordX = coordX;
        this.coordY = coordY;
        setChanged();
        notifyObservers();
    }
    public void setNotPrepareToShot(){
        isPrepareToShot = false;
        setChanged();
        notifyObservers();
    }

    @Override
    public int getOpponentShipCount(IGame.ShipType type) {
        return game.getOpponentShipCount(type);
    }
    @Override
    public boolean isMiss() {
        boolean tmp = isMiss;
        isMiss = false;
        return tmp;
    }

    @Override
    public boolean isHit() {
        boolean tmp = isHit;
        isHit = false;
        return tmp;
    }

    @Override
    public boolean isOpponentHit() {
        boolean tmp = isOpponentHit;
        isOpponentHit = false;
        return tmp;
    }

    @Override
    public boolean isOpponentMiss() {
        boolean tmp = isOpponentMiss;
        isOpponentMiss = false;
        return tmp;
    }

    @Override
    public boolean isWinner() {
        boolean tmp = isWinner;
        isWinner = false;
        return tmp;
    }

    @Override
    public boolean isLoser() {
        boolean tmp = isLoser;
        isLoser = false;
        return tmp;
    }

    @Override
    public String getFirstGamerLogin() {
        return game.getOpponentName();
    }

    @Override
    public String getSecondGamerLogin() {
        return game.getOpponent2Name();
    }

    @Override
    public String getWinnerName() {
        return winnerName;
    }

    public void setIsMiss(boolean flag){
        isMiss = flag;
        setChanged();
        notifyObservers();
    }
    public void setIsHit(boolean flag){
        isHit = flag;
        setChanged();
        notifyObservers();
    }
    public void setIsOpponentMiss(boolean flag){
        isOpponentMiss = flag;
        setChanged();
        notifyObservers();
    }
    public void setIsOpponentHit(boolean flag){
        isOpponentHit = flag;
        setChanged();
        notifyObservers();
    }
    public void setIsWinner(){
        isWinner = true;
        setChanged();
        notifyObservers();
    }
    public void setIsLoser(){
        isLoser = true;
        setChanged();
        notifyObservers();
    }
    public void destroyHimselfShip(int coordX, int coordY){
        game.getHimselfGameBoard().markShipAsDestroyed(coordX, coordY);
        setChanged();
        notifyObservers();
    }
    public void destroyOpponentShip(int coordX, int coordY){
        game.markOpponentShipAsDestroyed(coordX, coordY);
        setChanged();
        notifyObservers();
    }
    public void handleHitShotObs(String login, int coordX, int coordY){
        if (game.getOpponentName().equals(login)){
            game.getOpponentGameBoard().shot(coordX,coordY,false);
        }
        else if (game.getOpponent2Name().equals(login)){
            game.getHimselfGameBoard().shot(coordX,coordY,false);
        }
        setChanged();
        notifyObservers();
    }

    public void handleMissShotObs(String login, int coordX, int coordY){
        if (game.getOpponentName().equals(login)){
            game.getOpponentGameBoard().shot(coordX,coordY,true);
        }
        else if (game.getOpponent2Name().equals(login)){
            game.getHimselfGameBoard().shot(coordX,coordY,true);
        }
        setChanged();
        notifyObservers();
    }

    public void handleDestroyShipObs(String login, int coordX, int coordY){
        if (game.getOpponentName().equals(login)){
            game.getOpponentGameBoard().markShipAsDestroyed(coordX,coordY);
        }
        else if (game.getOpponent2Name().equals(login)){
            game.getHimselfGameBoard().markShipAsDestroyed(coordX,coordY);
        }
        setChanged();
        notifyObservers();
    }

    public void handleWinsObs(String login){
        isWinner = true;
        winnerName = login;
        setChanged();
        notifyObservers();
    }

    public void actualizeGameBoardsForObs(int[][] gameBoard1, int [][] gameBoard2){
        game.actualizeGameBoardsForObs(gameBoard1,gameBoard2);
        setChanged();
        notifyObservers();
    }

    public void undoPlaceLastShip(){
        if (!placeShipRecords.isEmpty()){
            SetShipRecordItem item = placeShipRecords.pop();

            game.unplaceTheShip(item.x, item.y, item.type,item.isOrientHorz);
            setChanged();
        }
        notifyObservers();
    }

    public void resetBoard(){
        while (!placeShipRecords.isEmpty()){
            SetShipRecordItem item = placeShipRecords.pop();
            game.unplaceTheShip(item.x, item.y, item.type,item.isOrientHorz);
            setChanged();
        }
        notifyObservers();
    }

    public void setOffsetForStats(int value){
        offsetForStats = value;
    }

    public int getOffsetForStats(){
        return offsetForStats;
    }


    private int offsetForStats;
    private ConnectionState connectionState;
    private ModelState curState;
    private boolean isOtherView;
    private String login;
    private String password;
    private String curIP;
    private Stack<ChatMessage> lobbyMessage;
    private Stack<ChatMessage> gameMessage;
    private Stack<SetShipRecordItem> placeShipRecords;
    private RegistrationState curRegistrationState;
    private Game game;
    private boolean isPrepareOrientationShipHorizontal;
    private IGame.ShipType selectedTypeShip;
    private ArrayList<ServerGame> serverGamesList;
    private ArrayList<String> serverGameListString;
    private ArrayList<String> statisticList;
    private boolean isNeedToRefreshListOfGame;
    private boolean isNowMyTurn;
    private int coordX;
    private int coordY;
    private boolean isPrepareToShot;
    private boolean isMiss;
    private boolean isHit;
    private boolean isOpponentHit;
    private boolean isOpponentMiss;
    private boolean isWinner;
    private boolean isLoser;
    public String winnerName;
}
