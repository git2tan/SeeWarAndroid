package com.example.seawar;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * Created by Artem on 06.05.2017.
 */
public class ClientReceiver implements Runnable{
    Decoder decoder;
    private IController handler;
    private Socket socket;
    public ClientReceiver(IController handler, Socket socket){
        this.handler = handler;
        this.socket = socket;
        this.decoder = new Decoder();
    }

    @Override
    public void run() {
        try {
            BufferedReader r = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String fromServer;
            while(true){
                fromServer = r.readLine();
                if(fromServer != null){
                    System.err.println("Получил от сервера: " + fromServer);
                    Message message = decoder.decodeString(fromServer);
                    if(!message.isEmpty()) {
                        if (message.getNumberOfCommand() != 301)
                            handler.handleMessageFromServer(message);               //если сообщение удачно расшифрованно - отправляем его клиенту (то есть геймеру) вызывая обработчик
                        else
                            break;
                    }
                }else{
                    break;
                }
            }
        } catch (IOException e) {
            //добавить обработку отваливания
            e.printStackTrace();
            handler.handleMessageFromServer(new Message(301,"",""));
        }
    }
}
