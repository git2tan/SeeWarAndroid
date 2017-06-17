package com.example.seawar;

import java.util.ArrayList;
import java.util.Vector;

/**
 * Created by Artem on 07.05.2017.
 */
public class Decoder {
    public Decoder(){

    }
    //Для нужд клиента
    public static ServerGame decodeStringToServerGame(String input){
        int simPtr = 0;
        int id = Integer.parseInt(input.substring(simPtr,simPtr + 6));
        simPtr += 6;
        int loginLength = Integer.parseInt(input.substring(simPtr,simPtr+2));
        simPtr += 2;
        String login = input.substring(simPtr,simPtr+loginLength);
        simPtr += loginLength;
        boolean isFull = (input.substring(simPtr,simPtr + 1)).equals("1");
        simPtr += 1;
        int observerCount = Integer.parseInt(input.substring(simPtr));
        return new ServerGame(id,login,isFull,observerCount);

    }

    public static Vector<String> ListToVector(ArrayList<ServerGame> list){
        Vector<String> vec = new Vector<String>();
        for(ServerGame one : list)
            vec.add(one.toString());

        return vec;
    }

    ////////////ОБЩЕЕ//////////////////////////////////////////////

    public static int[][] StringToArray(String input){
        int[][] array = new int[10][10];
        int ptr = 0;
        for(int i = 0; i < 10; i++){
            for(int j = 0; j < 10; j++){
                array[i][j] = Integer.parseInt(input.substring(ptr,ptr+1));
                ptr++;
            }
        }
        return array;
    }

    public static GameInfo ToGameInfoFromstring(String input){
        int simPtr = 0;
        int id = Integer.parseInt(input.substring(simPtr,simPtr + 6));
        simPtr += 6;
        int loginLength = Integer.parseInt(input.substring(simPtr,simPtr+2));
        simPtr += 2;
        String login = input.substring(simPtr,simPtr+loginLength);
        simPtr += loginLength;
        boolean isReady1 = (input.substring(simPtr,simPtr + 1)).equals("1");
        simPtr += 1;
        boolean isFull = (input.substring(simPtr,simPtr + 1)).equals("1");
        simPtr += 1;
        String login2 = "";
        boolean isReady2 = false;
        if(isFull)
        {
            int loginLength2 = Integer.parseInt(input.substring(simPtr,simPtr+2));
            simPtr += 2;
            login2 = input.substring(simPtr,simPtr+loginLength2);
            simPtr += loginLength2;
            isReady2 = (input.substring(simPtr,simPtr + 1)).equals("1");
            simPtr += 1;
        }
        int observerCount = Integer.parseInt(input.substring(simPtr));
        return new GameInfo(id,login,isReady1,login2,isReady2,observerCount);
    }

    public static String ArrayToString(int[][] array){
        String answer = "";
        for(int i = 0; i < 10; i++){
            for(int j = 0; j < 10; j++){
                answer += array[i][j] == 0?"0":"1";
            }
        }
        return answer;
    }
    public static String boardWithShotsToString(int[][] array){
        String answer = "";
        for (int i = 0; i < 10; i++){
            for(int j = 0; j < 10; j++){
                answer += "" + array[i][j];
            }
        }
        return answer;
    }


