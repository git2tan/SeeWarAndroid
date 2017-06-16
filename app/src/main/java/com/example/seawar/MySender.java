package com.example.seawar;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by Artem on 06.05.2017.
 */
public class MySender {
    private IController controller;
    private Decoder decoder;
    private Socket socket;
    private PrintWriter toServer;
    public MySender(){
        decoder = new Decoder();
    }
    public MySender(IController controller){
        this.controller = controller;
        decoder = new Decoder();
    }
    public void setSocket(Socket socket) throws IOException {
        this.socket = socket;
        toServer = new PrintWriter(socket.getOutputStream(),true);
    }

    public void sendMessage(Message message){
        String tmpString = decoder.codeMessage(message);
        System.out.println("Отправил:" + tmpString);
        toServer.println(tmpString);
    }
    public void setController(IController controller){
        this.controller = controller;
    }
}
