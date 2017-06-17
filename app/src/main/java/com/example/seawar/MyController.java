package com.example.seawar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import java.io.IOException;
import java.net.InetSocketAddress;

//import java.net.Socket;
//import java.net.SocketAddress;
import java.net.*;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by Artem on 06.05.2017.
 */
public class MyController implements IController, Observer {
    private static MyController instance;
    private MyModel model;
    private IRefreshable curActivity;
    private Thread receiverThread;
    private MySender sender;
    private HimselfGameBoardController himselfGameBoardController;
    private OpponentGameBoardController opponentGameBoardController;
    private NullController nullController;
    private int offset;
    private MyController(){
        model = MyModel.getInstance();
        offset = -1;
        sender = null;
        model.addObserver(this);
        himselfGameBoardController = new HimselfGameBoardController(this);
        opponentGameBoardController = new OpponentGameBoardController(this);
        nullController = new NullController();
    }

    public static MyController getInstance(){
        if (instance == null)
            instance = new MyController();
        return instance;
    }
    @Override
    public void buttonConnectHandler(final String IP) {
        if ( (model.getConnectionState() == IModel.ConnectionState.offline) || (model.getConnectionState() == IModel.ConnectionState.cantConnectToServer)){
            Thread thread1 = new Thread(new Runnable() {
                @Override
                public void run() {

                        if (sender == null) {
                            sender = new MySender(MyController.getInstance());
                            try {
                                Socket clientSocket = new Socket();
                                clientSocket.connect(new InetSocketAddress(IP, 4444), 500);
                                System.err.println("Client with name " + clientSocket.toString());
                                ClientReceiver receiver = new ClientReceiver(MyController.getInstance(), clientSocket);
                                sender.setSocket(clientSocket);
                                receiverThread = new Thread(receiver);
                                receiverThread.start();
                                model.setConnectionState(IModel.ConnectionState.online);
                                model.setCurState(IModel.ModelState.loginFrame);
                            } catch (IOException e) {
                                e.printStackTrace();
                                model.setConnectionState(IModel.ConnectionState.cantConnectToServer);
                                System.err.println("Ошибка подключения к серверу.");
                                sender  = null;
                            }
                        }
                    model.setCurIP(IP);
                }
            });
            thread1.start();
        }
        else {
            model.setCurState(IModel.ModelState.loginFrame);
        }
    }

    @Override
    public void buttonRegistrationHandler() {
        if (model.getConnectionState() == IModel.ConnectionState.online ||
                model.getConnectionState() == IModel.ConnectionState.isAuthorizedOnTheServer)
            model.setCurState(IModel.ModelState.registrationFrame);
        else{
            //???
        }
    }

    @Override
    public void buttonRegistrationNewAccount(String login, String pass) {
        sender.sendMessage(new Message(108, login, pass));
    }

    @Override
    public void buttonSendHandler(String message) {
        sender.sendMessage(new Message(105, model.getLogin(),message));
    }

    @Override
    public void buttonLoginHandler(String login, String pass) {

        if (model.getConnectionState() != IModel.ConnectionState.online ||
                model.getConnectionState() != IModel.ConnectionState.isAuthorizedOnTheServer ||
                login.equals(model.getLogin()))
            sender.sendMessage(new Message(100, login, pass));
        else
        {
            model.setCurState(IModel.ModelState.mainMenuFrame);
        }
    }

    @Override
    public void connectErrorHandler() {

    }

    @Override
    public void activateViewHandler(IModel.ModelState state) {
        model.setCurState(state);
    }

    @Override
    public void buttonJoinHandler() {
        sender.sendMessage(new Message(103,"",""));
    }

