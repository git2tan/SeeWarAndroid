package com.example.seawar;

import java.util.ArrayList;

/**
 * Created by Artem on 07.05.2017.
 */
public class Message {
    private boolean isEmpty;
    private String login;
    private String pass;
    private String message;
    private int numberOfCommand;
    private int gameID;
    private ArrayList<String> listOfGame;
    private ArrayList<String> statisticList;
    private int observersCount;
    private GameInfo gameInfo;
    private int[][] board;
    private int [][] board2;
    private int coordX;
    private int coordY;
    private int variableOne;
    private String variable1;
    private String variable2;

    public Message(){
        isEmpty = true;
        numberOfCommand = MessageCommand.EmptyMessage;

    }


    public Message(int numberOfCommand, String substr1, String substr2){

        switch (numberOfCommand){
            case MessageCommand.C_S_TryLogin:{
                //От клиента к серверу команда пытаюсь залогиниться
                this.numberOfCommand = numberOfCommand;
                login = substr1;
                pass = substr2;
                isEmpty = false;
            } break;
            case MessageCommand.S_C_SuccessLogin:{
                //от сервера клиенту успешная валидация логин-пароля
                this.numberOfCommand = MessageCommand.S_C_SuccessLogin;
                login = substr1;
                pass = substr2;
                isEmpty = false;
            } break;
            case MessageCommand.S_C_InValidLogin:{
                //от сервера клиенту не успешная валидация
                this.numberOfCommand = MessageCommand.S_C_InValidLogin;
                isEmpty = false;
            } break;
            case MessageCommand.C_S_TryConnectToLobby:{
                //от клиента серверу пытаюсь войти в лобби для поиска игры
                this.numberOfCommand = MessageCommand.C_S_TryConnectToLobby;
                isEmpty = false;
            }break;
            case MessageCommand.S_C_YouAllowConnectToLobby:{
                //от сервера клиенту разрешено войти в лобби
                this.numberOfCommand = MessageCommand.S_C_YouAllowConnectToLobby;
                isEmpty = false;
            }break;
            case MessageCommand.C_S_MessageToLobby:{
                //от клиента серверу - сообщение в лобби-чат
                this.numberOfCommand = MessageCommand.C_S_MessageToLobby;
                this.login = substr1;
                this.message = substr2;
                isEmpty = false;
            } break;
            case MessageCommand.S_C_MessageToLobbyFromLogin:{
                //от сервера клиенту сообщение в лобби-чат от такого-то логина
                this.numberOfCommand = MessageCommand.S_C_MessageToLobbyFromLogin;
                this.login = substr1;
                this.message = substr2;
                isEmpty = false;
            }break;
            case MessageCommand.S_C_MessageToLobbyFromServer:{
                //от сервера клиенту сообщение сервера (о подключении нового геймера к лобби)
                this.numberOfCommand = MessageCommand.S_C_MessageToLobbyFromServer;
                this.login = "SERVER";
                this.message = substr1;
            }break;
            case MessageCommand.C_S_TryToRegisterNewLogin:{
                //запрос регистрации нового польлзователя
                this.numberOfCommand = MessageCommand.C_S_TryToRegisterNewLogin;
                this.login = substr1;
                this.pass = substr2;
            }break;
            case MessageCommand.S_C_RegistrationSuccess:{
                //Положительный ответ от сервера о регистрации
                this.numberOfCommand = MessageCommand.S_C_RegistrationSuccess;
                this.login = substr1;
                this.pass = substr2;
            }break;
            case MessageCommand.S_C_RegistrationNotSuccess:{
                //отрицательный ответ от сервера о регистрации
                this.numberOfCommand = MessageCommand.S_C_RegistrationNotSuccess;
            }break;
            case MessageCommand.C_S_WantToCreateGame:{
                //запрос серверу на создание игры
                this.numberOfCommand = MessageCommand.C_S_WantToCreateGame;
            }break;
            case MessageCommand.S_C_AllowToCreateGame:{
                //ответ от сервера на запрос создания игры
                this.numberOfCommand = MessageCommand.S_C_AllowToCreateGame;
            }break;
            case MessageCommand.C_S_WantToConnectToGame:{
                //запрос на подключение к игре
                this.numberOfCommand = MessageCommand.C_S_WantToConnectToGame;
                this.gameID = Integer.parseInt(substr1);
            }break;
            case MessageCommand.S_C_SuccessConnectToGame:{
                //положительный ответ на подключение к игре
                this.numberOfCommand = MessageCommand.S_C_SuccessConnectToGame;
                this.gameInfo = Decoder.ToGameInfoFromstring(substr1);
            } break;
            case MessageCommand.S_C_NotAllowConnectToGame:{
                //отрицательный ответ на подключение к игре
                this.numberOfCommand = MessageCommand.S_C_NotAllowConnectToGame;
            } break;
            case MessageCommand.C_S_ArmLeftTheLobby:{
                //покидаем лобби вручную
                this.numberOfCommand = MessageCommand.C_S_ArmLeftTheLobby;
            } break;
            case MessageCommand.S_C_ToHostGamer_NewGamerConnect:{
                //сообщение хостовому игроку о том что к игре подключился новый игрок
                this.numberOfCommand = MessageCommand.S_C_ToHostGamer_NewGamerConnect;
                this.login = substr1;
            } break;
            case MessageCommand.S_C_RequesttoArmDisconnectFromLobby:{
                // ответ на сообщение о том что игрок прервал инициализацию игры
                this.numberOfCommand = MessageCommand.S_C_RequesttoArmDisconnectFromLobby;
            } break;
            case MessageCommand.S_C_NewGameInfo:{
                // сообщение об изменении игорвых данных (Изм. кол-ва обсерверовб отключении оппонента, измении Готовности игроков.
                this.numberOfCommand = MessageCommand.S_C_NewGameInfo;
                this.gameInfo = Decoder.ToGameInfoFromstring(substr1);
            } break;
            case MessageCommand.C_S_GamerReadyAndSendBoard:{
                // сообщение от клиента что пользватель готов и посылает раскладку игрового поля
                this.numberOfCommand = MessageCommand.C_S_GamerReadyAndSendBoard;
                this.board = Decoder.StringToArray(substr1);
            } break;
            case MessageCommand.C_S_GamerNotReady:{
                // сообщение от клиента что он не готов и доска не соответствует действительной
                this.numberOfCommand = MessageCommand.C_S_GamerNotReady;
            } break;
            case MessageCommand.C_S_HostGamerStartTheGame:{
                // сообщение от клиента о старте игры
                this.numberOfCommand = MessageCommand.C_S_HostGamerStartTheGame;
            } break;
            case MessageCommand.S_C_ToHostGamerStartTheGame:{
                // сообщение первому игроку о начале игры (право первого хода у него)
                this.numberOfCommand = MessageCommand.S_C_ToHostGamerStartTheGame;
            } break;
            case MessageCommand.S_C_ToGamer2StartTheGame:{
                // сообщение второму игроку о начале игры (без возможности выполнить ход)
                this.numberOfCommand = MessageCommand.S_C_ToGamer2StartTheGame;
            } break;
            case MessageCommand.S_C_AllowObserveTheGame:{
                // ответ сервера наблюдателю что можно смотреть за игрой
                this.numberOfCommand = MessageCommand.S_C_AllowObserveTheGame;
            } break;
            case MessageCommand.C_S_FireToCoord:{
                // сообщение серверу - стреляю по координатам!
                this.numberOfCommand = MessageCommand.C_S_FireToCoord;
                coordX = Integer.parseInt(substr1);
                coordY = Integer.parseInt(substr2);
            }break;
            case MessageCommand.S_C_YouHitToCoord:{
                // ответ сервера попал по координатам (ход не переходит)
                this.numberOfCommand = MessageCommand.S_C_YouHitToCoord;
                coordX = Integer.parseInt(substr1);
                coordY = Integer.parseInt(substr2);
            } break;
            case MessageCommand.S_C_YouMissToCoord:{
                // ответ сервера по координатам ()() - пусто  (ход переходит к оппоненту)
                this.numberOfCommand = MessageCommand.S_C_YouMissToCoord;
                coordX = Integer.parseInt(substr1);
                coordY = Integer.parseInt(substr2);
            } break;
            case MessageCommand.S_C_OpponentHitToYou:{
                // сообщение сервера что по игроку стрельнули и попали (не его ход)
                this.numberOfCommand = MessageCommand.S_C_OpponentHitToYou;
                coordX = Integer.parseInt(substr1);
                coordY = Integer.parseInt(substr2);
            } break;
            case MessageCommand.S_C_OpponentMissToYou:{
                // сообщение сервера что по игроку стрельнули и промазали (его ход)
                this.numberOfCommand = MessageCommand.S_C_OpponentMissToYou;
                coordX = Integer.parseInt(substr1);
                coordY = Integer.parseInt(substr2);
            } break;
            case MessageCommand.S_C_YouDestroyTheShipByCoord:{
                // ответ сервера что по указанным координатам "потопили" корабль
                this.numberOfCommand = MessageCommand.S_C_YouDestroyTheShipByCoord;
                coordX = Integer.parseInt(substr1);
                coordY = Integer.parseInt(substr2);
            } break;
            case MessageCommand.S_C_YourShipByCoordIsDestroyed:{
                // уведомление сервера что ваш корабль содержащий такие координаты потоплен
                this.numberOfCommand = MessageCommand.S_C_YourShipByCoordIsDestroyed;
                coordX = Integer.parseInt(substr1);
                coordY = Integer.parseInt(substr2);
            } break;
            case MessageCommand.S_C_YouWin:{
                // уведомление от сервера о выигрыше
                this.numberOfCommand = MessageCommand.S_C_YouWin;
            } break;
            case MessageCommand.S_C_YouLose:{
                // уведомление от сервера о проигрыше
                this.numberOfCommand = MessageCommand.S_C_YouLose;
            } break;
            case MessageCommand.C_S_NeedStatisticFromNumber:{
                // запрос статистики
                this.numberOfCommand = MessageCommand.C_S_NeedStatisticFromNumber;
            }break;
            case MessageCommand.S_C_ShowStatActivity:{
                // ответ от сервера - переход в режим отображения статистики
                this.numberOfCommand = MessageCommand.S_C_ShowStatActivity;
            } break;
            case MessageCommand.C_S_NeedRefreshStatistic:{
                // запрос новой статистики от клиента
                this.numberOfCommand = MessageCommand.C_S_NeedRefreshStatistic;
                variableOne = Integer.parseInt(substr1);
            }break;
            case MessageCommand.C_S_MessageToLobbyFromlogin:{
                //от клиента серверу - сообщение в лобби-чат
                this.numberOfCommand = MessageCommand.C_S_MessageToLobbyFromlogin;
                this.login = substr1;
                this.message = substr2;
                isEmpty = false;
            } break;
            case MessageCommand.S_C_MessageToLobbyFromlogin:{
                //от сервера клиенту сообщение в лобби-чат от такого-то логина
                this.numberOfCommand = MessageCommand.S_C_MessageToLobbyFromlogin;
                this.login = substr1;
                this.message = substr2;
                isEmpty = false;
            }break;
            case MessageCommand.S_C_MessageToLobbyAboutCoonect:{
                //от сервера клиенту сообщение сервера (о подключении кого то к игре)
                this.numberOfCommand = MessageCommand.S_C_MessageToLobbyAboutCoonect;
                this.login = "SERVER";
                this.message = substr1;
            }break;
            case MessageCommand.C_S_WantToObserverToGame:{
                //от клиента серверу - запрос на подключение к игре в качестве обсервера
                this.numberOfCommand = MessageCommand.C_S_WantToObserverToGame;
                this.gameID = Integer.parseInt(substr1);
            }break;
            case MessageCommand.S_C_ShowObserverActivity:{
                // от сервера клиенту подключен в качестве обсервера
                this.numberOfCommand = MessageCommand.S_C_ShowObserverActivity;
                this.gameInfo = Decoder.ToGameInfoFromstring(substr1);
            }break;
            case MessageCommand.S_C_LoginFireToCoordAndHit:{
                // сообщение наблюдателям: такой-то логин попал по таким-то координатам
                this.numberOfCommand = MessageCommand.S_C_LoginFireToCoordAndHit;
                this.login = substr1;
                coordX = Integer.parseInt(substr2.substring(0,1));
                coordY = Integer.parseInt(substr2.substring(1,2));
            }break;
            case MessageCommand.S_C_ToObs_LoginFireToCoordAndMiss:{
                // сообщение наблюдателям: такой-то логин промазал по таким-то координатам
                this.numberOfCommand = MessageCommand.S_C_ToObs_LoginFireToCoordAndMiss;
                this.login = substr1;
                coordX = Integer.parseInt(substr2.substring(0,1));
                coordY = Integer.parseInt(substr2.substring(1,2));
            }break;
            case MessageCommand.S_C_ToObs_LoginDestroyShipByCoord:{
                // сообщение наблюдателям: такой-то логин убил по таким-то координатам
                this.numberOfCommand = MessageCommand.S_C_ToObs_LoginDestroyShipByCoord;
                this.login = substr1;
                coordX = Integer.parseInt(substr2.substring(0,1));
                coordY = Integer.parseInt(substr2.substring(1,2));
            }break;
            case MessageCommand.S_C_ToObs_LoginWin:{
                // сообщение наблюдателям: такой-то логин выиграл
                this.numberOfCommand = MessageCommand.S_C_ToObs_LoginWin;
                this.login = substr1;
            }break;
            case MessageCommand.S_C_ToObs_ActualGameInfo:{
                // сообщение с текущим состоянием игровых полей (для наблюдателя)
                this.numberOfCommand = MessageCommand.S_C_ToObs_ActualGameInfo;
                this.board = Decoder.StringToArray(substr1);
                this.board2 = Decoder.StringToArray(substr2);
            }break;
            case MessageCommand.C_S_GamerWantToLose:{
                // к->с сообщение о том что игрок пожелал сдаться...
                this.numberOfCommand = MessageCommand.C_S_GamerWantToLose;
            }break;
            case MessageCommand.C_S_WantStatAboutlogin:{
                // запрос статистики по конкретному пользователю
                this.numberOfCommand = MessageCommand.C_S_WantStatAboutlogin;
                this.login = substr1;
            }break;
            case MessageCommand.C_S_LeftFromTheGame:{
                // к->c ручной дисконнект от игры
                this.numberOfCommand = MessageCommand.C_S_LeftFromTheGame;
            }break;
            case MessageCommand.S_C_HostLeftTheGame:{
                // хостовый игрок отменил создание игры поэтому всем надо отключиться
                this.numberOfCommand = MessageCommand.S_C_HostLeftTheGame;
            }break;
            case MessageCommand.C_S_StopObserveTheGame:{
                // отключаемся как наблюдатели
                this.numberOfCommand = MessageCommand.C_S_StopObserveTheGame;
            }break;
            case MessageCommand.S_C_EmptyListOfLobbyGames:{
                this.numberOfCommand = MessageCommand.S_C_EmptyListOfLobbyGames;
            }break;
            case MessageCommand.C_S_DisconnectFromServer:{
                // сообщение серверу об отключении
                this.numberOfCommand = MessageCommand.C_S_DisconnectFromServer;
            } break;
            case MessageCommand.S_C_DisconnectFromServer:{
                // сообщение от сервера об отключении
                this.numberOfCommand = MessageCommand.S_C_DisconnectFromServer;
            } break;
            case MessageCommand.S_C_SystemMessageStopTheThread:{
                // служебное сообщение от сервера чтобы просто завершить поток
                this.numberOfCommand = MessageCommand.S_C_SystemMessageStopTheThread;
            } break;
            case MessageCommand.S_C_EmptyStat:{
                // пустой список статистики
                this.numberOfCommand = MessageCommand.S_C_EmptyStat;
            }break;
            default:{
                this.numberOfCommand = MessageCommand.EmptyMessage;
                isEmpty =true;
            }
        }
    }
    public Message(int numberOfCommand, ArrayList<String> list){
        switch (numberOfCommand){
            case MessageCommand.S_C_ListOfLobbyGame:
                this.numberOfCommand = MessageCommand.S_C_ListOfLobbyGame;
                this.listOfGame = list;
                break;
            case MessageCommand.S_C_Statistic:
                this.numberOfCommand = MessageCommand.S_C_Statistic;
                this.statisticList = list;
                break;
        }
    }


