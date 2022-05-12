package br.com.southsystem.desafio.service;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("Serviço de Pautas")
@ExtendWith(MockitoExtension.class)
public class CpfServiceTestes {

    @InjectMocks
    private CpfService cpfService;

    @Test
    public void deveriaRetornarTrue(){
        Assertions.assertTrue(cpfService.cpfValidoParaVotar("06613868302"));
    }
}
