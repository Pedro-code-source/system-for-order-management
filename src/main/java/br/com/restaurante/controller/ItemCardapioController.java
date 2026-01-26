package br.com.restaurante.controller;

import br.com.restaurante.dtos.DadosCadastroItem;
import br.com.restaurante.dtos.DadosListagemItem;
import br.com.restaurante.model.ItemCardapio;
import br.com.restaurante.service.ItemCardapioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/itens")
public class ItemCardapioController {

    @Autowired private ItemCardapioService itemCardapioService;

    @PostMapping
    public ResponseEntity<DadosListagemItem> cadastrar(@RequestBody @Valid DadosCadastroItem dados, UriComponentsBuilder uriBuilder) {
        ItemCardapio itemCardapio = new ItemCardapio(dados);

        ItemCardapio itemCardapioSalvo = itemCardapioService.salvar(itemCardapio);

        URI uri = uriBuilder.path("/itens/{id}").buildAndExpand(itemCardapioSalvo.getId()).toUri();

        return ResponseEntity.created(uri).body(new DadosListagemItem(itemCardapioSalvo));
    }

    @GetMapping
    public  ResponseEntity<List<DadosListagemItem>> listar() {
        var lista = itemCardapioService.listarTodos().stream()
                .map(DadosListagemItem::new)
                .toList();

        return ResponseEntity.ok(lista);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DadosListagemItem> buscarPorId(@PathVariable Long id) {
        ItemCardapio itemCardapio = itemCardapioService.buscarPorId(id);

        return ResponseEntity.ok(new DadosListagemItem(itemCardapio));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DadosListagemItem> atualizar(@PathVariable Long id, @RequestBody @Valid DadosCadastroItem dados) {
        ItemCardapio novosDados = new ItemCardapio(dados);

        ItemCardapio itemAtualizado = itemCardapioService.atualizar(id, novosDados);

        return ResponseEntity.ok(new DadosListagemItem(itemAtualizado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        itemCardapioService.deletarPorId(id);

        return ResponseEntity.noContent().build();
    }
}