    public String getLogin() {
        return login;
    }
    public String getLoginLength(){
        String tmp;
        if(login.length() < 10){
            tmp = "0" + login.length();
        }else{
            tmp = "" + login.length();
        }
        return tmp;
    }
    public String getPassLength(){
        String tmp;
        if(pass.length() < 10){
            tmp = "0" + pass.length();
        }else{
            tmp = "" +pass.length();
        }
        return tmp;
    }
    public String getPass() {
        return pass;
    }
    public String getMessage() {
        return message;
    }
    public boolean isEmpty()
    {
        return isEmpty;
    }
    public int getNumberOfCommand(){
        return this.numberOfCommand;
    }
    public ArrayList<String> getListOfGame(){
        return listOfGame;
    }
    public ArrayList<String> getStatisticList(){
        return statisticList;
    }

    public int getGameID() {
        return gameID;
    }

    public int getObserversCount() {
        return observersCount;
    }
    public GameInfo getGameInfo(){
        return gameInfo;
    }

    public int[][] getBoard() {
        return board;
    }
    public int[][] getBoard2() {return board2;}
    public int getCoordX() {return coordX;}
    public int getCoordY(){return coordY;}
    public int getVariableOne(){return variableOne;}

    public String getVariable1(){return variable1;}
    public String getVariable2(){return variable2;}
}
