package server;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;

public class Session extends Thread{
    private final Socket socket;

    public Session(Socket socketForClient) {
        this.socket = socketForClient;
    }
    public void run(){
        try(
                DataInputStream input = new DataInputStream(socket.getInputStream());
                DataOutputStream output  = new DataOutputStream(socket.getOutputStream());
                ) {
            JSONParser parser = new JSONParser();
            FileReader JSONReader = new FileReader("src/main/java/server/files.json");
            JSONObject filesData = (JSONObject) parser.parse(JSONReader);
            JSONReader.close();
            String receive = "";
            String path = "src/main/java/server/data/";
            byte[] fileInBytes = null;
            receive = input.readUTF();
            switch (receive) {
                case "1" -> {
                    try {
                        receive = input.readUTF();
                        switch (receive){
                            case "name" ->{
                                receive = input.readUTF();
                                fileInBytes = Files.readAllBytes(Path.of(path + receive));
                            }
                            case "id" -> {
                                receive = input.readUTF();
                                fileInBytes = Files.readAllBytes(Path.of(path + filesData.get(receive)));
                            }
                        }
                        output.writeUTF("200");
                        assert fileInBytes != null;
                        output.writeInt(fileInBytes.length);
                        output.write(fileInBytes);
                    } catch (Exception e) {
                        output.writeUTF("404");
                    }
                }
                case "2" -> {
                    int length = input.readInt();
                    fileInBytes = new byte[length];
                    input.readFully(fileInBytes, 0, fileInBytes.length);
                    receive = input.readUTF();
                    if(!filesData.containsValue(receive)){
                        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(path + receive));
                        bufferedOutputStream.write(fileInBytes);
                        bufferedOutputStream.close();
                        output.writeUTF("200");
                        filesData.put(filesData.size(), receive);
                        Files.write(Path.of("src/main/java/server/files.json"), filesData.toJSONString().getBytes());
                        output.writeUTF(String.valueOf(filesData.size() - 1));
                    }
                    else {
                        output.writeUTF("404");
                    }
                }
                case "3" -> {
                    receive = input.readUTF();
                    File file = null;
                    switch (receive){
                        case "name" -> {
                            receive = input.readUTF();
                            file = new File(path + receive);
                        }
                        case "id" -> {
                            receive = input.readUTF();
                            file = new File(path + filesData.get(receive));
                        }
                    }
                    assert file != null;
                    if(file.delete()){
                        output.writeUTF("200");
                        filesData.remove(receive);
                        Files.write(Path.of("src/main/java/server/files.json"), filesData.toJSONString().getBytes());
                    }
                    else
                        output.writeUTF("404");
                }
                case "exit" -> Main.isItTimeToDisconnect = true;
            }
            socket.close();
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }
}
