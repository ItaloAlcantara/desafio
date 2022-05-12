package br.com.southsystem.desafio.repository;

import br.com.southsystem.desafio.model.Associado;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AssociadoRepository extends JpaRepository<Associado,Long> {

    Optional<Associado> findByCpf(String cpf);
}
