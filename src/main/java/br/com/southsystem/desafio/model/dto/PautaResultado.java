package br.com.southsystem.desafio.model.dto;

import br.com.southsystem.desafio.model.enumerador.Status;
import br.com.southsystem.desafio.model.enumerador.Voto;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PautaResultado {

    private Long votosPositivos;
    private Long votosNegativos;
    private Status status;

}
