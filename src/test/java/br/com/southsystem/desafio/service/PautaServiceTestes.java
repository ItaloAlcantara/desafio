package br.com.southsystem.desafio.service;

import br.com.southsystem.desafio.model.Associado;
import br.com.southsystem.desafio.model.Pauta;
import br.com.southsystem.desafio.model.dto.PautaResultado;
import br.com.southsystem.desafio.model.enumerador.Voto;
import br.com.southsystem.desafio.repository.AssociadoRepository;
import br.com.southsystem.desafio.repository.PautaRepository;
import br.com.southsystem.desafio.util.TestesMocksUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.junit.jupiter.api.Assertions;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("Serviço de Pautas")
public class PautaServiceTestes {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PautaService service;

    @MockBean
    private PautaRepository repository;

    @MockBean
    private AssociadoRepository associadoRepository;

    @Test
    public void deveriaTrazerResultadoAprovadoComUmVoto(){

        when(repository.findById(Mockito.anyLong())).thenReturn(Optional.of(TestesMocksUtil.getPautaValidaComVotosAFavor()));
        ResponseEntity<PautaResultado> resultado = service.resultado(Mockito.anyLong());
        Assertions.assertEquals(Objects.requireNonNull(resultado.getBody()).getVotosPositivos(),1);
    }

}
