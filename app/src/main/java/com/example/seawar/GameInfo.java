package com.example.seawar;

/**
 * Created by Artem on 14.05.2017.
 */
public class GameInfo {
    int id;
    String login1;
    String login2;
    boolean isReady1;
    boolean isReady2;
    int observerCount;
    public GameInfo(int id, String login1, boolean isReady1, String login2, boolean isReady2, int observerCount){
        this.id = id;
        this.login1 = login1;
        this.isReady1 = isReady1;
        this.login2 = login2;
        this.isReady2 = isReady2;
        this.observerCount = observerCount;
    }

    public String toString(){
        String tmp = "";
        if(!login2.isEmpty()){
            tmp = String.format("%s%02d%s%s","1",login2.length(),login2, isReady2?"1":"0");
        }
        else{
            tmp = "0";
        }
        String answer = String.format("%06d%02d%s%s%s%04d", id, login1.length(), login1, (isReady1?"1":"0"), tmp, observerCount);
        return answer;
    }
}
