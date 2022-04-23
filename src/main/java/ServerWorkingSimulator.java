import java.util.HashSet;
import java.util.Scanner;

public class ServerWorkingSimulator {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        String command = "";
        HashSet<String> files = new HashSet<>(10);
        HashSet<String> acceptableNames = new HashSet<>(10);
        for(int i = 1; i <= 10; i++)
            acceptableNames.add("file" + String.valueOf(i));
        while (!command.equals("exit")){
            System.out.print(">");
            command = in.nextLine();
            String[] arr = command.split(" ");
            switch (arr[0]){
                case "add":
                    if(files.contains(arr[1]) || !acceptableNames.contains(arr[1]))
                        System.out.println("Cannot add the file " + arr[1]);
                    else {
                        files.add(arr[1]);
                        System.out.println("The file " + arr[1] + " added successfully");
                    }
                    break;
                case "get":
                    if(files.contains(arr[1]))
                        System.out.println("The file " + arr[1] + " was sent");
                    else {
                        System.out.println("The file " + arr[1] + " not found");
                    }
                    break;
                case "delete":
                    if(files.contains(arr[1])){
                        System.out.println("The file " + arr[1] + " was deleted");
                        files.remove(arr[1]);
                    }
                    else {
                        System.out.println("The file " + arr[1] + " not found");
                    }
                    break;
                default:
                    if(!arr[0].equals("exit"))
                        System.out.println("Unknown command!");
                    break;
            }
        }
    }
}
