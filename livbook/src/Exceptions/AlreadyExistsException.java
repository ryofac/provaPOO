package Exceptions;

// Essa exceção é lançada quando algum elemento em sua tentativa de criação já existe nos repositories
// A sua mensagem é passada no construtor ao longo do código
public class AlreadyExistsException extends Exception {

    public AlreadyExistsException(String message) {
        super(message);
    }
}
