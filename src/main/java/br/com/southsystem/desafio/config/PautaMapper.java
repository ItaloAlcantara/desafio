package br.com.southsystem.desafio.config;

import br.com.southsystem.desafio.model.Pauta;
import br.com.southsystem.desafio.model.dto.PautaDto;

public class PautaMapper {

    public static Pauta mapearDtotoEntity(PautaDto pautaDto){
        return Pauta.builder()
                .expiracao(pautaDto.getExpiracao())
                .descricao(pautaDto.getDescricao())
                .titulo(pautaDto.getTitulo())
                .build();
    }
}
