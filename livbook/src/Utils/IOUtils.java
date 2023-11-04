package Utils;

import java.util.Scanner;

public class IOUtils {
    // Scanner utilizado para as operações de entrada e saída da classe
    private static Scanner scanner = new Scanner(System.in);


    // Método para obter uma string passando uma mensagem, que também é uma string
    static public String getText(String msg){
        System.out.println(msg);
        String input = scanner.next();
        return input;
    }


    // Método para obter um inteiro passando uma mensagem que é uma string
    static public Integer getInt(String msg) throws NumberFormatException {
        System.out.println(msg);
        String input = scanner.next();
        return Integer.parseInt(input);

    }

    //Método que quando invocado cria a ilusão de apagar a tela: 
    //exibe vários caracteres de quebra de linha
    static public void clearScreen(){
        System.out.println("\n".repeat(20));
    }

    // Fecha o scanner para as operações de entrada e saída da classe
    // É obrigatório a sua invoação ao fim do programa, se não realizada, é levantada a 
    static public void closeScanner(){
        scanner.close();
    }
    
}
