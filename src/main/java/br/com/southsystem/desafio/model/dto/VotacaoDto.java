package br.com.southsystem.desafio.model.dto;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VotacaoDto implements Serializable{

    private Integer votosAFavor = 0;

    private Integer votosContra = 0;

    public void addVotoAFavor() {
        ++votosAFavor;
    }

    public void addVotoContra() {
        ++votosContra;
    }
}
