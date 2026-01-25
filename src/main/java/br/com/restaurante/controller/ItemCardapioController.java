package br.com.restaurante.controller;

import br.com.restaurante.dtos.DadosCadastroItem;
import br.com.restaurante.dtos.DadosListagemItem;
import br.com.restaurante.model.ItemCardapio;
import br.com.restaurante.repository.ItemCardapioRepository;
import br.com.restaurante.service.ItemCardapioService;
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
    @Autowired private ItemCardapioRepository itemCardapioRepository;

    @PostMapping
    public ResponseEntity cadastrar(@RequestBody DadosCadastroItem dados, UriComponentsBuilder uriComponentsBuilder) {

        ItemCardapio itemCardapio = new ItemCardapio();
        itemCardapio.setNome(dados.nome());
        itemCardapio.setDescricao(dados.descricao());
        itemCardapio.setPreco(dados.preco());
        itemCardapio.setCategoria(dados.categoria());
        itemCardapio.setUrlFoto(dados.urlFoto());

        itemCardapioService.salvar(itemCardapio);

        URI uri = uriComponentsBuilder.path("/itens/{id}").buildAndExpand(itemCardapio.getId()).toUri();

        return ResponseEntity.created(uri).body("Item Cadastrado com ID: " + itemCardapio.getId() + " e nome: " + itemCardapio.getNome());
    }

    @GetMapping
    public ResponseEntity<List<DadosListagemItem>> listar() {
        var lista = itemCardapioRepository.findAll();
        var listaDto = lista.stream().map(DadosListagemItem::new).toList();
        return ResponseEntity.ok(listaDto);
    }
}
