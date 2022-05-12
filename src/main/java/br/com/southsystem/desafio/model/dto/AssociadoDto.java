package br.com.southsystem.desafio.model.dto;

import br.com.southsystem.desafio.model.enumerador.Voto;
import com.sun.istack.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AssociadoDto {

    @NotNull
    private String cpf;

    @NotNull
    private Voto voto;
}
