package br.com.restaurante.controller;

import br.com.restaurante.dtos.DadosRegistroMovimentacao;
import br.com.restaurante.model.Ingrediente;
import br.com.restaurante.model.MovimentacaoDeEstoque;
import br.com.restaurante.service.IngredienteService;
import br.com.restaurante.service.MovimentacaoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/movimentacoes")
@RequiredArgsConstructor
public class MovimentacaoController {

    private final MovimentacaoService movimentacaoService;
    private final IngredienteService ingredienteService;

    @GetMapping
    public ResponseEntity<List<MovimentacaoDeEstoque>> listarHistorico() {
        return ResponseEntity.ok(movimentacaoService.listarTodos());
    }

    @PostMapping("/entrada")
    public ResponseEntity<Void> registrarEntrada(@RequestBody @Valid DadosRegistroMovimentacao dados) {
        Ingrediente ingrediente = ingredienteService.buscarPorId(dados.idIngrediente());

        movimentacaoService.registrarEntrada(ingrediente, dados.quantidade());

        return ResponseEntity.ok().build();
    }

    @PostMapping("/saida")
    public ResponseEntity<Void> registrarSaida(@RequestBody @Valid DadosRegistroMovimentacao dados) {
        Ingrediente ingrediente = ingredienteService.buscarPorId(dados.idIngrediente());

        movimentacaoService.registrarSaida(ingrediente, dados.quantidade());

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarRegistroHistorico(@PathVariable Long id) {
        movimentacaoService.deletarPorId(id);
        return ResponseEntity.noContent().build();
    }
}