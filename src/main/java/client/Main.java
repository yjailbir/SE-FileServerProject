package client;

import java.net.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {
        String address = "127.0.0.1";
        int port = 23456;
        Socket socket = new Socket(InetAddress.getByName(address), port);
        DataInputStream input = new DataInputStream(socket.getInputStream());
        DataOutputStream output = new DataOutputStream(socket.getOutputStream());
        Scanner scanner = new Scanner(System.in);
        String receive = "";
        String path = "src/main/java/client/data/";
        byte[] fileInBytes;
        System.out.print("Enter action (1 - get a file, 2 - save a file, 3 - delete a file): > ");
        receive = scanner.nextLine();
        switch (receive) {
            case "1" -> {
                output.writeUTF("1");
                System.out.print("Do you want to get the file by name or by id (1 - name, 2 - id): > ");
                receive = scanner.nextLine();
                switch (receive){
                    case "1" ->{
                        output.writeUTF("name");
                        System.out.print("Enter name of the file: > ");
                        receive = scanner.nextLine();
                        output.writeUTF(receive);
                    }
                    case "2" -> {
                        output.writeUTF("id");
                        System.out.print("Enter id: > ");
                        receive = scanner.nextLine();
                        output.writeUTF(receive);
                    }
                }
                System.out.println("The request was sent.");
                receive = input.readUTF();
                switch (receive) {
                    case "200" -> {
                        int length = input.readInt();
                        fileInBytes = new byte[length];
                        input.readFully(fileInBytes, 0, fileInBytes.length);
                        System.out.print("The file was downloaded! Specify a name for it: > ");
                        receive = scanner.nextLine();
                        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(path + receive));
                        bufferedOutputStream.write(fileInBytes);
                        bufferedOutputStream.close();
                        System.out.println("File saved on the hard drive!");
                    }
                    case "404" -> System.out.println("The response says that the file was not found!");
                }
            }
            case "2" -> {
                output.writeUTF("2");
                System.out.print("Enter name of the file: > ");
                receive = scanner.nextLine();
                String tmp = receive;
                fileInBytes = Files.readAllBytes(Path.of(path + receive));
                System.out.print("Enter name of the file to be saved on server: > ");
                receive = scanner.nextLine();
                output.writeInt(fileInBytes.length);
                output.write(fileInBytes);
                if (!receive.equals(""))
                    output.writeUTF(receive);
                else
                    output.writeUTF(tmp);
                System.out.println("The request was sent.");
                receive = input.readUTF();
                switch (receive) {
                    case "200" -> {
                        receive = input.readUTF();
                        System.out.println("The response says that file is saved! ID = " + receive);
                    }
                    case "404" -> System.out.println("The response says that this file is not found!");
                }
            }
            case "3" -> {
                output.writeUTF("3");
                System.out.print("Do you want to delete the file by name or by id (1 - name, 2 - id): > ");
                receive = scanner.nextLine();
                switch (receive) {
                    case "1" -> {
                        output.writeUTF("name");
                        System.out.print("Enter name of the file: > ");
                        receive = scanner.nextLine();
                        output.writeUTF(receive);
                    }
                    case "2" -> {
                        output.writeUTF("id");
                        System.out.print("Enter id: > ");
                        receive = scanner.nextLine();
                        output.writeUTF(receive);
                    }
                }
                System.out.println("The request was sent.");
                receive = input.readUTF();
                switch (receive) {
                    case "200" -> System.out.println("The response says that this file was deleted successfully!");
                    case "404" -> System.out.println("The response says that the file was not found!");
                }
            }
            case "exit" -> {
                System.out.println("The request was sent.");
                output.writeUTF("exit");
            }
            default -> //Is it necessary?
                    System.out.println("Unknown command!");
        }
        output.close();
        input.close();
        scanner.close();
        socket.close();
    }
}