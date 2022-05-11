package br.com.southsystem.desafio.service;

import br.com.southsystem.desafio.controller.PautaController;
import br.com.southsystem.desafio.exception.AssociadoException;
import br.com.southsystem.desafio.exception.PautaException;
import br.com.southsystem.desafio.model.Associado;
import br.com.southsystem.desafio.model.Pauta;
import br.com.southsystem.desafio.model.dto.AssociadoValido;
import br.com.southsystem.desafio.model.enumerador.Status;
import br.com.southsystem.desafio.repository.PautaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
public class PautaService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PautaController.class);

    @Autowired
    private PautaRepository repository;

    public List<Pauta> listar() {
        return repository.findAll();
    }

    public ResponseEntity<Pauta> cadastrar(Pauta pauta, UriComponentsBuilder builder) {
        LOGGER.info("Acessando o banco de dados para salvar uma nova pauta");
        repository.save(pauta);
        LOGGER.info("Pauta salva com o id={}, com expiraão de : {}",pauta.getId(),pauta.getExpiracao());
        URI uri = builder.path("/pauta/{id}").buildAndExpand(pauta.getId()).toUri();
        return ResponseEntity.created(uri).body(pauta);
    }

    public ResponseEntity deletar(Long id){
        Pauta pauta = ifExists(id);
        pauta.setStatus(Status.INATIVA);
        pauta.setDeleted(LocalDateTime.now());
        return ResponseEntity.ok().build();
    }

    public ResponseEntity votar (Long pautaId, Associado associado){
        Pauta pauta = ifExists(pautaId);
        validaPauta(pauta,associado);
        pauta.getAssociados().add(associado);
        return ResponseEntity.ok().body("Voto efetuado na pauta.");
    }

    public Pauta ifExists(Long id){
        LOGGER.info("Acessando o banco de dados para verificar se exista a pauta de id={}",id);
        if (repository.findById(id).isPresent()){
            LOGGER.info("Verificado que a pauta de id={} existe.",id);
            return repository.findById(id).get();
        }
        throw PautaException.pautaComId(id);
    }

    private void validaPauta(Pauta pauta, Associado associado) {

        if(!pauta.getStatus().equals(Status.ATIVA)){
            throw PautaException.pautaNaoAtiva();
        }

//        if(pauta.getExpiracao().isAfter(LocalDateTime.now()))
//            throw PautaException.pautaNaoAtiva();

        if(cpfValidoParaVotar(associado.getCpf()))
            throw AssociadoException.cpfAssociadoNaoEstaValido();
    }

    private boolean cpfValidoParaVotar(String cpf){
        RestTemplate client = new RestTemplate();
        ResponseEntity<AssociadoValido> response =
        client.exchange("https://user-info.herokuapp.com/users/"+cpf, HttpMethod.GET,
                null, AssociadoValido.class);

        return Objects.requireNonNull(response.getBody()).getValido().equals("ABLE_TO_VOTE");
    }
}
