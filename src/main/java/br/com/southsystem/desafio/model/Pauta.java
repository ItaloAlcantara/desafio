package br.com.southsystem.desafio.model;

import br.com.southsystem.desafio.model.dto.VotacaoDto;
import br.com.southsystem.desafio.model.enumerador.Status;
import br.com.southsystem.desafio.model.enumerador.Voto;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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

    @OneToMany(mappedBy = "pauta", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonManagedReference
    private List<PautaAssociado> pautaAssociados = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private Status status;

    private LocalDateTime expiracao ;


    public Pauta pautaFechada() {
        if (LocalDateTime.now().isAfter(this.getExpiracao())) {
            VotacaoDto votacaoDto = new VotacaoDto();

            if(pautaAssociados != null){
                pautaAssociados.forEach(associado -> {
                    if(associado.getVoto().equals(Voto.SIM)){
                        votacaoDto.addVotoAFavor();
                    }else{
                        votacaoDto.addVotoContra();
                    }
                });
                this.setStatus(votacaoDto.getVotosAFavor() > votacaoDto.getVotosContra() ? Status.APROVADA : Status.REPROVADA);
            }
            return this;
        }
        return this;}


}
