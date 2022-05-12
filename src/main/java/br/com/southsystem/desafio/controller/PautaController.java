package br.com.southsystem.desafio.controller;

import br.com.southsystem.desafio.model.Associado;
import br.com.southsystem.desafio.model.Pauta;
import br.com.southsystem.desafio.model.dto.AssociadoDto;
import br.com.southsystem.desafio.model.dto.PautaDto;
import br.com.southsystem.desafio.service.PautaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pauta")
public class PautaController {

    @Autowired
    private PautaService service;

    @GetMapping
    public List<Pauta> listar() {
        return service.listar();
    }

    @PostMapping
    public ResponseEntity<PautaDto> cadastrar(@RequestBody PautaDto pauta) {
        return service.cadastrar(pauta);
    }

    @PostMapping("/{id}")
    public ResponseEntity votar (@PathVariable Long id, @RequestBody @Validated AssociadoDto associado){
        return service.votar(id,associado);
    }

    @GetMapping("/{id}")
    public ResponseEntity resultado (@PathVariable Long id){
        return service.resultado(id);
    }
}
