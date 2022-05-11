package br.com.southsystem.desafio.controller;

import br.com.southsystem.desafio.model.Associado;
import br.com.southsystem.desafio.model.Pauta;
import br.com.southsystem.desafio.service.PautaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

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
    public ResponseEntity<Pauta> cadastrar(@RequestBody Pauta pauta, UriComponentsBuilder uri) {
        return service.cadastrar(pauta, uri);
    }

    @PostMapping("/{id}")
    public ResponseEntity votar (@PathVariable Long id, @RequestBody Associado associado){
        return service.votar(id,associado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deletar(@PathVariable Long id) throws Exception {
        return service.deletar(id);
    }

}
