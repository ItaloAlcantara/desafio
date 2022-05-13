package br.com.southsystem.desafio.service;

import br.com.southsystem.desafio.exception.AssociadoException;
import br.com.southsystem.desafio.exception.PautaException;
import br.com.southsystem.desafio.model.Pauta;
import br.com.southsystem.desafio.model.dto.PautaDto;
import br.com.southsystem.desafio.model.dto.PautaResultado;
import br.com.southsystem.desafio.repository.AssociadoRepository;
import br.com.southsystem.desafio.repository.PautaAssociadoRepository;
import br.com.southsystem.desafio.repository.PautaRepository;
import br.com.southsystem.desafio.util.TestesMocksUtil;
import org.dozer.DozerBeanMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.exceptions.misusing.PotentialStubbingProblem;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@DisplayName("Serviço de Pautas")
@ExtendWith(MockitoExtension.class)
public class PautaServiceTestes {

    @InjectMocks
    private PautaService service;

    @Mock
    private CpfService cpf;

    @Mock
    private PautaRepository repository;

    @Mock
    private AssociadoRepository associadoRepository;

    @Mock
    private PautaAssociadoRepository pautaAssociadoRepository;

    @Mock
    private DozerBeanMapper mapper;
    @Test
    public void deveriaNaoTrazerResultado(){
        when(repository.findById(Mockito.anyLong())).thenReturn(Optional.of(TestesMocksUtil.getPautaNaoValidaParaResultado()));
        Assertions.assertThrows(PautaException.class, () -> service.resultado(Mockito.anyLong()));
    }

    @Test
    public void deveriaCadastrarUmaPauta(){
        when(repository.save(any())).thenReturn(TestesMocksUtil.getPautaCadastrada());
        ResponseEntity<PautaDto> resultado = service.cadastrar(PautaDto.builder()
                                                                            .descricao("teste")
                                                                            .titulo("teste").build());
        Assertions.assertNotNull(Objects.requireNonNull(resultado.getBody()).getTitulo());
    }

    @Test
    public void deveriaNaoCadastrarUmaPauta(){
        Assertions.assertThrows(PautaException.class, () -> service.cadastrar(PautaDto.builder()
                .descricao("teste")
                .titulo("teste")
                .expiracao(LocalDateTime.now().minus(5, ChronoUnit.MINUTES)).build()));
    }

    @Test
    public void deveriaMostrarListaPautas(){
        when(repository.findAll()).thenReturn(Collections.singletonList(TestesMocksUtil.getPautaCadastrada()));
        List<Pauta> resultado = service.listar();
        Assertions.assertNotNull(resultado);
    }

    @Test
    public void deveriaNaoLocalizarPauta(){
        when(repository.findById(any())).thenReturn(Optional.empty());
        Assertions.assertThrows(PautaException.class, () -> service.resultado(anyLong()));
    }

    @Test
    public void deveriaNaoLocalizarPautaPorId(){
        when(repository.findById(anyLong())).thenReturn(Optional.empty());
        Assertions.assertThrows(PautaException.class, () -> service.votar(anyLong(),TestesMocksUtil.getAssociadoDto()));
    }

    @Test
    public void deveriaRetornarErroDeDataExpirada(){
        when(repository.findById(anyLong())).thenReturn(Optional.of(TestesMocksUtil.getPautaCadastradaExpiracaoMenorQueDataAtual()));
        when(repository.save(any())).thenReturn(TestesMocksUtil.getPautaCadastradaExpiracaoMenorQueDataAtual());
        when(associadoRepository.findByCpf(anyString())).thenReturn(Optional.empty());
        when(associadoRepository.save(any())).thenReturn(TestesMocksUtil.getAssociadoValido());
        Assertions.assertThrows(PautaException.class, () -> service.votar(anyLong(),TestesMocksUtil.getAssociadoDto()));
    }

    @Test
    public void deveriaRetornarErroDeStatusInvalido(){
        when(repository.findById(anyLong())).thenReturn(Optional.of(TestesMocksUtil.getPautaCadastradaJaComStatus()));
        when(repository.save(any())).thenReturn(TestesMocksUtil.getPautaCadastradaJaComStatus());
        when(associadoRepository.findByCpf(anyString())).thenReturn(Optional.of(TestesMocksUtil.getAssociadoValido()));
        Assertions.assertThrows(PautaException.class, () -> service.votar(anyLong(),TestesMocksUtil.getAssociadoDto()));
    }

    @Test
    public void deveriaRetornarErroDeCPF(){
        when(repository.findById(anyLong())).thenReturn(Optional.of(TestesMocksUtil.getPautaCadastrada()));
        when(repository.save(any())).thenReturn(TestesMocksUtil.getPautaCadastrada());
        when(associadoRepository.findByCpf(anyString())).thenReturn(Optional.of(TestesMocksUtil.getAssociadoValido()));
        Assertions.assertThrows(AssociadoException.class, () -> service.votar(anyLong(),TestesMocksUtil.getAssociadoDto()));
    }

    @Test
    public void deveriaRetornarErroDeAssociadoDuplicado(){
        when(repository.findById(anyLong())).thenReturn(Optional.of(TestesMocksUtil.getPautaCadastrada()));
        when(repository.save(any())).thenReturn(TestesMocksUtil.getPautaCadastrada());
        when(associadoRepository.findByCpf(anyString())).thenReturn(Optional.of(TestesMocksUtil.getAssociadoValido()));
        when(associadoRepository.findById(anyLong())).thenReturn(Optional.of(TestesMocksUtil.getAssociadoValido()));
        when(pautaAssociadoRepository.findByPauta_IdAndAssociado_Id(1L,1L))
                .thenReturn(Optional.of(TestesMocksUtil.getPautaAssociadoVotandoAFavor()));
        when(cpf.cpfValidoParaVotar(any())).thenReturn(true);
        Assertions.assertThrows(PotentialStubbingProblem.class, () -> service.votar(anyLong(),TestesMocksUtil.getAssociadoDto()));
    }


    @Test
    public void deveriaVotar(){
        when(repository.findById(anyLong())).thenReturn(Optional.of(TestesMocksUtil.getPautaCadastrada()));
        when(repository.save(any())).thenReturn(TestesMocksUtil.getPautaCadastrada());
        when(associadoRepository.save(any())).thenReturn(TestesMocksUtil.getAssociadoValido());
        when(cpf.cpfValidoParaVotar(any())).thenReturn(true);
        when(pautaAssociadoRepository.save(any())).thenReturn(TestesMocksUtil.getPautaAssociadoVotandoAFavor());
        ResponseEntity response = service.votar(anyLong(),TestesMocksUtil.getAssociadoDto());
        Assertions.assertNotNull(response.getBody());

    }

    @Test
    public void deveriaVirResultado(){
        when(repository.findById(anyLong())).thenReturn(Optional.of(TestesMocksUtil.getPautaComAssociados()));
        when(repository.save(any())).thenReturn(TestesMocksUtil.getPautaComAssociados());
        ResponseEntity<PautaResultado> response = service.resultado(anyLong());
        Assertions.assertNotNull(response.getBody());

    }

}
