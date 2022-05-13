package br.com.southsystem.desafio.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class PautaException extends RuntimeException{

    public PautaException(final String message){super(message);}

    public static PautaException pautaComId(Long id){
        return new PautaException("Pauta com o id = "+id+" não existe na base!");
    }

    public static PautaException  pautaNaoAtiva() {
        throw new PautaException("Pauta não está mais ativa!");
    }

    public static PautaException  associadoJaVotouNessaPauta() {
        throw new PautaException("Associado em questão já votou!");
    }

    public static PautaException  pautaNaoPossivelGerarResultado() {
        throw new PautaException("Pauta em questão ou não foi finalizada ou não possui votos.");
    }

    public static PautaException  pautaInvalida() {
        throw new PautaException("Pauta em questão é invalida pois data de expiracao menor que data e hora atual.");
    }

}