    public Message decodeString(String input){
        String s = input.substring(0,3);
        int number = Integer.parseInt(s);
        Message tmp;
        switch (number){
            case 100:{
                int loginLength = Integer.parseInt(input.substring(3,5));
                int passwordLength = Integer.parseInt(input.substring(5,7));
                String login = input.substring(7,7+loginLength);
                String password = input.substring(7+loginLength,7+loginLength+passwordLength);
                tmp = new Message(100,login,password);
            } break;
            case 101:{
                int loginLength = Integer.parseInt(input.substring(3,5));
                int passwordLength = Integer.parseInt(input.substring(5,7));
                String login = input.substring(7,7+loginLength);
                String password = input.substring(7+loginLength,7+loginLength+passwordLength);
                tmp = new Message(101,login,password);
            } break;
            case 102:{
                tmp = new Message(102,"","");
            } break;
            case 103:{
                tmp = new Message(103,"","");
            } break;
            case 104:{
                tmp = new Message(104,"","");
            } break;
            case 105:{
                int loginLength = Integer.parseInt(input.substring(3,5));
                String login = input.substring(5,5+loginLength);
                String message = input.substring(5+loginLength);
                tmp = new Message(105,login,message);
            } break;
            case 106:{
                int loginLength = Integer.parseInt(input.substring(3,5));
                String login = input.substring(5,5+loginLength);
                String message = input.substring(5+loginLength);
                tmp = new Message(106,login,message);
            } break;
            case 107:{
                String message = input.substring(3);
                tmp = new Message(107,message,"");
            } break;
            case 108:{
                int loginLength = Integer.parseInt(input.substring(3,5));
                int passwordLength = Integer.parseInt(input.substring(5,7));
                String login = input.substring(7,7+loginLength);
                String password = input.substring(7+loginLength,7+loginLength+passwordLength);
                tmp = new Message(108,login,password);
            } break;
            case 109:{
                int loginLength = Integer.parseInt(input.substring(3,5));
                int passwordLength = Integer.parseInt(input.substring(5,7));
                String login = input.substring(7,7+loginLength);
                String password = input.substring(7+loginLength,7+loginLength+passwordLength);
                tmp = new Message(109,login,password);
            } break;
            case 110:{
                tmp = new Message(110,"","");
            } break;
            case 111:{
                tmp = new Message(111, "", "");
            } break;
            case 112:{
                tmp = new Message(112,"","");
            } break;
            case 113:{
                int id = Integer.parseInt(input.substring(3));
                tmp = new Message(113, "" + id,"");
            } break;
            case 114:{
                tmp = new Message(114, input.substring(3),"");
            } break;
            case 115:{
                tmp = new Message(115,"","");
            }break;
            case 116:{
                tmp = new Message(116,"","");
            }break;
            case 117:{
                int loginLength = Integer.parseInt(input.substring(3,5));
                String login = input.substring(5,5+loginLength);
                tmp = new Message(117,login,"");
            }break;
            case 118:{
                tmp = new Message(118,"","");
            } break;
            case 119:{
                tmp = new Message(119, input.substring(3),"");
            } break;
            case 120:{
                tmp = new Message(120, input.substring(3),"");
            } break;
            case 121:{
                tmp = new Message(121,"","");
            } break;
            case 122:{
                tmp = new Message(122,"","");
            } break;
            case 123:{
                tmp = new Message(123,"","");
            } break;
            case 124:{
                tmp = new Message(124, "","");
            } break;
            case 125:{
                tmp = new Message(125, "","");
            } break;
            case 126:{
                tmp = new Message(126, input.substring(3,4), input.substring(4,5));
            } break;
            case 127:{
                tmp = new Message(127, input.substring(3,4), input.substring(4,5));
            } break;
            case 128:{
                tmp = new Message(128, input.substring(3,4), input.substring(4,5));
            } break;
            case 129:{
                tmp = new Message(129, input.substring(3,4), input.substring(4,5));
            } break;
            case 130:{
                tmp = new Message(130, input.substring(3,4), input.substring(4,5));
            } break;
            case 131:{
                tmp = new Message(131, input.substring(3,4), input.substring(4,5));
            }break;
            case 132:{
                tmp = new Message(132, input.substring(3,4), input.substring(4,5));
            }break;
            case 133:{
                tmp = new Message(133, "","");
            }break;
            case 134:{
                tmp = new Message(134, "","");
            }break;
            case 135:{
                tmp = new Message(135, "","");
            }break;
            case 136:{
                tmp = new Message(136,"","");
            }break;
            case 137:{
                tmp = new Message(137, input.substring(3),"");
            }break;
            case 138:{
                int loginLength = Integer.parseInt(input.substring(3,5));
                String login = input.substring(5,5+loginLength);
                String message = input.substring(5+loginLength);
                tmp = new Message(138,login,message);
            }break;
            case 139:{
                int loginLength = Integer.parseInt(input.substring(3,5));
                String login = input.substring(5,5+loginLength);
                String message = input.substring(5+loginLength);
                tmp = new Message(139, login, message);
            }break;
            case 140:{
                String message = input.substring(3);
                tmp = new Message(140,message,"");
            }break;
            case 141:{
                int id = Integer.parseInt(input.substring(3));
                tmp = new Message(141, "" + id,"");
            }break;
            case 142:{
                tmp = new Message(142, input.substring(3), "");
            }break;
            case 143:{
                int loginLength = Integer.parseInt(input.substring(3,5));
                String login = input.substring(5, 5+loginLength);
                tmp = new Message(143, login, input.substring(5+loginLength));
            }break;
            case 144:{
                int loginLength = Integer.parseInt(input.substring(3, 5));
                String login = input.substring(5, 5+loginLength);
                tmp = new Message(144, login, input.substring(5+loginLength));
            }break;
            case 145:{
                int loginLength = Integer.parseInt(input.substring(3,5));
                String login = input.substring(5, 5+loginLength);
                tmp = new Message(145, login, input.substring(5+loginLength));
            }break;
            case 146:{
                tmp = new Message(146, input.substring(3), "");
            }break;
            case 147:{
                tmp = new Message(147, input.substring(3,103), input.substring(103,203));
            }break;
            case 148:{
                tmp = new Message (148, "", "");
            }break;
            case 149:{
                int loginLength = Integer.parseInt(input.substring(3,5));
                String login = input.substring(5,5+loginLength);
                tmp = new Message(149, login, input.substring(5+loginLength));
            }break;
            case 150:{
                tmp = new Message(150,"","");
            }break;
            case 151:{
                tmp = new Message(151,"","");
            }break;
            case 152:{
                tmp = new Message(152,"","");
            }break;
            case 201:{
                tmp = toMessageStringWithGames(input);
            } break;
            case 202:{
                tmp = toMessageStringWithStatistic(input);
            }break;
            case 203:{
                tmp = new Message(203,"","");
            }break;
            case 300:{
                tmp = new Message(300, "", "");
            }break;
            case 301:{
                tmp = new Message(301, "", "");
            }break;
            case 302:{
                tmp = new Message(302, "", "");
            }break;
            case 998:{
                tmp = new Message(998,"","");
            }break;
            default:{
                tmp = new Message();
            }
        }

        return tmp;
    }

