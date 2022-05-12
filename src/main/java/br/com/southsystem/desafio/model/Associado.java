package br.com.southsystem.desafio.model;

import br.com.southsystem.desafio.model.enumerador.Voto;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
@Table(name = "associado",indexes = @Index(name = "pk_associado_id",unique = true,columnList = "id"))
@SequenceGenerator(name = "id", sequenceName = "SEQ_ASSOCIADO", allocationSize = 1)
public class Associado extends BaseEntity{

    private String cpf;

    @Enumerated(EnumType.STRING)
    private Voto voto;

    @JsonBackReference
    @ManyToMany(mappedBy = "associados")
    private List<Pauta> pautaList;

}