    @Override
    public void handleMessageFromServer(Message message) {
        switch (message.getNumberOfCommand()){
            case 101:{
                model.setLogin(message.getLogin());
                model.setPassword(message.getPass());
                model.setConnectionState(IModel.ConnectionState.isAuthorizedOnTheServer);
                model.setCurState(IModel.ModelState.mainMenuFrame);
            } break;
            case 102:{
                model.setConnectionState(IModel.ConnectionState.cantLogin);
            } break;
            case 103:{
                //пропускаю
            } break;
            case 104:{
                model.setCurState(IModel.ModelState.lobbyFrame);
            } break;
            case 105:{
                //пропускаю такое сообщение гениртся клиентом и обрабатывается на сервере
            } break;
            case 106:{
                //сообщение в чат лобби от конкретного пользователя
                model.addMessageToLobbyChat(new ChatMessage(message.getLogin(),message.getMessage()));

            } break;
            case 107:{
                //служебное сообщение сервера в чат Лобби
                model.addMessageToLobbyChat(new ChatMessage(message.getLogin(),message.getMessage()));
            } break;
            case 108:{
                //пропускаем
            } break;
            case 109:{
                //Положительный ответ на запрос регистрации
                model.setRegistrationState(IModel.RegistrationState.success);
            } break;
            case 110:{
                //отрицательный ответ на запрос регистрации
                model.setRegistrationState(IModel.RegistrationState.forbidden);
            } break;
            case 111:{
                // пропускаем так как это клиент генерирует такие сообщения
            } break;
            case 112:{
                //удалось создать игру
                model.CreateGame();
                model.setCurState(IModel.ModelState.createGameFrame);
            } break;
            case 113:{
                // пропускаем
            } break;
            case 114:{
                // положительный ответ на запрос о подключении к игре
                // так мы подключились то работаем от лица второго пользователя и для нас оппонент это первый пользователь
                model.CreateGame();
                model.setOpponent(message.getGameInfo().login1);
                model.setObserverCount(message.getGameInfo().observerCount);
                model.setOpponentReady(message.getGameInfo().isReady1);
                model.setCurState(IModel.ModelState.connectToGameFrame);
            } break;
            case 115:{
                // TODO переделать!!!
                //JOptionPane.showMessageDialog(mainFrame.getFrame(),"Не удалось подключиться к игре.");
            } break;
            case 116:{
                // пропускаем т.к. это мы генерируем такое сообщение
            }break;
            case 117:{
                model.setOpponent(message.getLogin());
            } break;
            case 118:{
                //ответ на наш запрос прервать создание игры
                model.setCurState(IModel.ModelState.lobbyFrame);
                //TODO сделать уничтожение данных об игре...
            } break;
            case 119:{
                // сообщение что произошли изменения в игровых данных
                GameInfo gameInfo = message.getGameInfo();
                //если сообщение пишло нам как хосту
                if(model.getLogin().equals(gameInfo.login1)){
                    model.setOpponent(gameInfo.login2);
                    model.setOpponentReady(gameInfo.isReady2);
                    model.setObserverCount(gameInfo.observerCount);

                }
                else{
                    model.setOpponent(gameInfo.login1);
                    model.setOpponentReady(gameInfo.isReady1);
                    model.setObserverCount(gameInfo.observerCount);

                }
            }break;
            case 120:{
                //пропускаем (это мы посылаем готовность и расстановку кораблей
            }break;
            case 121:{
                //пропускаем т.к. это мы посылаем что мы не готовы
            }break;
            case 122:{
                // сообщение от клиента о старте игры
            }break;
            case 123:{
                // пришел ответ на зпрос о старте игры что мы стартуем первым номером
                model.setNowMyTurn(true);
                model.setCurState(IModel.ModelState.inGameState);

            } break;
            case 124:{
                // пришло указание от сервера что хостовый игрок начал игру
                model.setNowMyTurn(false);
                model.setCurState(IModel.ModelState.inGameState);
            }break;
            case 125:{
                // пришло указание от сервера что игроки начали игру и мы должны начать наблюдать
            } break;
            case 126:{
                // это мы сгенерировали
            } break;
            case 127:{
                // ответ сервера попал по координатам (ход не переходит)
                model.getGame().getOpponentGameBoard().shot(message.getCoordX(),message.getCoordY(),false);
                model.setNotPrepareToShot();
                model.setNowMyTurn(true);
                model.setIsHit(true);
            } break;
            case 128:{
                // ответ сервера по координатам ()() - пусто  (ход переходит к оппоненту)
                model.getGame().getOpponentGameBoard().shot(message.getCoordX(),message.getCoordY(),true);
                model.setNotPrepareToShot();
                model.setNowMyTurn(false);
                model.setIsMiss(true);
            } break;
            case 129:{
                // сообщение сервера что по игроку стрельнули и попали (не его ход)
                model.getGame().getHimselfGameBoard().shot(message.getCoordX(),message.getCoordY());
                model.setNotPrepareToShot();
                model.setNowMyTurn(false);
                model.setIsOpponentHit(true);
            } break;
            case 130:{
                // сообщение сервера что по игроку стрельнули и промазали (его ход)
                model.getGame().getHimselfGameBoard().shot(message.getCoordX(),message.getCoordY());
                //model.setNotPrepareToShot();
                model.setNowMyTurn(true);
                model.setIsOpponentMiss(true);
            } break;
            case 131:{
                // сообщение сервера что по указанным координатам "потопили" корабль (на вражеской доске)
                System.err.println("Мы потопили корабль по координатам " + message.getCoordX() + " : " + message.getCoordY());
                model.destroyOpponentShip(message.getCoordX(), message.getCoordY());
            } break;
            case 132:{
                // сообщение сервера что по указанным координатам нам потопили корабль (на нашей доске)
                System.err.println("Нам потопили корабль по координатам " + message.getCoordX() + " : " + message.getCoordY());
                model.destroyHimselfShip(message.getCoordX(), message.getCoordY());
            } break;
            case 133:{
                // уведомление о выигрыше
                model.setIsWinner();
            } break;
            case 134:{
                // уведомление о проигрыше
                model.setIsLoser();
            } break;
            case 135:{
                //
            } break;
            case 136:{
                model.setCurState(IModel.ModelState.statisticFrame);
            } break;
            case 137:{
                sender.sendMessage(message);
            } break;
            case 138:{
                sender.sendMessage(message);
            } break;
            case 139:{
                model.addMessageToGameChat(new ChatMessage(message.getLogin(), message.getMessage()));
            } break;
            case 140:{
                model.addMessageToGameChat(new ChatMessage(message.getLogin(), message.getMessage()));
            } break;
            case 141:{
                sender.sendMessage(message);
            } break;
            case 142:{
                model.CreateObservableGame(message.getGameInfo());
                model.setCurState(IModel.ModelState.observerFrame);
            } break;
            case 143:{
                model.handleHitShotObs(message.getLogin(),message.getCoordX(),message.getCoordY());
            } break;
            case 144:{
                model.handleMissShotObs(message.getLogin(),message.getCoordX(),message.getCoordY());
            } break;
            case 145:{
                model.handleDestroyShipObs(message.getLogin(),message.getCoordX(),message.getCoordY());
            } break;
            case 146:{
                model.handleWinsObs(message.getLogin());
            } break;
            case 147:{
                // пришло актуальное состояние досок игроков для обсервера
                model.actualizeGameBoardsForObs(message.getBoard(), message.getBoard2());
            } break;
            case 148:{
                sender.sendMessage(message);
            } break;
            case 149:{
                sender.sendMessage(message);    // запрос статистики по логину (генерим на соотв кнопке)
            } break;
            case 150:{
                sender.sendMessage(message);    // сообщение о желании отключиться в момент создания игры
            } break;
            case 151:{
                // пришло уведомление что нам надо отключиться от игры (т.к. мы или OBS или игрок)
                if (model.getCurrentState() == IModel.ModelState.connectToGameFrame || model.getCurrentState() == IModel.ModelState.observerFrame){
                    model.setCurState(IModel.ModelState.mainMenuFrame);
                }
            } break;
            case 201:{
                // пришел список игр от сервера
                model.setListOFServersGames(parseServerGames(message));
            } break;
            case 202:{
                // пришла статистика по играм
                model.setStatisticList(message.getStatisticList());
                model.setOffsetForStats(offset);
            } break;
            case 203:{
                // пришел пустой список игр
                ArrayList<ServerGame> emptyList = new ArrayList<ServerGame>();
                model.setListOFServersGames(emptyList);
            } break;
            case 301:{
                // по каким-то причинам мы отключились
                System.err.println("Обработал отключение");
                sender = null;  // переделать
                model.resetData();
            }break;
            case 998:{
                // пришел пустой список статистики
            }break;
        }
    }

