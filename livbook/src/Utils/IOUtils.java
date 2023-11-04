package Utils;

import java.util.Scanner;

public class IOUtils {
    // Scanner utilizado para as operações de entrada e saída da classe
    private static Scanner scanner = new Scanner(System.in).useDelimiter("\n");


    // Método para obter uma string passando uma mensagem, que também é uma string
    static public String getText(String msg){
        System.out.print(msg);
        String input = scanner.next().trim();
        return input;
    }

    //Usado para obter dados que no "banco" estão em um formato específico como username
    static public String getTextNormalized(String msg){
        return getText(msg).trim().toLowerCase();
    }


    // Método para obter um inteiro passando uma mensagem que é uma string
    static public Integer getInt(String msg) throws NumberFormatException {
        System.out.print(msg);
        String input = scanner.next().trim();
        return Integer.parseInt(input);

    }

    //Método que quando invocado cria a ilusão de apagar a tela: 
    //exibe vários caracteres de quebra de linha
    static public void clearScreen(){
        System.out.print("<Enter....>");
        scanner.next();
        System.out.println("\n".repeat(20));
    }

    // Fecha o scanner para as operações de entrada e saída da classe
    // É obrigatório a sua invoação ao fim do programa, se não realizada, é levantada a 
    static public void closeScanner(){
        scanner.close();
    }
    
}
