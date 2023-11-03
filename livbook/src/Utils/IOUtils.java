package Utils;

import java.util.Scanner;

public class IOUtils {
    private static Scanner scanner = new Scanner(System.in);

    static public String getText(String msg){
        System.out.println(msg);
        String input = scanner.next();
        return input;
    }

    static public void closeScanner(){
        scanner.close();
    }
    
}