    @Override
    public void buttonCreateGameHandler() {
        sender.sendMessage(new Message(111,"",""));
    }

    @Override
    public void buttonConnectToGameHandler(int indx) {
        ServerGame game = null;
        if (indx != -1)
            game = model.getListOFServersGames().get(indx);

        if (game != null)
            sender.sendMessage(new Message(113,"" + game.getId(),""));
    }

    @Override
    public void buttonConnectToGameAsObsHandler(int indx) {
        ServerGame game = null;
        if (indx != -1)
            game = model.getListOFServersGames().get(indx);

        if (game != null)
            sender.sendMessage(new Message(141, "" + game.getId(), ""));
    }

    @Override
    public void update(Observable o, Object arg) {
        if(model.isOtherView())
        {
            curActivity.getHandler().sendEmptyMessage(model.getCurrentState().ordinal());
            model.isOtherView(false);
            System.err.println("Создание новой активити " + model.getCurrentState().name());
        }
        else{
            curActivity.getHandler().sendEmptyMessage(IModel.ModelState.onlyRefresh.ordinal());
            System.err.println("Обновление самой активити");
        }
    }

    @Override
    public void buttonTurnOrientationHandler() {
        model.setPrepareOrientationShipHorizontal(!model.isPrepareOrientationShipHorizontal());
    }

