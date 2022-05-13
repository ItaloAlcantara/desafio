package br.com.southsystem.desafio.util;

import br.com.southsystem.desafio.model.Associado;
import br.com.southsystem.desafio.model.Pauta;
import br.com.southsystem.desafio.model.PautaAssociado;
import br.com.southsystem.desafio.model.dto.AssociadoDto;
import br.com.southsystem.desafio.model.enumerador.Status;
import br.com.southsystem.desafio.model.enumerador.Voto;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collections;

public class TestesMocksUtil {



    public static Pauta getPautaCadastrada(){
        return Pauta.builder()
                .titulo("Teste a favor")
                .descricao("votos validos")
                .expiracao(LocalDateTime.now().plusHours(1))
                .build();
    }

    public static Pauta getPautaCadastradaExpiracaoMenorQueDataAtual(){
        return Pauta.builder()
                .titulo("Teste a favor")
                .descricao("votos validos")
                .expiracao(LocalDateTime.now().minus(5,ChronoUnit.MINUTES))
                .build();
    }

    public static Pauta getPautaCadastradaJaComStatus(){
        return Pauta.builder()
                .titulo("Teste a favor")
                .descricao("votos validos")
                .expiracao(LocalDateTime.now())
                .status(Status.APROVADA)
                .build();
    }

    public static Pauta getPautaValidaComVotosAFavor(){
        return Pauta.builder()
                .titulo("Teste a favor")
                .descricao("votos validos")
                .expiracao(LocalDateTime.now().minus(1L, ChronoUnit.MINUTES))
                .build();
    }

    public static PautaAssociado getPautaAssociadoVotandoAFavor(){
        return PautaAssociado.builder()
                .pauta(getPautaValidaComVotosAFavor())
                .associado(getAssociadoValido())
                .voto(Voto.SIM)
                .build();
    }

    public static Pauta getPautaComAssociados(){
        return Pauta.builder()
                .pautaAssociados(Collections.singletonList(getPautaAssociadoVotandoAFavor()))
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

    public static Associado getAssociadoValido(){
        return Associado.builder()
                .cpf("06613868302")
                .build();
    }

    public static AssociadoDto getAssociadoDto(){
        return AssociadoDto.builder()
                .cpf("06612368302")
                .voto(Voto.SIM).build();
    }
}
