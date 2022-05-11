package br.com.southsystem.desafio.model;

import br.com.southsystem.desafio.model.enumerador.Status;
import br.com.southsystem.desafio.model.enumerador.Voto;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
@Table(name = "pauta",indexes = @Index(name = "pk_pauta_id",unique = true,columnList = "id"))
@SequenceGenerator(name = "id", sequenceName = "SEQ_PAUTA", allocationSize = 1)
public class Pauta extends BaseEntity {

    private String titulo;
    private String descricao;

    @ManyToMany
    @JoinTable(
            name = "pauta_associado",
            joinColumns = @JoinColumn(name = "associado_id"),
            inverseJoinColumns = @JoinColumn(name = "pauta_id")
    )
    private List<Associado> associados = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private Status status = Status.ATIVA;

    private LocalDateTime expiracao = LocalDateTime.now().plusMinutes(1);

    private void pautaFechada() {
        if (this.getExpiracao().isEqual(LocalDateTime.now())) {
            Long aprovada = associados.stream().filter(associado -> associado.getVoto().equals(Voto.SIM)).count();
            Long reprovada = associados.stream().filter(associado -> associado.getVoto().equals(Voto.NAO)).count();
            this.setStatus(aprovada.compareTo(reprovada) > 0 ? Status.APROVADA : Status.REPROVADA);
        }
    }

}
