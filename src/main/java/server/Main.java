package server;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.net.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Main {
    public static boolean isItTimeToDisconnect = false;
    public static ConcurrentHashMap<String, String> filesData = new ConcurrentHashMap<>();
    public static void main(String[] args) {
        try (ServerSocket server = new ServerSocket(23456)) {
            JSONParser parser = new JSONParser();
            FileReader JSONReader = new FileReader("src/main/java/server/files.json");
            HashMap<String, String> oldHashMap = (HashMap<String, String>) parser.parse(JSONReader);
            Set<String> oldSet = oldHashMap.keySet();
            for(String k: oldSet)
                filesData.put(k, oldHashMap.get(k));
            JSONReader.close();
            System.out.println("Server started!");
            while (!isItTimeToDisconnect) {
                Session session = new Session(server.accept());
                session.start();
                session.join();
            }
            HashMap<String, String> newHashMap = new HashMap<>();
            Set<String> newSet = filesData.keySet();
            for(String k: newSet)
                newHashMap.put(k, filesData.get(k));
            JSONObject jsonObject = new JSONObject(newHashMap);
            Files.write(Path.of("src/main/java/server/files.json"), jsonObject.toJSONString().getBytes());
        } catch (IOException | InterruptedException | ParseException e) {
            e.printStackTrace();
        }
    }
}
