package br.com.southsystem.desafio.util;

import br.com.southsystem.desafio.model.Associado;
import br.com.southsystem.desafio.model.Pauta;
import br.com.southsystem.desafio.model.enumerador.Voto;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;

public class TestesMocksUtil {



    public static Pauta getPautaCadastrada(){
        return Pauta.builder()
                .titulo("Teste a favor")
                .descricao("votos validos")
                .expiracao(LocalDateTime.now().plusHours(1))
                .build();
    }

    public static Pauta getPautaCadastradaComAssociado(){
        return Pauta.builder()
                .titulo("Teste a favor")
                .descricao("votos validos")
                .expiracao(LocalDateTime.now().plusHours(1))
                .build();
    }

    public static Pauta getPautaValidaComVotosAFavor(){
        return Pauta.builder()
                .titulo("Teste a favor")
                .descricao("votos validos")
                .expiracao(LocalDateTime.now().minus(1L, ChronoUnit.MINUTES))
                .build();
    }

    public static Pauta getPautaNaoValidaParaResultado(){
        return Pauta.builder()
                .titulo("Teste a favor")
                .descricao("votos validos")
                .expiracao(LocalDateTime.now().plusHours(1L))
                .build();
    }

    public static Pauta getPautaValidaComVotosContra(){
        return Pauta.builder()
                .titulo("Teste a favor")
                .descricao("votos validos")
                .expiracao(LocalDateTime.now().minus(1L, ChronoUnit.MINUTES))
                .build();
    }

    public static Associado getAssociadoVotandoAFavor(){
        return Associado.builder()
                .cpf("06613868302")
                .build();
    }

    public static Associado getAssociadoVotandoContra(){
        return Associado.builder()
                .cpf("06613868302")
                .build();
    }
}
