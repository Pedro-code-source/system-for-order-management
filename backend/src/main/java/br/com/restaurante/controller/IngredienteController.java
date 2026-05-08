package br.com.restaurante.controller;

import br.com.restaurante.dtos.DadosCadastroIngrediente;
import br.com.restaurante.dtos.DadosListagemIngrediente;
import br.com.restaurante.model.Ingrediente;
import br.com.restaurante.service.IngredienteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;


@RestController
@RequestMapping("/ingredientes")
@RequiredArgsConstructor
public class IngredienteController {

    private final IngredienteService ingredienteService;

    @PostMapping
    public ResponseEntity<DadosListagemIngrediente> cadastrar(
            @RequestBody @Valid DadosCadastroIngrediente dto,
            UriComponentsBuilder uriBuilder
    ) {
        Ingrediente ingrediente = ingredienteService.salvar(dto);

        URI uri = uriBuilder
                .path("/ingredientes/{id}")
                .buildAndExpand(ingrediente.getId())
                .toUri();

        return ResponseEntity
                .created(uri)
                .body(new DadosListagemIngrediente(ingrediente));
    }

    @GetMapping
    public ResponseEntity<List<DadosListagemIngrediente>> listar() {
        List<DadosListagemIngrediente> lista = ingredienteService.listarTodos()
                .stream()
                .map(DadosListagemIngrediente::new)
                .toList();
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DadosListagemIngrediente> buscarPorId(@PathVariable Long id) {
        Ingrediente ingrediente = ingredienteService.buscarPorId(id);
        return ResponseEntity.ok(new DadosListagemIngrediente(ingrediente));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DadosListagemIngrediente> atualizar(
            @PathVariable Long id,
            @RequestBody @Valid DadosCadastroIngrediente dto
    ) {
        Ingrediente ingrediente = ingredienteService.atualizar(id, dto);
        return ResponseEntity.ok(new DadosListagemIngrediente(ingrediente));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        ingredienteService.deletarPorId(id);
        return ResponseEntity.noContent().build();
    }
}