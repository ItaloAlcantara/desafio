package br.com.southsystem.desafio.Util;

import br.com.southsystem.desafio.model.Associado;
import br.com.southsystem.desafio.model.Pauta;
import br.com.southsystem.desafio.model.dto.AssociadoDto;
import br.com.southsystem.desafio.model.dto.PautaDto;

public class Util {

    public static String formatCpf(String cpf) {
        if(cpf.contains(".") || cpf.contains("/"))
            return cpf.replace(".","").replace("/","");
        return cpf;
    }

    public static Pauta mapearDtotoEntityPauta(PautaDto pautaDto){
        return Pauta.builder()
                .expiracao(pautaDto.getExpiracao())
                .descricao(pautaDto.getDescricao())
                .titulo(pautaDto.getTitulo())
                .build();
    }

    public static Associado mapearDtotoEntityAssociado(String cpf){
        return Associado.builder()
                .cpf(cpf)
                .build();
    }

}
