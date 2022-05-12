package br.com.southsystem.desafio.model.dto;

import com.sun.istack.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
public class PautaDto {

    @NotNull
    private String titulo;

    @NotNull
    private String descricao;
    private LocalDateTime expiracao = LocalDateTime.now().plusMinutes(1);
}
