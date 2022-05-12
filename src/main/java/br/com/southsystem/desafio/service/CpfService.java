package br.com.southsystem.desafio.service;

import br.com.southsystem.desafio.model.dto.AssociadoValidoDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

@Service
public class CpfService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CpfService.class);

    boolean cpfValidoParaVotar(String cpf){
        RestTemplate client = new RestTemplate();
        LOGGER.info("Indo na api de validacao de CPF");
        ResponseEntity<AssociadoValidoDto> response =
                client.exchange("https://user-info.herokuapp.com/users/"+cpf, HttpMethod.GET,
                        null, AssociadoValidoDto.class);
        LOGGER.info("Resultado recuperado");
        return Objects.requireNonNull(response.getBody()).getStatus().equals("ABLE_TO_VOTE");
    }
}
