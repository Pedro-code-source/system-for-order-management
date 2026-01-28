package br.com.restaurante.controller;

import br.com.restaurante.dtos.DadosCadastroGarcom;
import br.com.restaurante.dtos.DadosListagemGarcom;
import br.com.restaurante.model.Garcom;
import br.com.restaurante.service.GarcomService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/garcons")
public class GarcomController {

    @Autowired
    private GarcomService garcomService;

    @PostMapping
    public ResponseEntity<DadosListagemGarcom> cadastrar(
            @RequestBody @Valid DadosCadastroGarcom dto,
            UriComponentsBuilder uriBuilder
    ) {
        Garcom garcom = garcomService.salvar(dto);

        URI uri = uriBuilder
                .path("/garcons/{id}")
                .buildAndExpand(garcom.getId())
                .toUri();

        return ResponseEntity
                .created(uri)
                .body(new DadosListagemGarcom(garcom));
    }

    @GetMapping
    public ResponseEntity<List<DadosListagemGarcom>> listar() {
        List<DadosListagemGarcom> lista = garcomService.listarTodos()
                .stream()
                .map(DadosListagemGarcom::new)
                .toList();

        return ResponseEntity.ok(lista);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DadosListagemGarcom> buscarPorId(@PathVariable Long id) {
        Garcom garcom = garcomService.buscarPorId(id);
        return ResponseEntity.ok(new DadosListagemGarcom(garcom));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DadosListagemGarcom> atualizar(
            @PathVariable Long id,
            @RequestBody @Valid DadosCadastroGarcom dto
    ) {
        Garcom garcomAtualizado = garcomService.atualizar(id, dto);
        return ResponseEntity.ok(new DadosListagemGarcom(garcomAtualizado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        garcomService.deletarPorId(id);
        return ResponseEntity.noContent().build();
    }
}