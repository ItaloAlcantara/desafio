package br.com.southsystem.desafio.service;

import br.com.southsystem.desafio.Util.Util;
import br.com.southsystem.desafio.exception.AssociadoException;
import br.com.southsystem.desafio.exception.PautaException;
import br.com.southsystem.desafio.model.Associado;
import br.com.southsystem.desafio.model.Pauta;
import br.com.southsystem.desafio.model.PautaAssociado;
import br.com.southsystem.desafio.model.dto.AssociadoDto;
import br.com.southsystem.desafio.model.dto.PautaDto;
import br.com.southsystem.desafio.model.dto.PautaResultado;
import br.com.southsystem.desafio.model.enumerador.Voto;
import br.com.southsystem.desafio.repository.AssociadoRepository;
import br.com.southsystem.desafio.repository.PautaAssociadoRepository;
import br.com.southsystem.desafio.repository.PautaRepository;
import org.dozer.DozerBeanMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PautaService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PautaService.class);

    @Autowired
    private DozerBeanMapper mapper;

    @Autowired
    private PautaRepository repository;

    @Autowired
    private AssociadoRepository associadoRepository;

    @Autowired
    private CpfService validaCpf;

    @Autowired
    private PautaAssociadoRepository pautaAssociadoRepository;

    public List<Pauta> listar() {
        return repository.findAll();
    }

    public ResponseEntity<PautaDto> cadastrar(PautaDto pauta) {
        LOGGER.info("Acessando o banco de dados para salvar uma nova pauta");
        validaExpiracao(pauta);
        repository.save(Util.mapearDtotoEntityPauta(pauta));
        LOGGER.info("Pauta salva");
        return ResponseEntity.ok().body(pauta);
    }

    private void validaExpiracao(PautaDto pauta) {
        if(pauta.getExpiracao() != null && LocalDateTime.now().isAfter(pauta.getExpiracao())){
            throw PautaException.pautaInvalida();
        }
        if(pauta.getExpiracao() == null){
            pauta.setExpiracao(LocalDateTime.now().plusMinutes(1));
        }
    }


    public ResponseEntity votar (Long pautaId, AssociadoDto associadoDto){

        Pauta pauta = pautaExiste(pautaId);
        Associado associado = associadoExiste(associadoDto);
        LOGGER.info("Step de validacoes de associado");

        validaPautaEAssociado(pauta,associado.getCpf());

        LOGGER.info("Step de validacoes concluido!");

        LOGGER.info("Criando PautaAssociado");

        PautaAssociado pautaAssociado = PautaAssociado.builder()
                .associado(associado)
                .pauta(pauta)
                .voto(associadoDto.getVoto())
                .build();

        pautaAssociadoRepository.save(pautaAssociado);

        LOGGER.info("Salvando Pauta.");

        return ResponseEntity.ok().body("Voto efetuado na pauta.");
    }
        public ResponseEntity<PautaResultado> resultado(Long id){
        Pauta pauta = validaPauta(id);
        if(validaPautaToPautaResultado(pauta)){
            LOGGER.info("Retortando a quantidade de votos e o resultado da votacao");
            return ResponseEntity.ok().body(
                                        PautaResultado.builder()
                                        .votosPositivos(pauta.getPautaAssociados().stream().filter(associado ->
                                                associado.getVoto().equals(Voto.SIM)).count())
                                        .votosNegativos(pauta.getPautaAssociados().stream().filter(associado ->
                                                associado.getVoto().equals(Voto.NAO)).count())
                                        .status(pauta.getStatus())
                                        .build());
        }
        throw PautaException.pautaNaoPossivelGerarResultado();

    }

    private boolean validaPautaToPautaResultado(Pauta pauta) {
        LOGGER.info("Validando se a pauta já encontra-se com data de expiracao maior que hora atual e se tem votos");
        return LocalDateTime.now().isAfter(pauta.getExpiracao()) && pauta.getPautaAssociados().size() >0;
    }

    public Pauta pautaExiste(Long id){
        LOGGER.info("Acessando o banco de dados para verificar se exista a pauta de id={}",id);
        if (repository.findById(id).isPresent() ){
            LOGGER.info("Verificado que a pauta de id={} existe.",id);
            return repository.findById(id).get();
        }
        throw PautaException.pautaComId(id);
    }

    private Associado associadoExiste(AssociadoDto associadoDto) {
        associadoDto.setCpf(Util.formatCpf(associadoDto.getCpf()));
        LOGGER.info("Indo na base para verificar se o associado ja existe para nao haver necessidade de cadastrar um novo");
        if(associadoRepository.findByCpf(associadoDto.getCpf()).isPresent()){
            LOGGER.info("Associado localizado");
            return associadoRepository.findByCpf(associadoDto.getCpf()).get();
        }
        return associadoRepository.save(Util.mapearDtotoEntityAssociado(associadoDto.getCpf()));
    }

    private void validaPautaEAssociado(Pauta pauta, String cpfAssociado) {

        repository.save(pauta.pautaFechada());

        if(LocalDateTime.now().isAfter(pauta.getExpiracao())){
            throw PautaException.pautaNaoAtiva();
        }

        if(pauta.getStatus() != null){
            throw PautaException.pautaNaoAtiva();
        }

        if(!validaCpf.cpfValidoParaVotar(cpfAssociado))
            throw AssociadoException.cpfAssociadoNaoEstaValido();

        if(associadoDuplicadoParaPauta(pauta.getId(),cpfAssociado)){
            throw PautaException.associadoJaVotouNessaPauta();
        }
    }

    private boolean associadoDuplicadoParaPauta(Long pautaId,String cpf) {
        Associado associado;
        if(associadoRepository.findByCpf(cpf).isEmpty())
            return false;
        associado = associadoRepository.findByCpf(cpf).get();
        return pautaAssociadoRepository.findByPauta_IdAndAssociado_Id(pautaId, associado.getId()).isPresent();
    }

    private  Pauta validaPauta(Long id){
        Pauta pauta = pautaExiste(id);
        LOGGER.info("Verifica se pauta já não é mais valida e muda o status");
        repository.save(pauta.pautaFechada());
        return pauta;
    }


}
