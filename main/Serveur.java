package main;

import request.Request;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Serveur extends Thread{
    Socket client;
    public static void main(String[] args) throws IOException {
        ServerSocket s= new ServerSocket(7777);
        while (true){
            Socket client = s.accept();
            Serveur ss=new Serveur(client);
            ss.start();
        }

    }

    public Serveur (Socket client){
        this.client=client;
    }

    public void run(){
        try {
            Request request = new Request();
            DataInputStream message = new DataInputStream(client.getInputStream());
            String mess = message.readUTF();
            System.out.println(mess);
            String sss = request.request(mess).toString();
            DataOutputStream renvoie = new DataOutputStream(client.getOutputStream());
            renvoie.writeUTF(sss);
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
