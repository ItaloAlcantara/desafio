package br.com.southsystem.desafio.repository;

import br.com.southsystem.desafio.model.Associado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface AssociadoRepository extends JpaRepository<Associado,Long> {

    Optional<Associado> findByCpf(String cpf);

    @Query(value = "select p from PAUTA_ASSOCIADO  as p where p.ASSOCIADO_ID = :associadoId and p.PAUTA_ID = :pautaId", nativeQuery=true)
    Optional<Associado> findByIdNaPauta(@Param("associadoId")Long associadoId,
                                        @Param("pautaId")Long pautaId);
}
