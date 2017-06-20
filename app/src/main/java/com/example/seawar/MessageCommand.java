package com.example.seawar;

/**
 * Created by Artem on 20.06.2017.
 */

public class MessageCommand{
    public static final int C_S_TryLogin = 100;
    public static final int S_C_SuccessLogin = 101;
    public static final int S_C_InValidLogin = 102;
    public static final int C_S_TryConnectToLobby = 103;
    public static final int S_C_YouAllowConnectToLobby = 104;
    public static final int C_S_MessageToLobby = 105;
    public static final int S_C_MessageToLobbyFromLogin = 106;
    public static final int S_C_MessageToLobbyFromServer = 107;
    public static final int C_S_TryToRegisterNewLogin = 108;
    public static final int S_C_RegistrationSuccess = 109;
    public static final int S_C_RegistrationNotSuccess = 110;
    public static final int C_S_WantToCreateGame = 111;
    public static final int S_C_AllowToCreateGame = 112;
    public static final int C_S_WantToConnectToGame = 113;
    public static final int S_C_SuccessConnectToGame = 114;
    public static final int S_C_NotAllowConnectToGame = 115;
    public static final int C_S_ArmLeftTheLobby = 116;
    public static final int S_C_ToHostGamer_NewGamerConnect = 117;
    public static final int S_C_RequesttoArmDisconnectFromLobby = 118;
    public static final int S_C_NewGameInfo = 119;
    public static final int C_S_GamerReadyAndSendBoard = 120;
    public static final int C_S_GamerNotReady = 121;
    public static final int C_S_HostGamerStartTheGame = 122;
    public static final int S_C_ToHostGamerStartTheGame = 123;
    public static final int S_C_ToGamer2StartTheGame = 124;
    public static final int S_C_AllowObserveTheGame = 125;
    public static final int C_S_FireToCoord  = 126;
    public static final int S_C_YouHitToCoord  = 127;
    public static final int S_C_YouMissToCoord = 128;
    public static final int S_C_OpponentHitToYou = 129;
    public static final int S_C_OpponentMissToYou  = 130;
    public static final int S_C_YouDestroyTheShipByCoord  = 131;
    public static final int S_C_YourShipByCoordIsDestroyed = 132;
    public static final int S_C_YouWin = 133;
    public static final int S_C_YouLose = 134;
    public static final int C_S_NeedStatisticFromNumber = 135;
    public static final int S_C_ShowStatActivity = 136;
    public static final int C_S_NeedRefreshStatistic = 137;
    public static final int C_S_MessageToLobbyFromlogin = 138;
    public static final int S_C_MessageToLobbyFromlogin = 139;
    public static final int S_C_MessageToLobbyAboutCoonect = 140;
    public static final int C_S_WantToObserverToGame = 141;
    public static final int S_C_ShowObserverActivity = 142;
    public static final int S_C_LoginFireToCoordAndHit = 143;
    public static final int S_C_ToObs_LoginFireToCoordAndMiss = 144;
    public static final int S_C_ToObs_LoginDestroyShipByCoord = 145;
    public static final int S_C_ToObs_LoginWin = 146;
    public static final int S_C_ToObs_ActualGameInfo = 147;
    public static final int C_S_GamerWantToLose = 148;
    public static final int C_S_WantStatAboutlogin = 149;
    public static final int C_S_LeftFromTheGame = 150;
    public static final int S_C_HostLeftTheGame = 151;
    public static final int C_S_StopObserveTheGame = 152;
    public static final int S_C_ListOfLobbyGame = 201;
    public static final int S_C_Statistic = 202;
    public static final int S_C_EmptyListOfLobbyGames  = 203;
    public static final int C_S_DisconnectFromServer = 300;
    public static final int S_C_DisconnectFromServer = 301;
    public static final int S_C_SystemMessageStopTheThread = 302;
    public static final int C_S_ShutdownServer = 997;
    public static final int S_C_EmptyStat = 998;
    public static final int EmptyMessage   = 999;
}
