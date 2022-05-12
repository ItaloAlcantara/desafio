package br.com.southsystem.desafio.repository;

import br.com.southsystem.desafio.model.PautaAssociado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PautaAssociadoRepository extends JpaRepository<PautaAssociado,Long> {

    Optional<PautaAssociado> findByPauta_IdAndAssociado_Id(Long pautaId, Long associadoId);
}