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
        numberOfCommand = 999;

    }


    public Message(int numberOfCommand, String substr1, String substr2){

        switch (numberOfCommand){
            case 100:{
                //От клиента к серверу команда пытаюсь залогиниться
                this.numberOfCommand = numberOfCommand;
                login = substr1;
                pass = substr2;
                isEmpty = false;
            } break;
            case 101:{
                //от сервера клиенту успешная валидация логин-пароля
                this.numberOfCommand = 101;
                login = substr1;
                pass = substr2;
                isEmpty = false;
            } break;
            case 102:{
                //от сервера клиенту не успешная валидация
                this.numberOfCommand = 102;
                isEmpty = false;
            } break;
            case 103:{
                //от клиента серверу пытаюсь войти в лобби для поиска игры
                this.numberOfCommand = 103;
                isEmpty = false;
            }break;
            case 104:{
                //от сервера клиенту разрешено войти в лобби
                this.numberOfCommand = 104;
                isEmpty = false;
            }break;
            case 105:{
                //от клиента серверу - сообщение в лобби-чат
                this.numberOfCommand = 105;
                this.login = substr1;
                this.message = substr2;
                isEmpty = false;
            } break;
            case 106:{
                //от сервера клиенту сообщение в лобби-чат от такого-то логина
                this.numberOfCommand = 106;
                this.login = substr1;
                this.message = substr2;
                isEmpty = false;
            }break;
            case 107:{
                //от сервера клиенту сообщение сервера (о подключении нового геймера к лобби)
                this.numberOfCommand = 107;
                this.login = "SERVER";
                this.message = substr1;
            }break;
            case 108:{
                //запрос регистрации нового польлзователя
                this.numberOfCommand = 108;
                this.login = substr1;
                this.pass = substr2;
            }break;
            case 109:{
                //Положительный ответ от сервера о регистрации
                this.numberOfCommand = 109;
                this.login = substr1;
                this.pass = substr2;
            }break;
            case 110:{
                //отрицательный ответ от сервера о регистрации
                this.numberOfCommand = 110;
            }break;
            case 111:{
                //запрос серверу на создание игры
                this.numberOfCommand = 111;
            }break;
            case 112:{
                //ответ от сервера на запрос создания игры
                this.numberOfCommand = 112;
            }break;
            case 113:{
                //запрос на подключение к игре
                this.numberOfCommand = 113;
                this.gameID = Integer.parseInt(substr1);
            }break;
            case 114:{
                //положительный ответ на подключение к игре
                this.numberOfCommand = 114;
                this.gameInfo = Decoder.ToGameInfoFromstring(substr1);
            } break;
            case 115:{
                //отрицательный ответ на подключение к игре
                this.numberOfCommand = 115;
            } break;
            case 116:{
                //покидаем лобби вручную
                this.numberOfCommand = 116;
            } break;
            case 117:{
                //сообщение хостовому игроку о том что к игре подключился новый игрок
                this.numberOfCommand = 117;
                this.login = substr1;
            } break;
            case 118:{
                // ответ на сообщение о том что игрок прервал инициализацию игры
                this.numberOfCommand = 118;
            } break;
            case 119:{
                // сообщение об изменении игорвых данных (Изм. кол-ва обсерверовб отключении оппонента, измении Готовности игроков.
                this.numberOfCommand = 119;
                this.gameInfo = Decoder.ToGameInfoFromstring(substr1);
            } break;
            case 120:{
                // сообщение от клиента что пользватель готов и посылает раскладку игрового поля
                this.numberOfCommand = 120;
                this.board = Decoder.StringToArray(substr1);
            } break;
            case 121:{
                // сообщение от клиента что он не готов и доска не соответствует действительной
                this.numberOfCommand = 121;
            } break;
            case 122:{
                // сообщение от клиента о старте игры
                this.numberOfCommand = 122;
            } break;
            case 123:{
                // сообщение первому игроку о начале игры (право первого хода у него)
                this.numberOfCommand = 123;
            } break;
            case 124:{
                // сообщение второму игроку о начале игры (без возможности выполнить ход)
                this.numberOfCommand = 124;
            } break;
            case 125:{
                // ответ сервера наблюдателю что можно смотреть за игрой
                this.numberOfCommand = 125;
            } break;
            case 126:{
                // сообщение серверу - стреляю по координатам!
                this.numberOfCommand = 126;
                coordX = Integer.parseInt(substr1);
                coordY = Integer.parseInt(substr2);
            }break;
            case 127:{
                // ответ сервера попал по координатам (ход не переходит)
                this.numberOfCommand = 127;
                coordX = Integer.parseInt(substr1);
                coordY = Integer.parseInt(substr2);
            } break;
            case 128:{
                // ответ сервера по координатам ()() - пусто  (ход переходит к оппоненту)
                this.numberOfCommand = 128;
                coordX = Integer.parseInt(substr1);
                coordY = Integer.parseInt(substr2);
            } break;
            case 129:{
                // сообщение сервера что по игроку стрельнули и попали (не его ход)
                this.numberOfCommand = 129;
                coordX = Integer.parseInt(substr1);
                coordY = Integer.parseInt(substr2);
            } break;
            case 130:{
                // сообщение сервера что по игроку стрельнули и промазали (его ход)
                this.numberOfCommand = 130;
                coordX = Integer.parseInt(substr1);
                coordY = Integer.parseInt(substr2);
            } break;
            case 131:{
                // ответ сервера что по указанным координатам "потопили" корабль
                this.numberOfCommand = 131;
                coordX = Integer.parseInt(substr1);
                coordY = Integer.parseInt(substr2);
            } break;
            case 132:{
                // уведомление сервера что ваш корабль содержащий такие координаты потоплен
                this.numberOfCommand = 132;
                coordX = Integer.parseInt(substr1);
                coordY = Integer.parseInt(substr2);
            } break;
            case 133:{
                // уведомление от сервера о выигрыше
                this.numberOfCommand = 133;
            } break;
            case 134:{
                // уведомление от сервера о проигрыше
                this.numberOfCommand = 134;
            } break;
            case 135:{
                // запрос статистики
                this.numberOfCommand = 135;
            }break;
            case 136:{
                // ответ от сервера - переход в режим отображения статистики
                this.numberOfCommand = 136;
            } break;
            case 137:{
                // запрос новой статистики от клиента
                this.numberOfCommand = 137;
                variableOne = Integer.parseInt(substr1);
            }break;
            case 138:{
                //от клиента серверу - сообщение в лобби-чат
                this.numberOfCommand = 138;
                this.login = substr1;
                this.message = substr2;
                isEmpty = false;
            } break;
            case 139:{
                //от сервера клиенту сообщение в лобби-чат от такого-то логина
                this.numberOfCommand = 139;
                this.login = substr1;
                this.message = substr2;
                isEmpty = false;
            }break;
            case 140:{
                //от сервера клиенту сообщение сервера (о подключении кого то к игре)
                this.numberOfCommand = 140;
                this.login = "SERVER";
                this.message = substr1;
            }break;
            case 141:{
                //от клиента серверу - запрос на подключение к игре в качестве обсервера
                this.numberOfCommand = 141;
                this.gameID = Integer.parseInt(substr1);
            }break;
            case 142:{
                // от сервера клиенту подключен в качестве обсервера
                this.numberOfCommand = 142;
                this.gameInfo = Decoder.ToGameInfoFromstring(substr1);
            }break;
            case 143:{
                // сообщение наблюдателям: такой-то логин попал по таким-то координатам
                this.numberOfCommand = 143;
                this.login = substr1;
                coordX = Integer.parseInt(substr2.substring(0,1));
                coordY = Integer.parseInt(substr2.substring(1,2));
            }break;
            case 144:{
                // сообщение наблюдателям: такой-то логин промазал по таким-то координатам
                this.numberOfCommand = 144;
                this.login = substr1;
                coordX = Integer.parseInt(substr2.substring(0,1));
                coordY = Integer.parseInt(substr2.substring(1,2));
            }break;
            case 145:{
                // сообщение наблюдателям: такой-то логин убил по таким-то координатам
                this.numberOfCommand = 145;
                this.login = substr1;
                coordX = Integer.parseInt(substr2.substring(0,1));
                coordY = Integer.parseInt(substr2.substring(1,2));
            }break;
            case 146:{
                // сообщение наблюдателям: такой-то логин выиграл
                this.numberOfCommand = 146;
                this.login = substr1;
            }break;
            case 147:{
                // сообщение с текущим состоянием игровых полей (для наблюдателя)
                this.numberOfCommand = 147;
                this.board = Decoder.StringToArray(substr1);
                this.board2 = Decoder.StringToArray(substr2);
            }break;
            case 148:{
                // к->с сообщение о том что игрок пожелал сдаться...
                this.numberOfCommand = 148;
            }break;
            case 149:{
                // запрос статистики по конкретному пользователю
                this.numberOfCommand = 149;
                this.login = substr1;
            }break;
            case 150:{
                // к->c ручной дисконнект от игры
                this.numberOfCommand = 150;
            }break;
            case 151:{
                // хостовый игрок отменил создание игры поэтому всем надо отключиться
                this.numberOfCommand = 151;
            }break;
            case 152:{
                // отключаемся как наблюдатели
                this.numberOfCommand = 152;
            }break;
            case 203:{
                this.numberOfCommand = 203;
            }break;
            case 300:{
                // сообщение серверу об отключении
                this.numberOfCommand = 300;
            } break;
            case 301:{
                // сообщение от сервера об отключении
                this.numberOfCommand = 301;
            } break;
            case 302:{
                // служебное сообщение от сервера чтобы просто завершить поток
                this.numberOfCommand = 302;
            } break;
            case 998:{
                // пустой список статистики
                this.numberOfCommand = 998;
            }break;
            default:{
                this.numberOfCommand = 999;
                isEmpty =true;
            }
        }
    }
    public Message(int numberOfCommand, ArrayList<String> list){
        switch (numberOfCommand){
            case 201:
                this.numberOfCommand = 201;
                this.listOfGame = list;
                break;
            case 202:
                this.numberOfCommand = 202;
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
