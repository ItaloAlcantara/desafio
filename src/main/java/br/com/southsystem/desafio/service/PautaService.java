package br.com.southsystem.desafio.service;

import br.com.southsystem.desafio.Util.Util;
import br.com.southsystem.desafio.config.PautaMapper;
import br.com.southsystem.desafio.controller.PautaController;
import br.com.southsystem.desafio.exception.AssociadoException;
import br.com.southsystem.desafio.exception.PautaException;
import br.com.southsystem.desafio.model.Associado;
import br.com.southsystem.desafio.model.Pauta;
import br.com.southsystem.desafio.model.dto.AssociadoDto;
import br.com.southsystem.desafio.model.dto.AssociadoValidoDto;
import br.com.southsystem.desafio.model.dto.PautaDto;
import br.com.southsystem.desafio.model.dto.PautaResultado;
import br.com.southsystem.desafio.model.enumerador.Status;
import br.com.southsystem.desafio.model.enumerador.Voto;
import br.com.southsystem.desafio.repository.AssociadoRepository;
import br.com.southsystem.desafio.repository.PautaRepository;
import org.dozer.DozerBeanMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
public class PautaService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PautaController.class);

    @Autowired
    private DozerBeanMapper mapper;

    @Autowired
    private PautaRepository repository;

    @Autowired
    private AssociadoRepository associadoRepository;

    public List<Pauta> listar() {
        return repository.findAll();
    }

    public ResponseEntity<PautaResultado> resultado(Long id){
        Pauta pauta = validaPauta(id);
        if(validaPautaToPautaResultado(pauta)){
            LOGGER.info("Retortando a quantidade de votos e o resultado da votacao");
            return ResponseEntity.ok().body(
                                        PautaResultado.builder()
                                        .votosPositivos(pauta.getAssociados().stream().filter(associado ->
                                                associado.getVoto().equals(Voto.SIM)).count())
                                        .votosNegativos(pauta.getAssociados().stream().filter(associado ->
                                                associado.getVoto().equals(Voto.NAO)).count())
                                        .status(pauta.getStatus())
                                        .build());
        }
        throw PautaException.pautaNaoPossivelGerarResultado();

    }

    private boolean validaPautaToPautaResultado(Pauta pauta) {
        LOGGER.info("Validando se a pauta já encontra-se com data de expiracao maior que hora atual e se tem votos");
        return LocalDateTime.now().isAfter(pauta.getExpiracao()) && pauta.getAssociados().size() >0;
    }

    public ResponseEntity<PautaDto> cadastrar(PautaDto pauta) {
        LOGGER.info("Acessando o banco de dados para salvar uma nova pauta");
        if(pauta.getExpiracao() == null){
            pauta.setExpiracao(LocalDateTime.now().plusMinutes(1));
        }
        repository.save(PautaMapper.mapearDtotoEntity(pauta));
        LOGGER.info("Pauta salva");
        return ResponseEntity.ok().body(pauta);
    }

    public ResponseEntity votar (Long pautaId, AssociadoDto associadoDto){
        Pauta pauta = pautaExiste(pautaId);
        Associado associado = associadoExiste(associadoDto);

        LOGGER.info("Step de validacoes de associado");
        validaPautaEAssociado(pauta,associado.getCpf());
        pauta.getAssociados().add(associado);
        repository.save(pauta);
        return ResponseEntity.ok().body("Voto efetuado na pauta.");
    }

    private Associado associadoExiste(AssociadoDto associadoDto) {
        associadoDto.setCpf(Util.formatCpf(associadoDto.getCpf()));
        LOGGER.info("Indo na base para verificar se o associado ja existe para nao haver necessidade de cadastrar um novo");
        if(associadoRepository.findByCpf(associadoDto.getCpf()).isPresent()){
            LOGGER.info("Associado localizado");
            return associadoRepository.findByCpf(associadoDto.getCpf()).get();
        }
        return associadoRepository.saveAndFlush(mapper.map(associadoDto,Associado.class));
    }



    public Pauta pautaExiste(Long id){
        LOGGER.info("Acessando o banco de dados para verificar se exista a pauta de id={}",id);
        if (repository.findById(id).isPresent() ){
            LOGGER.info("Verificado que a pauta de id={} existe.",id);
            return repository.findById(id).get();
        }
        throw PautaException.pautaComId(id);
    }

    private  Pauta validaPauta(Long id){
        Pauta pauta = pautaExiste(id);
        LOGGER.info("Verifica se pauta já não é mais valida e muda o status");
        repository.save(pauta.pautaFechada());
        return pauta;
    }

    private void validaPautaEAssociado(Pauta pauta, String cpfAssociado) {

        repository.save(pauta.pautaFechada());

        if(pauta.getStatus() != null){
            throw PautaException.pautaNaoAtiva();
        }

        if(pauta.getAssociados().stream().anyMatch(associado -> associado.getCpf().equals(cpfAssociado)))
            throw PautaException.associadoJaVotouNessaPauta();

        if(!cpfValidoParaVotar(cpfAssociado))
            throw AssociadoException.cpfAssociadoNaoEstaValido();
    }

    private boolean cpfValidoParaVotar(String cpf){
        RestTemplate client = new RestTemplate();
        LOGGER.info("Indo na api de validacao de CPF");
        ResponseEntity<AssociadoValidoDto> response =
        client.exchange("https://user-info.herokuapp.com/users/"+cpf, HttpMethod.GET,
                null, AssociadoValidoDto.class);
        LOGGER.info("Resultado recuperado");
        return Objects.requireNonNull(response.getBody()).getStatus().equals("ABLE_TO_VOTE");
    }
}
