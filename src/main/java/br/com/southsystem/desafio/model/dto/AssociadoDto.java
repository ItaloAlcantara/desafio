package br.com.southsystem.desafio.model.dto;

import br.com.southsystem.desafio.model.enumerador.Voto;
import com.sun.istack.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@Builder
public class AssociadoDto implements Serializable {

    @NotNull
    private String cpf;

    @NotNull
    private Voto voto;
}
