package br.com.southsystem.desafio.model.dto;

import com.sun.istack.NotNull;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
public class PautaDto implements Serializable {

    @NotNull
    private String titulo;

    @NotNull
    private String descricao;
    private LocalDateTime expiracao = LocalDateTime.now().plusMinutes(1);
}