    public String codeMessage(Message message){
        String textMessage ="";
        if(!message.isEmpty()){
            switch (message.getNumberOfCommand()){
                case 100:{
                    textMessage += "100";
                    textMessage += message.getLoginLength();
                    textMessage += message.getPassLength();
                    textMessage += message.getLogin();
                    textMessage += message.getPass();
                }break;
                case 101:{
                    textMessage += "101";
                    textMessage += message.getLoginLength();
                    textMessage += message.getPassLength();
                    textMessage += message.getLogin();
                    textMessage += message.getPass();
                }break;
                case 102:{
                    textMessage += "102";
                }break;
                case 103:{
                    textMessage += "103";
                }break;
                case 104:{
                    textMessage += "104";
                }break;
                case 105:{
                    textMessage += "105";
                    textMessage += message.getLoginLength();
                    textMessage += message.getLogin();
                    textMessage += message.getMessage();
                }break;
                case 106:{
                    textMessage += "106";
                    textMessage += message.getLoginLength();
                    textMessage += message.getLogin();
                    textMessage += message.getMessage();
                } break;
                case 107:{
                    textMessage += "107";
                    textMessage += message.getMessage();
                } break;
                case 108:{
                    textMessage += "108";
                    textMessage += message.getLoginLength();
                    textMessage += message.getPassLength();
                    textMessage += message.getLogin();
                    textMessage += message.getPass();
                }break;
                case 109:{
                    textMessage += "109";
                    textMessage += message.getLoginLength();
                    textMessage += message.getPassLength();
                    textMessage += message.getLogin();
                    textMessage += message.getPass();
                }break;
                case 110:{
                    textMessage += "110";
                }break;
                case 111:{
                    textMessage += "111";
                }break;
                case 112:{
                    textMessage += "112";
                }break;
                case 113:{
                    textMessage += "113";
                    textMessage += message.getGameID();
                }break;
                case 114:{
                    textMessage += "114";
                    textMessage += message.getGameInfo().toString();
                }break;
                case 115:{
                    textMessage += "115";
                }break;
                case 116:{
                    textMessage += "116";
                }break;
                case 117:{
                    textMessage += "117";
                    textMessage += message.getLoginLength();
                    textMessage += message.getLogin();
                }break;
                case 118:{
                    textMessage += "118";
                }break;
                case 119:{
                    textMessage += "119";
                    textMessage += message.getGameInfo().toString();
                }break;
                case 120:{
                    textMessage += "120";
                    textMessage += Decoder.ArrayToString(message.getBoard());
                }break;
                case 121:{
                    textMessage += "121";
                }break;
                case 122:{
                    textMessage += "122";
                } break;
                case 123:{
                    textMessage += "123";
                } break;
                case 124:{
                    textMessage += "124";
                } break;
                case 125:{
                    textMessage += "125";
                } break;
                case 126:{
                    textMessage += "126";
                    textMessage += "" + message.getCoordX();
                    textMessage += "" + message.getCoordY();
                } break;
                case 127:{
                    textMessage += "127";
                    textMessage += "" + message.getCoordX();
                    textMessage += "" + message.getCoordY();
                } break;
                case 128:{
                    textMessage += "128";
                    textMessage += "" + message.getCoordX();
                    textMessage += "" + message.getCoordY();
                } break;
                case 129:{
                    textMessage += "129";
                    textMessage += "" + message.getCoordX();
                    textMessage += "" + message.getCoordY();
                } break;
                case 130:{
                    textMessage += "130";
                    textMessage += "" + message.getCoordX();
                    textMessage += "" + message.getCoordY();
                } break;
                case 131:{
                    // ответ сервера что по указанным координатам "потопили" корабль
                    textMessage += "131";
                    textMessage += "" + message.getCoordX();
                    textMessage += "" + message.getCoordY();
                } break;
                case 132:{
                    // уведомление сервера что ваш корабль содержащий такие координаты потоплен
                    textMessage += "132";
                    textMessage += "" + message.getCoordX();
                    textMessage += "" + message.getCoordY();
                } break;
                case 133:{
                    textMessage += "133";
                }break;
                case 134:{
                    textMessage += "134";
                }break;
                case 135:{
                    textMessage += "135";
                }break;
                case 136:{
                    textMessage += "136";
                }break;
                case 137:{
                    textMessage += "137";
                    textMessage += "" + message.getVariableOne();
                }break;
                case 138:{
                    //от клиента серверу - сообщение в лобби-чат
                    textMessage += "138";
                    textMessage += message.getLoginLength();
                    textMessage += message.getLogin();
                    textMessage += message.getMessage();
                }break;
                case 139:{
                    textMessage += "139";
                    textMessage += message.getLoginLength();
                    textMessage += message.getLogin();
                    textMessage += message.getMessage();
                }break;
                case 140:{
                    textMessage += "140";
                    textMessage += message.getMessage();
                }break;
                case 141:{
                    textMessage += "141";
                    textMessage += message.getGameID();
                }break;
                case 142:{
                    textMessage += "142";
                    textMessage += message.getGameInfo().toString();
                }break;
                case 143:{
                    textMessage += "143";
                    textMessage += message.getLoginLength();
                    textMessage += message.getLogin();
                    textMessage += "" + message.getCoordX();
                    textMessage += "" + message.getCoordY();
                }break;
                case 144:{
                    textMessage += "144";
                    textMessage += message.getLoginLength();
                    textMessage += message.getLogin();
                    textMessage += "" + message.getCoordX();
                    textMessage += "" + message.getCoordY();
                }break;
                case 145:{
                    textMessage += "145";
                    textMessage += message.getLoginLength();
                    textMessage += message.getLogin();
                    textMessage += "" + message.getCoordX();
                    textMessage += "" + message.getCoordY();
                }break;
                case 146:{
                    textMessage += "146";
                    textMessage += message.getLogin();
                }break;
                case 147:{
                    textMessage += "147";
                    textMessage += Decoder.boardWithShotsToString(message.getBoard());
                    textMessage += Decoder.boardWithShotsToString(message.getBoard2());
                }break;
                case 148:{
                    textMessage += "148";
                }break;
                case 149:{
                    textMessage += "149";
                    textMessage += message.getLoginLength();
                    textMessage += message.getLogin();
                }break;
                case 150:{
                    textMessage += "150";
                }break;
                case 151:{
                    textMessage += "151";
                }break;
                case 152:{
                    textMessage += "152";
                }break;
                case 201:{
                    textMessage = toStringMessageWithGames(message);
                }break;
                case 202:{
                    textMessage = toStringMessageOfStatistic(message);
                }break;
                case 203:{
                    textMessage = "203";
                }break;
                case 300:{
                    textMessage += "300";
                }break;
                case 301:{
                    textMessage += "301";
                }break;
                case 302:{
                    textMessage += "302";
                }break;
                case 998:{
                    textMessage += "998";
                }break;
                default:{
                    textMessage ="";
                }
            }
        }

        return textMessage;
    }



