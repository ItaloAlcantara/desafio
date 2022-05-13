package br.com.southsystem.desafio.model.dto;

import br.com.southsystem.desafio.model.enumerador.Status;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PautaResultado implements Serializable {

    private Long votosPositivos;
    private Long votosNegativos;
    private Status status;

}
