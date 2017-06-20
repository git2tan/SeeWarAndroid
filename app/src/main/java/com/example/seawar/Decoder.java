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
            case MessageCommand.C_S_TryLogin:{
                int loginLength = Integer.parseInt(input.substring(3,5));
                int passwordLength = Integer.parseInt(input.substring(5,7));
                String login = input.substring(7,7+loginLength);
                String password = input.substring(7+loginLength,7+loginLength+passwordLength);
                tmp = new Message(MessageCommand.C_S_TryLogin,login,password);
            } break;
            case MessageCommand.S_C_SuccessLogin:{
                int loginLength = Integer.parseInt(input.substring(3,5));
                int passwordLength = Integer.parseInt(input.substring(5,7));
                String login = input.substring(7,7+loginLength);
                String password = input.substring(7+loginLength,7+loginLength+passwordLength);
                tmp = new Message(MessageCommand.S_C_SuccessLogin,login,password);
            } break;
            case MessageCommand.S_C_InValidLogin:{
                tmp = new Message(MessageCommand.S_C_InValidLogin,"","");
            } break;
            case MessageCommand.C_S_TryConnectToLobby:{
                tmp = new Message(MessageCommand.C_S_TryConnectToLobby,"","");
            } break;
            case MessageCommand.S_C_YouAllowConnectToLobby:{
                tmp = new Message(MessageCommand.S_C_YouAllowConnectToLobby,"","");
            } break;
            case MessageCommand.C_S_MessageToLobby:{
                int loginLength = Integer.parseInt(input.substring(3,5));
                String login = input.substring(5,5+loginLength);
                String message = input.substring(5+loginLength);
                tmp = new Message(MessageCommand.C_S_MessageToLobby,login,message);
            } break;
            case MessageCommand.S_C_MessageToLobbyFromLogin:{
                int loginLength = Integer.parseInt(input.substring(3,5));
                String login = input.substring(5,5+loginLength);
                String message = input.substring(5+loginLength);
                tmp = new Message(MessageCommand.S_C_MessageToLobbyFromLogin,login,message);
            } break;
            case MessageCommand.S_C_MessageToLobbyFromServer:{
                String message = input.substring(3);
                tmp = new Message(MessageCommand.S_C_MessageToLobbyFromServer,message,"");
            } break;
            case MessageCommand.C_S_TryToRegisterNewLogin:{
                int loginLength = Integer.parseInt(input.substring(3,5));
                int passwordLength = Integer.parseInt(input.substring(5,7));
                String login = input.substring(7,7+loginLength);
                String password = input.substring(7+loginLength,7+loginLength+passwordLength);
                tmp = new Message(MessageCommand.C_S_TryToRegisterNewLogin,login,password);
            } break;
            case MessageCommand.S_C_RegistrationSuccess:{
                int loginLength = Integer.parseInt(input.substring(3,5));
                int passwordLength = Integer.parseInt(input.substring(5,7));
                String login = input.substring(7,7+loginLength);
                String password = input.substring(7+loginLength,7+loginLength+passwordLength);
                tmp = new Message(MessageCommand.S_C_RegistrationSuccess,login,password);
            } break;
            case MessageCommand.S_C_RegistrationNotSuccess:{
                tmp = new Message(MessageCommand.S_C_RegistrationNotSuccess,"","");
            } break;
            case MessageCommand.C_S_WantToCreateGame:{
                tmp = new Message(MessageCommand.C_S_WantToCreateGame, "", "");
            } break;
            case MessageCommand.S_C_AllowToCreateGame:{
                tmp = new Message(MessageCommand.S_C_AllowToCreateGame,"","");
            } break;
            case MessageCommand.C_S_WantToConnectToGame:{
                int id = Integer.parseInt(input.substring(3));
                tmp = new Message(MessageCommand.C_S_WantToConnectToGame, "" + id,"");
            } break;
            case MessageCommand.S_C_SuccessConnectToGame:{
                tmp = new Message(MessageCommand.S_C_SuccessConnectToGame, input.substring(3),"");
            } break;
            case MessageCommand.S_C_NotAllowConnectToGame:{
                tmp = new Message(MessageCommand.S_C_NotAllowConnectToGame,"","");
            }break;
            case MessageCommand.C_S_ArmLeftTheLobby:{
                tmp = new Message(MessageCommand.C_S_ArmLeftTheLobby,"","");
            }break;
            case MessageCommand.S_C_ToHostGamer_NewGamerConnect:{
                int loginLength = Integer.parseInt(input.substring(3,5));
                String login = input.substring(5,5+loginLength);
                tmp = new Message(MessageCommand.S_C_ToHostGamer_NewGamerConnect,login,"");
            }break;
            case MessageCommand.S_C_RequesttoArmDisconnectFromLobby:{
                tmp = new Message(MessageCommand.S_C_RequesttoArmDisconnectFromLobby,"","");
            } break;
            case MessageCommand.S_C_NewGameInfo:{
                tmp = new Message(MessageCommand.S_C_NewGameInfo, input.substring(3),"");
            } break;
            case MessageCommand.C_S_GamerReadyAndSendBoard:{
                tmp = new Message(MessageCommand.C_S_GamerReadyAndSendBoard, input.substring(3),"");
            } break;
            case MessageCommand.C_S_GamerNotReady:{
                tmp = new Message(MessageCommand.C_S_GamerNotReady,"","");
            } break;
            case MessageCommand.C_S_HostGamerStartTheGame:{
                tmp = new Message(MessageCommand.C_S_HostGamerStartTheGame,"","");
            } break;
            case MessageCommand.S_C_ToHostGamerStartTheGame:{
                tmp = new Message(MessageCommand.S_C_ToHostGamerStartTheGame,"","");
            } break;
            case MessageCommand.S_C_ToGamer2StartTheGame:{
                tmp = new Message(MessageCommand.S_C_ToGamer2StartTheGame, "","");
            } break;
            case MessageCommand.S_C_AllowObserveTheGame:{
                tmp = new Message(MessageCommand.S_C_AllowObserveTheGame, "","");
            } break;
            case MessageCommand.C_S_FireToCoord:{
                tmp = new Message(MessageCommand.C_S_FireToCoord, input.substring(3,4), input.substring(4,5));
            } break;
            case MessageCommand.S_C_YouHitToCoord:{
                tmp = new Message(MessageCommand.S_C_YouHitToCoord, input.substring(3,4), input.substring(4,5));
            } break;
            case MessageCommand.S_C_YouMissToCoord:{
                tmp = new Message(MessageCommand.S_C_YouMissToCoord, input.substring(3,4), input.substring(4,5));
            } break;
            case MessageCommand.S_C_OpponentHitToYou:{
                tmp = new Message(MessageCommand.S_C_OpponentHitToYou, input.substring(3,4), input.substring(4,5));
            } break;
            case MessageCommand.S_C_OpponentMissToYou:{
                tmp = new Message(MessageCommand.S_C_OpponentMissToYou, input.substring(3,4), input.substring(4,5));
            } break;
            case MessageCommand.S_C_YouDestroyTheShipByCoord:{
                tmp = new Message(MessageCommand.S_C_YouDestroyTheShipByCoord, input.substring(3,4), input.substring(4,5));
            }break;
            case MessageCommand.S_C_YourShipByCoordIsDestroyed:{
                tmp = new Message(MessageCommand.S_C_YourShipByCoordIsDestroyed, input.substring(3,4), input.substring(4,5));
            }break;
            case MessageCommand.S_C_YouWin:{
                tmp = new Message(MessageCommand.S_C_YouWin, "","");
            }break;
            case MessageCommand.S_C_YouLose:{
                tmp = new Message(MessageCommand.S_C_YouLose, "","");
            }break;
            case MessageCommand.C_S_NeedStatisticFromNumber:{
                tmp = new Message(MessageCommand.C_S_NeedStatisticFromNumber, "","");
            }break;
            case MessageCommand.S_C_ShowStatActivity:{
                tmp = new Message(MessageCommand.S_C_ShowStatActivity,"","");
            }break;
            case MessageCommand.C_S_NeedRefreshStatistic:{
                tmp = new Message(MessageCommand.C_S_NeedRefreshStatistic, input.substring(3),"");
            }break;
            case MessageCommand.C_S_MessageToLobbyFromlogin:{
                int loginLength = Integer.parseInt(input.substring(3,5));
                String login = input.substring(5,5+loginLength);
                String message = input.substring(5+loginLength);
                tmp = new Message(MessageCommand.C_S_MessageToLobbyFromlogin,login,message);
            }break;
            case MessageCommand.S_C_MessageToLobbyFromlogin:{
                int loginLength = Integer.parseInt(input.substring(3,5));
                String login = input.substring(5,5+loginLength);
                String message = input.substring(5+loginLength);
                tmp = new Message(MessageCommand.S_C_MessageToLobbyFromlogin, login, message);
            }break;
            case MessageCommand.S_C_MessageToLobbyAboutCoonect:{
                String message = input.substring(3);
                tmp = new Message(MessageCommand.S_C_MessageToLobbyAboutCoonect,message,"");
            }break;
            case MessageCommand.C_S_WantToObserverToGame:{
                int id = Integer.parseInt(input.substring(3));
                tmp = new Message(MessageCommand.C_S_WantToObserverToGame, "" + id,"");
            }break;
            case MessageCommand.S_C_ShowObserverActivity:{
                tmp = new Message(MessageCommand.S_C_ShowObserverActivity, input.substring(3), "");
            }break;
            case MessageCommand.S_C_LoginFireToCoordAndHit:{
                int loginLength = Integer.parseInt(input.substring(3,5));
                String login = input.substring(5, 5+loginLength);
                tmp = new Message(MessageCommand.S_C_LoginFireToCoordAndHit, login, input.substring(5+loginLength));
            }break;
            case MessageCommand.S_C_ToObs_LoginFireToCoordAndMiss:{
                int loginLength = Integer.parseInt(input.substring(3, 5));
                String login = input.substring(5, 5+loginLength);
                tmp = new Message(MessageCommand.S_C_ToObs_LoginFireToCoordAndMiss, login, input.substring(5+loginLength));
            }break;
            case MessageCommand.S_C_ToObs_LoginDestroyShipByCoord:{
                int loginLength = Integer.parseInt(input.substring(3,5));
                String login = input.substring(5, 5+loginLength);
                tmp = new Message(MessageCommand.S_C_ToObs_LoginDestroyShipByCoord, login, input.substring(5+loginLength));
            }break;
            case MessageCommand.S_C_ToObs_LoginWin:{
                tmp = new Message(MessageCommand.S_C_ToObs_LoginWin, input.substring(3), "");
            }break;
            case MessageCommand.S_C_ToObs_ActualGameInfo:{
                tmp = new Message(MessageCommand.S_C_ToObs_ActualGameInfo, input.substring(3,103), input.substring(103,203));
            }break;
            case MessageCommand.C_S_GamerWantToLose:{
                tmp = new Message (MessageCommand.C_S_GamerWantToLose, "", "");
            }break;
            case MessageCommand.C_S_WantStatAboutlogin:{
                int loginLength = Integer.parseInt(input.substring(3,5));
                String login = input.substring(5,5+loginLength);
                tmp = new Message(MessageCommand.C_S_WantStatAboutlogin, login, input.substring(5+loginLength));
            }break;
            case MessageCommand.C_S_LeftFromTheGame:{
                tmp = new Message(MessageCommand.C_S_LeftFromTheGame,"","");
            }break;
            case MessageCommand.S_C_HostLeftTheGame:{
                tmp = new Message(MessageCommand.S_C_HostLeftTheGame,"","");
            }break;
            case MessageCommand.C_S_StopObserveTheGame:{
                tmp = new Message(MessageCommand.C_S_StopObserveTheGame,"","");
            }break;
            case MessageCommand.S_C_ListOfLobbyGame:{
                tmp = toMessageStringWithGames(input);
            } break;
            case MessageCommand.S_C_Statistic:{
                tmp = toMessageStringWithStatistic(input);
            }break;
            case MessageCommand.S_C_EmptyListOfLobbyGames:{
                tmp = new Message(MessageCommand.S_C_EmptyListOfLobbyGames,"","");
            }break;
            case MessageCommand.C_S_DisconnectFromServer:{
                tmp = new Message(MessageCommand.C_S_DisconnectFromServer, "", "");
            }break;
            case MessageCommand.S_C_DisconnectFromServer:{
                tmp = new Message(MessageCommand.S_C_DisconnectFromServer, "", "");
            }break;
            case MessageCommand.S_C_SystemMessageStopTheThread:{
                tmp = new Message(MessageCommand.S_C_SystemMessageStopTheThread, "", "");
            }break;
            case MessageCommand.S_C_EmptyStat:{
                tmp = new Message(MessageCommand.S_C_EmptyStat,"","");
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
                case MessageCommand.C_S_TryLogin:{
                    textMessage += Integer.toString(MessageCommand.C_S_TryLogin);
                    textMessage += message.getLoginLength();
                    textMessage += message.getPassLength();
                    textMessage += message.getLogin();
                    textMessage += message.getPass();
                }break;
                case MessageCommand.S_C_SuccessLogin:{
                    textMessage += Integer.toString(MessageCommand.S_C_SuccessLogin);
                    textMessage += message.getLoginLength();
                    textMessage += message.getPassLength();
                    textMessage += message.getLogin();
                    textMessage += message.getPass();
                }break;
                case MessageCommand.S_C_InValidLogin:{
                    textMessage += Integer.toString(MessageCommand.S_C_InValidLogin);
                }break;
                case MessageCommand.C_S_TryConnectToLobby:{
                    textMessage += Integer.toString(MessageCommand.C_S_TryConnectToLobby);
                }break;
                case MessageCommand.S_C_YouAllowConnectToLobby:{
                    textMessage += Integer.toString(MessageCommand.S_C_YouAllowConnectToLobby);
                }break;
                case MessageCommand.C_S_MessageToLobby:{
                    textMessage += Integer.toString(MessageCommand.C_S_MessageToLobby);
                    textMessage += message.getLoginLength();
                    textMessage += message.getLogin();
                    textMessage += message.getMessage();
                }break;
                case MessageCommand.S_C_MessageToLobbyFromLogin:{
                    textMessage += Integer.toString(MessageCommand.S_C_MessageToLobbyFromLogin);
                    textMessage += message.getLoginLength();
                    textMessage += message.getLogin();
                    textMessage += message.getMessage();
                } break;
                case MessageCommand.S_C_MessageToLobbyFromServer:{
                    textMessage += Integer.toString(MessageCommand.S_C_MessageToLobbyFromServer);
                    textMessage += message.getMessage();
                } break;
                case MessageCommand.C_S_TryToRegisterNewLogin:{
                    textMessage += Integer.toString(MessageCommand.C_S_TryToRegisterNewLogin);
                    textMessage += message.getLoginLength();
                    textMessage += message.getPassLength();
                    textMessage += message.getLogin();
                    textMessage += message.getPass();
                }break;
                case MessageCommand.S_C_RegistrationSuccess:{
                    textMessage += Integer.toString(MessageCommand.S_C_RegistrationSuccess);
                    textMessage += message.getLoginLength();
                    textMessage += message.getPassLength();
                    textMessage += message.getLogin();
                    textMessage += message.getPass();
                }break;
                case MessageCommand.S_C_RegistrationNotSuccess:{
                    textMessage += Integer.toString(MessageCommand.S_C_RegistrationNotSuccess);
                }break;
                case MessageCommand.C_S_WantToCreateGame:{
                    textMessage += Integer.toString(MessageCommand.C_S_WantToCreateGame);
                }break;
                case MessageCommand.S_C_AllowToCreateGame:{
                    textMessage += Integer.toString(MessageCommand.S_C_AllowToCreateGame);
                }break;
                case MessageCommand.C_S_WantToConnectToGame:{
                    textMessage += Integer.toString(MessageCommand.C_S_WantToConnectToGame);
                    textMessage += message.getGameID();
                }break;
                case MessageCommand.S_C_SuccessConnectToGame:{
                    textMessage += Integer.toString(MessageCommand.S_C_SuccessConnectToGame);
                    textMessage += message.getGameInfo().toString();
                }break;
                case MessageCommand.S_C_NotAllowConnectToGame:{
                    textMessage += Integer.toString(MessageCommand.S_C_NotAllowConnectToGame);
                }break;
                case MessageCommand.C_S_ArmLeftTheLobby:{
                    textMessage += Integer.toString(MessageCommand.C_S_ArmLeftTheLobby);
                }break;
                case MessageCommand.S_C_ToHostGamer_NewGamerConnect:{
                    textMessage += Integer.toString(MessageCommand.S_C_ToHostGamer_NewGamerConnect);
                    textMessage += message.getLoginLength();
                    textMessage += message.getLogin();
                }break;
                case MessageCommand.S_C_RequesttoArmDisconnectFromLobby:{
                    textMessage += Integer.toString(MessageCommand.S_C_RequesttoArmDisconnectFromLobby);
                }break;
                case MessageCommand.S_C_NewGameInfo:{
                    textMessage += Integer.toString(MessageCommand.S_C_NewGameInfo);
                    textMessage += message.getGameInfo().toString();
                }break;
                case MessageCommand.C_S_GamerReadyAndSendBoard:{
                    textMessage += Integer.toString(MessageCommand.C_S_GamerReadyAndSendBoard);
                    textMessage += Decoder.ArrayToString(message.getBoard());
                }break;
                case MessageCommand.C_S_GamerNotReady:{
                    textMessage += Integer.toString(MessageCommand.C_S_GamerNotReady);
                }break;
                case MessageCommand.C_S_HostGamerStartTheGame:{
                    textMessage += Integer.toString(MessageCommand.C_S_HostGamerStartTheGame);
                } break;
                case MessageCommand.S_C_ToHostGamerStartTheGame:{
                    textMessage += Integer.toString(MessageCommand.S_C_ToHostGamerStartTheGame);
                } break;
                case MessageCommand.S_C_ToGamer2StartTheGame:{
                    textMessage += Integer.toString(MessageCommand.S_C_ToGamer2StartTheGame);
                } break;
                case MessageCommand.S_C_AllowObserveTheGame:{
                    textMessage += Integer.toString(MessageCommand.S_C_AllowObserveTheGame);
                } break;
                case MessageCommand.C_S_FireToCoord:{
                    textMessage += Integer.toString(MessageCommand.C_S_FireToCoord);
                    textMessage += "" + message.getCoordX();
                    textMessage += "" + message.getCoordY();
                } break;
                case MessageCommand.S_C_YouHitToCoord:{
                    textMessage += Integer.toString(MessageCommand.S_C_YouHitToCoord);
                    textMessage += "" + message.getCoordX();
                    textMessage += "" + message.getCoordY();
                } break;
                case MessageCommand.S_C_YouMissToCoord:{
                    textMessage += Integer.toString(MessageCommand.S_C_YouMissToCoord);
                    textMessage += "" + message.getCoordX();
                    textMessage += "" + message.getCoordY();
                } break;
                case MessageCommand.S_C_OpponentHitToYou:{
                    textMessage += Integer.toString(MessageCommand.S_C_OpponentHitToYou);
                    textMessage += "" + message.getCoordX();
                    textMessage += "" + message.getCoordY();
                } break;
                case MessageCommand.S_C_OpponentMissToYou:{
                    textMessage += Integer.toString(MessageCommand.S_C_OpponentMissToYou);
                    textMessage += "" + message.getCoordX();
                    textMessage += "" + message.getCoordY();
                } break;
                case MessageCommand.S_C_YouDestroyTheShipByCoord:{
                    // ответ сервера что по указанным координатам "потопили" корабль
                    textMessage += Integer.toString(MessageCommand.S_C_YouDestroyTheShipByCoord);
                    textMessage += "" + message.getCoordX();
                    textMessage += "" + message.getCoordY();
                } break;
                case MessageCommand.S_C_YourShipByCoordIsDestroyed:{
                    // уведомление сервера что ваш корабль содержащий такие координаты потоплен
                    textMessage += Integer.toString(MessageCommand.S_C_YourShipByCoordIsDestroyed);
                    textMessage += "" + message.getCoordX();
                    textMessage += "" + message.getCoordY();
                } break;
                case MessageCommand.S_C_YouWin:{
                    textMessage += Integer.toString(MessageCommand.S_C_YouWin);
                }break;
                case MessageCommand.S_C_YouLose:{
                    textMessage += Integer.toString(MessageCommand.S_C_YouLose);
                }break;
                case MessageCommand.C_S_NeedStatisticFromNumber:{
                    textMessage += Integer.toString(MessageCommand.C_S_NeedStatisticFromNumber);
                }break;
                case MessageCommand.S_C_ShowStatActivity:{
                    textMessage += Integer.toString(MessageCommand.S_C_ShowStatActivity);
                }break;
                case MessageCommand.C_S_NeedRefreshStatistic:{
                    textMessage += Integer.toString(MessageCommand.C_S_NeedRefreshStatistic);
                    textMessage += "" + message.getVariableOne();
                }break;
                case MessageCommand.C_S_MessageToLobbyFromlogin:{
                    //от клиента серверу - сообщение в лобби-чат
                    textMessage += Integer.toString(MessageCommand.C_S_MessageToLobbyFromlogin);
                    textMessage += message.getLoginLength();
                    textMessage += message.getLogin();
                    textMessage += message.getMessage();
                }break;
                case MessageCommand.S_C_MessageToLobbyFromlogin:{
                    textMessage += Integer.toString(MessageCommand.S_C_MessageToLobbyFromlogin);
                    textMessage += message.getLoginLength();
                    textMessage += message.getLogin();
                    textMessage += message.getMessage();
                }break;
                case MessageCommand.S_C_MessageToLobbyAboutCoonect:{
                    textMessage += Integer.toString(MessageCommand.S_C_MessageToLobbyAboutCoonect);
                    textMessage += message.getMessage();
                }break;
                case MessageCommand.C_S_WantToObserverToGame:{
                    textMessage += Integer.toString(MessageCommand.C_S_WantToObserverToGame);
                    textMessage += message.getGameID();
                }break;
                case MessageCommand.S_C_ShowObserverActivity:{
                    textMessage += Integer.toString(MessageCommand.S_C_ShowObserverActivity);
                    textMessage += message.getGameInfo().toString();
                }break;
                case MessageCommand.S_C_LoginFireToCoordAndHit:{
                    textMessage += Integer.toString(MessageCommand.S_C_LoginFireToCoordAndHit);
                    textMessage += message.getLoginLength();
                    textMessage += message.getLogin();
                    textMessage += "" + message.getCoordX();
                    textMessage += "" + message.getCoordY();
                }break;
                case MessageCommand.S_C_ToObs_LoginFireToCoordAndMiss:{
                    textMessage += Integer.toString(MessageCommand.S_C_ToObs_LoginFireToCoordAndMiss);
                    textMessage += message.getLoginLength();
                    textMessage += message.getLogin();
                    textMessage += "" + message.getCoordX();
                    textMessage += "" + message.getCoordY();
                }break;
                case MessageCommand.S_C_ToObs_LoginDestroyShipByCoord:{
                    textMessage += Integer.toString(MessageCommand.S_C_ToObs_LoginDestroyShipByCoord);
                    textMessage += message.getLoginLength();
                    textMessage += message.getLogin();
                    textMessage += "" + message.getCoordX();
                    textMessage += "" + message.getCoordY();
                }break;
                case MessageCommand.S_C_ToObs_LoginWin:{
                    textMessage += Integer.toString(MessageCommand.S_C_ToObs_LoginWin);
                    textMessage += message.getLogin();
                }break;
                case MessageCommand.S_C_ToObs_ActualGameInfo:{
                    textMessage += Integer.toString(MessageCommand.S_C_ToObs_ActualGameInfo);
                    textMessage += Decoder.boardWithShotsToString(message.getBoard());
                    textMessage += Decoder.boardWithShotsToString(message.getBoard2());
                }break;
                case MessageCommand.C_S_GamerWantToLose:{
                    textMessage += Integer.toString(MessageCommand.C_S_GamerWantToLose);
                }break;
                case MessageCommand.C_S_WantStatAboutlogin:{
                    textMessage += Integer.toString(MessageCommand.C_S_WantStatAboutlogin);
                    textMessage += message.getLoginLength();
                    textMessage += message.getLogin();
                }break;
                case MessageCommand.C_S_LeftFromTheGame:{
                    textMessage += Integer.toString(MessageCommand.C_S_LeftFromTheGame);
                }break;
                case MessageCommand.S_C_HostLeftTheGame:{
                    textMessage += Integer.toString(MessageCommand.S_C_HostLeftTheGame);
                }break;
                case MessageCommand.C_S_StopObserveTheGame:{
                    textMessage += Integer.toString(MessageCommand.C_S_StopObserveTheGame);
                }break;
                case MessageCommand.S_C_ListOfLobbyGame:{
                    textMessage = toStringMessageWithGames(message);
                }break;
                case MessageCommand.S_C_Statistic:{
                    textMessage = toStringMessageOfStatistic(message);
                }break;
                case MessageCommand.C_S_DisconnectFromServer:{
                    textMessage += Integer.toString(MessageCommand.C_S_DisconnectFromServer);
                }break;
                case MessageCommand.S_C_DisconnectFromServer:{
                    textMessage += Integer.toString(MessageCommand.S_C_DisconnectFromServer);
                }break;
                case MessageCommand.S_C_SystemMessageStopTheThread:{
                    textMessage += Integer.toString(MessageCommand.S_C_SystemMessageStopTheThread);
                }break;
                case MessageCommand.S_C_EmptyStat:{
                    textMessage += Integer.toString(MessageCommand.S_C_EmptyStat);
                }break;
                default:{
                    textMessage += Integer.toString(MessageCommand.EmptyMessage);
                }
            }
        }

        return textMessage;
    }



    private String toStringMessageWithGames(Message message){
        if(!message.getListOfGame().isEmpty()) {
            String result = "";
            result += MessageCommand.S_C_ListOfLobbyGame;
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

        return new Message(MessageCommand.S_C_ListOfLobbyGame, resultArray);
    }

    private String toStringMessageOfStatistic(Message message){
        if(!message.getStatisticList().isEmpty()){
            String result = "";
            result += MessageCommand.S_C_Statistic;
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
        return new Message(MessageCommand.S_C_Statistic,resultArray);
    }
}