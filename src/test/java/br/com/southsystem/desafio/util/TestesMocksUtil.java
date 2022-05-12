package br.com.southsystem.desafio.util;

import br.com.southsystem.desafio.model.Associado;
import br.com.southsystem.desafio.model.Pauta;
import br.com.southsystem.desafio.model.enumerador.Voto;

import java.util.List;

public class TestesMocksUtil {

    public static Pauta getPautaValidaComVotosAFavor(){
        return Pauta.builder()
                .titulo("Teste a favor")
                .descricao("votos validos")
                .associados(List.of(getAssociadoVotandoAFavor()))
                .build();
    }

    public static Associado getAssociadoVotandoAFavor(){
        return Associado.builder()
                .cpf("06613868302")
                .voto(Voto.SIM)
                .build();
    }
}