    @Override
    public void shipSelectHandler(IGame.ShipType type) {
        model.setSelectedTypeShip(type);
    }

    @Override
    public void cancelCreateGameButton() {
        if (model.getConnectionState() != IModel.ConnectionState.offline && sender != null)
            sender.sendMessage(new Message(150,"",""));
    }

    @Override
    public void mouseClickedSpriteHandler(int coordX, int coordY) {
        //проверяем что не самый первый клик (то есть еще не был выбран тип корабля
        IGame.ShipType type = model.getSelectedTypeShip();
        if (type != null){
            // проверяем что количество доступных кораблей выбранного типа больше 0
            if (model.getGame().getRemainsSetToPosition(type) > 0){
                //проверяем что в выбранное место можно установить корабль выбранноо типа
                if (model.getGame().getHimselfGameBoard().isPossibleToPlaceShip(coordX, coordY, type, model.isPrepareOrientationShipHorizontal())){
                    //ставим корабль на место
                    model.placeTheShip(coordX, coordY);
                }

            }

        }
    }

    @Override
    public void checkBoxIsReadyHandler(boolean flag) {
        if (flag){
            // посылаем на сервер сообщение с расположением кораблей
            sender.sendMessage(new Message(120,model.getGame().getHimselfGameBoard().toString(),""));
        }
        else{
            sender.sendMessage(new Message(121,"",""));
        }
    }

    @Override
    public void startGameButtonHandler() {
        sender.sendMessage(new Message(122, "",""));
    }

    @Override
    public IClickedController getHimselfGameBoardController() {
        return himselfGameBoardController;
    }

    @Override
    public IClickedController getOpponentGameBoardController() {
        return opponentGameBoardController;
    }

    @Override
    public void mouseClickedSpriteOpponentHandler(int coordX, int coordY) {
        //метод обработки нажатия на спрайт поля оппонента
        model.getGame().getOpponentGameBoard().mark(coordX, coordY);
        model.setPrepareToShot(coordX, coordY);
    }

    @Override
    public void buttonFireHandler() {
        if (model.isNowMyTurn() && model.isPrepareToShot())
        {
            model.setNowMyTurn(false);
            model.setNotPrepareToShot();
            sender.sendMessage(new Message(126, "" + model.getCoordX(), "" + model.getCoordY()));
        }
    }

    @Override
    public void setCurrentActivity(IRefreshable activity) {
        this.curActivity = activity;
    }

    @Override
    public void resumeActivity(IRefreshable activity) {
        this.curActivity = activity;
        model.setCurState(activity.getRelevantState());
    }

    private ArrayList<ServerGame> parseServerGames(Message message){
        ArrayList<ServerGame> request = new ArrayList<ServerGame>();
        ArrayList<String> list = message.getListOfGame();
        for (String one : list){
            request.add(Decoder.decodeStringToServerGame(one));
        }
        return request;
    }

    @Override
    public IClickedController getNullController() {
        return nullController;
    }

    @Override
    public void endGameHandler() {
        model.setCurState(IModel.ModelState.mainMenuFrame);
    }

    @Override
    public void showStatiscticHandler() {
        sender.sendMessage(new Message(135,"",""));
    }

    @Override
    public void refreshStatisticHandler(int offset) {
        if (this.offset != offset){
            sender.sendMessage(new Message(137, "" + offset, ""));
            this.offset = offset;
        }
    }

    @Override
    public void disconnect() {
        if (model.getConnectionState() != IModel.ConnectionState.offline && sender != null)
            sender.sendMessage(new Message(300, "", ""));
        model.resetData();
    }

    @Override
    public void buttonSendMessageToGameChat(String message) {
        sender.sendMessage(new Message(138, model.getLogin(),message));
    }

    @Override
    public void undoPlaceLastShip() {
        model.undoPlaceLastShip();
    }

    @Override
    public void resetBoard() {
        model.resetBoard();
    }

    @Override
    public void surrenderButtonHandle() {
        sender.sendMessage(new Message(148,"",""));
    }

    @Override
    public void showMyStatsHandler(boolean isNowMyStatTurn) {
        if (isNowMyStatTurn)
            sender.sendMessage(new Message(149, model.getLogin(),""));
        else
            sender.sendMessage(new Message(137, "" + offset, ""));
    }

    @Override
    public void stopObservGameHandler() {
        sender.sendMessage(new Message(152,"",""));
    }

    @Override
    public void disconnectFromLobby() {
        if (sender != null)
            sender.sendMessage(new Message(116,"",""));
    }
}
