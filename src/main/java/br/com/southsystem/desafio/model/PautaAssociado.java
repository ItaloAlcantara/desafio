package br.com.southsystem.desafio.model;

import br.com.southsystem.desafio.model.enumerador.Voto;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
@Table(name = "pauta_associado",indexes = @Index(name = "pk_pauta_associado_id",unique = true,columnList = "id"))
@SequenceGenerator(name = "id", sequenceName = "SEQ_PAUTA_ASSOCIADO", allocationSize = 1)
public class PautaAssociado extends BaseEntity{

    @ManyToOne
    @JoinColumn(name = "id_pauta", referencedColumnName = "id")
    @JsonBackReference
    private Pauta pauta;

    @ManyToOne
    @JoinColumn(name = "id_associado", referencedColumnName = "id")
    @JsonBackReference
    private Associado associado;

    @Enumerated(EnumType.STRING)
    private Voto voto;


}
