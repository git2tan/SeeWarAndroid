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
    private int prevOffset;
    private MyController(){
        model = MyModel.getInstance();
        prevOffset = 0;
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
        sender.sendMessage(new Message(MessageCommand.C_S_TryToRegisterNewLogin, login, pass));
    }

    @Override
    public void buttonSendHandler(String message) {
        sender.sendMessage(new Message(MessageCommand.C_S_MessageToLobby, model.getLogin(),message));
    }

    @Override
    public void buttonLoginHandler(String login, String pass) {

        if (model.getConnectionState() != IModel.ConnectionState.online ||
                model.getConnectionState() != IModel.ConnectionState.isAuthorizedOnTheServer ||
                login.equals(model.getLogin()))
            sender.sendMessage(new Message(MessageCommand.C_S_TryLogin, login, pass));
        else
        {
            model.setCurState(IModel.ModelState.mainMenuFrame);
        }
    }



    @Override
    public void buttonJoinHandler() {
        sender.sendMessage(new Message(MessageCommand.C_S_TryConnectToLobby,"",""));
    }

    @Override
    public void handleMessageFromServer(Message message) {
        switch (message.getNumberOfCommand()){
            case MessageCommand.S_C_SuccessLogin:{
                model.setLogin(message.getLogin());
                model.setPassword(message.getPass());
                model.setConnectionState(IModel.ConnectionState.isAuthorizedOnTheServer);
                model.setCurState(IModel.ModelState.mainMenuFrame);
            } break;
            case MessageCommand.S_C_InValidLogin:{
                model.setConnectionState(IModel.ConnectionState.cantLogin);
            } break;
            case MessageCommand.C_S_TryConnectToLobby:{
                //пропускаю
            } break;
            case MessageCommand.S_C_YouAllowConnectToLobby:{
                model.setCurState(IModel.ModelState.lobbyFrame);
            } break;
            case MessageCommand.C_S_MessageToLobby:{
                //пропускаю такое сообщение гениртся клиентом и обрабатывается на сервере
            } break;
            case MessageCommand.S_C_MessageToLobbyFromLogin:{
                //сообщение в чат лобби от конкретного пользователя
                model.addMessageToLobbyChat(new ChatMessage(message.getLogin(),message.getMessage()));

            } break;
            case MessageCommand.S_C_MessageToLobbyFromServer:{
                //служебное сообщение сервера в чат Лобби
                model.addMessageToLobbyChat(new ChatMessage(message.getLogin(),message.getMessage()));
            } break;
            case MessageCommand.C_S_TryToRegisterNewLogin:{
                //пропускаем
            } break;
            case MessageCommand.S_C_RegistrationSuccess:{
                //Положительный ответ на запрос регистрации
                model.setRegistrationState(IModel.RegistrationState.success);
            } break;
            case MessageCommand.S_C_RegistrationNotSuccess:{
                //отрицательный ответ на запрос регистрации
                model.setRegistrationState(IModel.RegistrationState.forbidden);
            } break;
            case MessageCommand.C_S_WantToCreateGame:{
                // пропускаем так как это клиент генерирует такие сообщения
            } break;
            case MessageCommand.S_C_AllowToCreateGame:{
                //удалось создать игру
                model.CreateGame();
                model.setCurState(IModel.ModelState.createGameFrame);
            } break;
            case MessageCommand.C_S_WantToConnectToGame:{
                // пропускаем
            } break;
            case MessageCommand.S_C_SuccessConnectToGame:{
                // положительный ответ на запрос о подключении к игре
                // так мы подключились то работаем от лица второго пользователя и для нас оппонент это первый пользователь
                model.CreateGame();
                model.setOpponent(message.getGameInfo().login1);
                model.setObserverCount(message.getGameInfo().observerCount);
                model.setOpponentReady(message.getGameInfo().isReady1);
                model.setCurState(IModel.ModelState.connectToGameFrame);
            } break;
            case MessageCommand.S_C_NotAllowConnectToGame:{
                //JOptionPane.showMessageDialog(mainFrame.getFrame(),"Не удалось подключиться к игре.");
            } break;
            case MessageCommand.C_S_ArmLeftTheLobby:{
                // пропускаем т.к. это мы генерируем такое сообщение
            }break;
            case MessageCommand.S_C_ToHostGamer_NewGamerConnect:{
                model.setOpponent(message.getLogin());
            } break;
            case MessageCommand.S_C_RequesttoArmDisconnectFromLobby:{
                //ответ на наш запрос прервать создание игры
                model.setCurState(IModel.ModelState.lobbyFrame);
            } break;
            case MessageCommand.S_C_NewGameInfo:{
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
            case MessageCommand.C_S_GamerReadyAndSendBoard:{
                //пропускаем (это мы посылаем готовность и расстановку кораблей
            }break;
            case MessageCommand.C_S_GamerNotReady:{
                //пропускаем т.к. это мы посылаем что мы не готовы
            }break;
            case MessageCommand.C_S_HostGamerStartTheGame:{
                // сообщение от клиента о старте игры
            }break;
            case MessageCommand.S_C_ToHostGamerStartTheGame:{
                // пришел ответ на зпрос о старте игры что мы стартуем первым номером
                model.setNowMyTurn(true);
                model.setCurState(IModel.ModelState.inGameState);

            } break;
            case MessageCommand.S_C_ToGamer2StartTheGame:{
                // пришло указание от сервера что хостовый игрок начал игру
                model.setNowMyTurn(false);
                model.setCurState(IModel.ModelState.inGameState);
            }break;
            case MessageCommand.S_C_AllowObserveTheGame:{
                // пришло указание от сервера что игроки начали игру и мы должны начать наблюдать
            } break;
            case MessageCommand.C_S_FireToCoord:{
                // это мы сгенерировали
            } break;
            case MessageCommand.S_C_YouHitToCoord:{
                // ответ сервера попал по координатам (ход не переходит)
                model.getGame().getOpponentGameBoard().shot(message.getCoordX(),message.getCoordY(),false);
                model.setNotPrepareToShot();
                model.setNowMyTurn(true);
                model.setIsHit(true);
            } break;
            case MessageCommand.S_C_YouMissToCoord:{
                // ответ сервера по координатам ()() - пусто  (ход переходит к оппоненту)
                model.getGame().getOpponentGameBoard().shot(message.getCoordX(),message.getCoordY(),true);
                model.setNotPrepareToShot();
                model.setNowMyTurn(false);
                model.setIsMiss(true);
            } break;
            case MessageCommand.S_C_OpponentHitToYou:{
                // сообщение сервера что по игроку стрельнули и попали (не его ход)
                model.getGame().getHimselfGameBoard().shot(message.getCoordX(),message.getCoordY());
                model.setNotPrepareToShot();
                model.setNowMyTurn(false);
                model.setIsOpponentHit(true);
            } break;
            case MessageCommand.S_C_OpponentMissToYou:{
                // сообщение сервера что по игроку стрельнули и промазали (его ход)
                model.getGame().getHimselfGameBoard().shot(message.getCoordX(),message.getCoordY());
                //model.setNotPrepareToShot();
                model.setNowMyTurn(true);
                model.setIsOpponentMiss(true);
            } break;
            case MessageCommand.S_C_YouDestroyTheShipByCoord:{
                // сообщение сервера что по указанным координатам "потопили" корабль (на вражеской доске)
                System.err.println("Мы потопили корабль по координатам " + message.getCoordX() + " : " + message.getCoordY());
                model.destroyOpponentShip(message.getCoordX(), message.getCoordY());
            } break;
            case MessageCommand.S_C_YourShipByCoordIsDestroyed:{
                // сообщение сервера что по указанным координатам нам потопили корабль (на нашей доске)
                System.err.println("Нам потопили корабль по координатам " + message.getCoordX() + " : " + message.getCoordY());
                model.destroyHimselfShip(message.getCoordX(), message.getCoordY());
            } break;
            case MessageCommand.S_C_YouWin:{
                // уведомление о выигрыше
                model.setIsWinner();
            } break;
            case MessageCommand.S_C_YouLose:{
                // уведомление о проигрыше
                model.setIsLoser();
            } break;
            case MessageCommand.C_S_NeedStatisticFromNumber:{
                //
            } break;
            case MessageCommand.S_C_ShowStatActivity:{
                model.setCurState(IModel.ModelState.statisticFrame);
            } break;
            case MessageCommand.C_S_NeedRefreshStatistic:{
                sender.sendMessage(message);
            } break;
            case MessageCommand.C_S_MessageToLobbyFromlogin:{
                sender.sendMessage(message);
            } break;
            case MessageCommand.S_C_MessageToLobbyFromlogin:{
                model.addMessageToGameChat(new ChatMessage(message.getLogin(), message.getMessage()));
            } break;
            case MessageCommand.S_C_MessageToLobbyAboutCoonect:{
                model.addMessageToGameChat(new ChatMessage(message.getLogin(), message.getMessage()));
            } break;
            case MessageCommand.C_S_WantToObserverToGame:{
                sender.sendMessage(message);
            } break;
            case MessageCommand.S_C_ShowObserverActivity:{
                model.CreateObservableGame(message.getGameInfo());
                model.setCurState(IModel.ModelState.observerFrame);
            } break;
            case MessageCommand.S_C_LoginFireToCoordAndHit:{
                model.handleHitShotObs(message.getLogin(),message.getCoordX(),message.getCoordY());
            } break;
            case MessageCommand.S_C_ToObs_LoginFireToCoordAndMiss:{
                model.handleMissShotObs(message.getLogin(),message.getCoordX(),message.getCoordY());
            } break;
            case MessageCommand.S_C_ToObs_LoginDestroyShipByCoord:{
                model.handleDestroyShipObs(message.getLogin(),message.getCoordX(),message.getCoordY());
            } break;
            case MessageCommand.S_C_ToObs_LoginWin:{
                model.handleWinsObs(message.getLogin());
            } break;
            case MessageCommand.S_C_ToObs_ActualGameInfo:{
                // пришло актуальное состояние досок игроков для обсервера
                model.actualizeGameBoardsForObs(message.getBoard(), message.getBoard2());
            } break;
            case MessageCommand.C_S_GamerWantToLose:{
                sender.sendMessage(message);
            } break;
            case MessageCommand.C_S_WantStatAboutlogin:{
                sender.sendMessage(message);    // запрос статистики по логину (генерим на соотв кнопке)
            } break;
            case MessageCommand.C_S_LeftFromTheGame:{
                sender.sendMessage(message);    // сообщение о желании отключиться в момент создания игры
            } break;
            case MessageCommand.S_C_HostLeftTheGame:{
                // пришло уведомление что нам надо отключиться от игры (т.к. мы или OBS или игрок)
                if (model.getCurrentState() == IModel.ModelState.connectToGameFrame || model.getCurrentState() == IModel.ModelState.observerFrame){
                    model.setCurState(IModel.ModelState.mainMenuFrame);
                }
            } break;
            case MessageCommand.S_C_ListOfLobbyGame:{
                // пришел список игр от сервера
                model.setListOFServersGames(parseServerGames(message));
            } break;
            case MessageCommand.S_C_Statistic:{
                // пришла статистика по играм
                model.setStatisticList(message.getStatisticList());
                model.setOffsetForStats(offset);
            } break;
            case MessageCommand.S_C_EmptyListOfLobbyGames:{
                // пришел пустой список игр
                ArrayList<ServerGame> emptyList = new ArrayList<ServerGame>();
                model.setListOFServersGames(emptyList);
            } break;
            case MessageCommand.S_C_DisconnectFromServer:{
                // по каким-то причинам мы отключились
                System.err.println("Обработал отключение");
                sender = null;  // переделать
                model.resetData();
            }break;
            case MessageCommand.S_C_SystemMessageStopTheThread:{

            }break;
            case MessageCommand.S_C_EmptyStat:{
                // пришел пустой список статистики
            }break;
        }
    }

    @Override
    public void buttonCreateGameHandler() {
        sender.sendMessage(new Message(MessageCommand.C_S_WantToCreateGame,"",""));
    }

    @Override
    public void buttonConnectToGameHandler(int indx) {
        ServerGame game = null;
        if (indx != -1)
            game = model.getListOFServersGames().get(indx);

        if (game != null)
            sender.sendMessage(new Message(MessageCommand.C_S_WantToConnectToGame,"" + game.getId(),""));
    }

    @Override
    public void buttonConnectToGameAsObsHandler(int indx) {
        ServerGame game = null;
        if (indx != -1)
            game = model.getListOFServersGames().get(indx);

        if (game != null)
            sender.sendMessage(new Message(MessageCommand.C_S_WantToObserverToGame, "" + game.getId(), ""));
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
            sender.sendMessage(new Message(MessageCommand.C_S_LeftFromTheGame,"",""));
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
            sender.sendMessage(new Message(MessageCommand.C_S_GamerReadyAndSendBoard,model.getGame().getHimselfGameBoard().toString(),""));
        }
        else{
            sender.sendMessage(new Message(MessageCommand.C_S_GamerNotReady,"",""));
        }
    }

    @Override
    public void startGameButtonHandler() {
        sender.sendMessage(new Message(MessageCommand.C_S_HostGamerStartTheGame, "",""));
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
            sender.sendMessage(new Message(MessageCommand.C_S_FireToCoord, "" + model.getCoordX(), "" + model.getCoordY()));
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
        sender.sendMessage(new Message(MessageCommand.C_S_NeedStatisticFromNumber,"",""));
    }

    @Override
    public void refreshStatisticHandler(int offset) {
        if (this.offset != offset){
            if(this.offset != -1)
                prevOffset = this.offset;

            sender.sendMessage(new Message(MessageCommand.C_S_NeedRefreshStatistic, "" + offset, ""));
            this.offset = offset;
        }
    }

    @Override
    public void disconnect() {
        if (model.getConnectionState() != IModel.ConnectionState.offline && sender != null)
            sender.sendMessage(new Message(MessageCommand.C_S_DisconnectFromServer, "", ""));
        model.resetData();
    }

    @Override
    public void buttonSendMessageToGameChat(String message) {
        sender.sendMessage(new Message(MessageCommand.C_S_MessageToLobbyFromlogin, model.getLogin(),message));
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
        sender.sendMessage(new Message(MessageCommand.C_S_GamerWantToLose,"",""));
    }

    @Override
    public void showMyStatsHandler(boolean isNowMyStatTurn) {
        if (isNowMyStatTurn) {

            sender.sendMessage(new Message(MessageCommand.C_S_WantStatAboutlogin, model.getLogin(), ""));
            if (prevOffset < offset)
                offset = prevOffset;
        }
        else {
            sender.sendMessage(new Message(MessageCommand.C_S_NeedRefreshStatistic, "" + offset, ""));
        }
    }

    @Override
    public void stopObservGameHandler() {
        sender.sendMessage(new Message(MessageCommand.C_S_StopObserveTheGame,"",""));
    }

    @Override
    public void disconnectFromLobby() {
        if (sender != null)
            sender.sendMessage(new Message(MessageCommand.C_S_ArmLeftTheLobby,"",""));
    }
}
