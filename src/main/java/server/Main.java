package server;

import java.net.*;
import java.io.*;

public class Main {
    public static boolean isItTimeToDisconnect = false;
    public static void main(String[] args) {
        try (ServerSocket server = new ServerSocket(23456)) {
            System.out.println("Server started!");
            while (!isItTimeToDisconnect) {
                Session session = new Session(server.accept());
                session.start();
                session.join();
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
