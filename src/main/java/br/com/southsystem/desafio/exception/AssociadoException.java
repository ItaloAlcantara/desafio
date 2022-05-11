package br.com.southsystem.desafio.exception;

public class AssociadoException extends RuntimeException{

    public AssociadoException(final String message){super(message);}

    public static AssociadoException  cpfAssociadoNaoEstaValido() {
        throw new AssociadoException("O cpf do associado não está mais valido.");
    }

}
