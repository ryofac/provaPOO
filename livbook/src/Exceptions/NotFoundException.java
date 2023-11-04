package Exceptions;

// Essa exceção é lançada quando algum elemento em sua pesquisa não existe no repository
// A sua mensagem é passada no construtor ao longo do código
public class NotFoundException extends Exception {
    public NotFoundException(String message) {
        super(message);
    }

}
