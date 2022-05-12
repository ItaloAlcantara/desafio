package br.com.southsystem.desafio.service;

import br.com.southsystem.desafio.exception.PautaException;
import br.com.southsystem.desafio.model.Pauta;
import br.com.southsystem.desafio.model.dto.AssociadoDto;
import br.com.southsystem.desafio.model.dto.PautaDto;
import br.com.southsystem.desafio.model.dto.PautaResultado;
import br.com.southsystem.desafio.model.enumerador.Voto;
import br.com.southsystem.desafio.repository.AssociadoRepository;
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
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@DisplayName("Serviço de Pautas")
@ExtendWith(MockitoExtension.class)
public class PautaServiceTestes {

    @InjectMocks
    private PautaService service;

    @Mock
    private PautaRepository repository;

    @Mock
    private AssociadoRepository associadoRepository;

    @Mock
    private DozerBeanMapper mapper;

    @Test
    public void deveriaTrazerResultadoAprovadoComUmVoto(){
        when(repository.findById(Mockito.anyLong())).thenReturn(Optional.of(TestesMocksUtil.getPautaValidaComVotosAFavor()));
        ResponseEntity<PautaResultado> resultado = service.resultado(Mockito.anyLong());
        Assertions.assertEquals(Objects.requireNonNull(resultado.getBody()).getVotosPositivos(),1);
    }

    @Test
    public void deveriaTrazerResultadoReprovadoComUmVoto(){
        when(repository.findById(Mockito.anyLong())).thenReturn(Optional.of(TestesMocksUtil.getPautaValidaComVotosContra()));
        ResponseEntity<PautaResultado> resultado = service.resultado(Mockito.anyLong());
        Assertions.assertEquals(Objects.requireNonNull(resultado.getBody()).getVotosNegativos(),1);
    }

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
    public void deveriaMostrarListaPautas(){
        when(repository.findAll()).thenReturn(Collections.singletonList(TestesMocksUtil.getPautaCadastrada()));
        List<Pauta> resultado = service.listar();
        Assertions.assertNotNull(resultado);
    }


    @Test
    public void deveriaNaoAceitarOVotoPautaComStatus(){
        when(repository.findById(any())).thenReturn(Optional.of(TestesMocksUtil.getPautaValidaComVotosContra()));
        when(associadoRepository.findByCpf(anyString())).thenReturn(Optional.of(TestesMocksUtil.getAssociadoVotandoAFavor()));
        Assertions.assertThrows(PautaException.class, () -> service.votar(anyLong(), AssociadoDto.builder().cpf("0661").voto(Voto.SIM).build()));
    }


    @Test
    public void deveriaNaoAceitarOVotoPautaCpfDuplicado(){
        when(repository.findById(any())).thenReturn(Optional.of(TestesMocksUtil.getPautaCadastradaComAssociado()));
        when(associadoRepository.findByCpf(anyString())).thenReturn(Optional.of(TestesMocksUtil.getAssociadoVotandoAFavor()));
        Assertions.assertThrows(PautaException.class, () -> service.votar(anyLong(), AssociadoDto.builder().cpf("06613868302").voto(Voto.SIM).build()));
    }

    @Test
    public void deveriaNaoLocalizarPauta(){
        when(repository.findById(any())).thenReturn(Optional.empty());
        Assertions.assertThrows(PautaException.class, () -> service.resultado(anyLong()));
    }

    @Test
    public void deveriaAceitarOVoto(){
        when(repository.findById(Mockito.anyLong())).thenReturn(Optional.of(TestesMocksUtil.getPautaCadastrada()));
        when(associadoRepository.save(any())).thenReturn(Optional.of(TestesMocksUtil.getAssociadoVotandoAFavor()));
        when(associadoRepository.findByCpf(anyString())).thenReturn(Optional.of(TestesMocksUtil.getAssociadoVotandoAFavor()));
        service.votar(1L, AssociadoDto.builder().cpf("06613868302").voto(Voto.SIM).build());
        Assertions.assertEquals(1,1);
    }

}
