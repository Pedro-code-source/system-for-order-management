package br.com.restaurante.controller;

import br.com.restaurante.dtos.DadosCadastroMesa;
import br.com.restaurante.dtos.DadosListagemMesa;
import br.com.restaurante.model.Mesa;
import br.com.restaurante.service.MesaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/mesas")
@RequiredArgsConstructor
public class MesaController {

    private final MesaService mesaService;

    @PostMapping
    public ResponseEntity<DadosListagemMesa> cadastrar(@RequestBody @Valid DadosCadastroMesa dados, UriComponentsBuilder uriBuilder) {
        Mesa mesa = new Mesa(dados);
        Mesa mesaSalva = mesaService.salvar(mesa);
        URI uri = uriBuilder.path("/mesas/{id}").buildAndExpand(mesaSalva.getId()).toUri();
        return ResponseEntity.created(uri).body(new DadosListagemMesa(mesaSalva));
    }

    @GetMapping
    public ResponseEntity<List<DadosListagemMesa>> listar() {
        List<DadosListagemMesa> lista = mesaService.listarTodos().stream().map(DadosListagemMesa::new).toList();
        return ResponseEntity.ok(lista);
    }
}