package br.com.southsystem.desafio.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class PautaException extends RuntimeException{

    public PautaException(final String message){super(message);}

    public static PautaException pautaComId(Long id){
        return new PautaException("Pauta com o id = "+id+" não existe na base");
    }

    public static PautaException  pautaNaoAtiva() {
        throw new PautaException("Pauta não está mais ativa.");
    }

}