    private String toStringMessageWithGames(Message message){
        if(!message.getListOfGame().isEmpty()) {
            String result = "";
            result += 201;
            ArrayList<String> list = message.getListOfGame();
            String plus = String.format("%04d", list.size());
            result += plus;
            for (String one : list) {
                String add = String.format("%03d", one.length());
                result += add;
                result += one;
            }
            return result;
        }
        else return "998";
    }
    private Message toMessageStringWithGames(String input){
        int countOfGames = Integer.parseInt(input.substring(3,7));
        ArrayList<String> resultArray = new ArrayList<String>();
        int i = 7; //начальный символ с которого считываем игры
        while(countOfGames > 0){
            int length = Integer.parseInt(input.substring(i,i+3));
            i += 3;
            String tmp = input.substring(i,i + length);
            resultArray.add(tmp);
            i += length;
            countOfGames--;
        }

        return new Message(201, resultArray);
    }

    private String toStringMessageOfStatistic(Message message){
        if(!message.getStatisticList().isEmpty()){
            String result = "";
            result += 202;
            ArrayList<String> list = message.getStatisticList();
            String plus = String.format("%02d", list.size());
            result += plus;
            for (String one : list){
                String add = String.format("%03d", one.length());
                result += add;
                result += one;
            }
            return result;
        }
        return "998";
    }

    private Message toMessageStringWithStatistic(String input){
        int countOfStatisticItem = Integer.parseInt(input.substring(3,5));
        ArrayList<String> resultArray = new ArrayList<String>();
        int i = 5; //начальный символ с которого считываем статистику
        while (countOfStatisticItem > 0){
            int length = Integer.parseInt(input.substring(i, i + 3));
            i+=3;
            String tmp = input.substring(i, i + length);
            resultArray.add(tmp);
            i+=length;
            countOfStatisticItem--;
        }
        return new Message(202,resultArray);
    }
}