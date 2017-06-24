package com.example.seawar;

/**
 * Created by Artem on 13.05.2017.
 */
public class ServerGame {
    int id;
    String login;
    boolean isFull;
    int observerCount;

    public ServerGame(int id, String login, boolean isFull, int observerCount){
        this.id = id;
        this.login = login;
        this.isFull = isFull;
        this.observerCount = observerCount;
    }

    @Override
    public String toString(){
        String result = String.format(" %s  %s  %d", login, isFull?"full":"ready to game", observerCount);
        return result;
    }

    public int getId() {
        return id;
    }
}
