package br.com.restaurante.controller;

import br.com.restaurante.dtos.DadosListagemEntrega;
import br.com.restaurante.model.Entrega;
import br.com.restaurante.service.EntregaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/entregas")
@RequiredArgsConstructor
public class EntregaController {

    private final EntregaService entregaService;

    @GetMapping
    public ResponseEntity<List<DadosListagemEntrega>> listar() {
        List<DadosListagemEntrega> lista = entregaService.listarTodos()
                .stream().map(DadosListagemEntrega::new).toList();
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DadosListagemEntrega> buscarPorId(@PathVariable Long id) {
        Entrega entrega = entregaService.buscarPorId(id);
        return ResponseEntity.ok(new DadosListagemEntrega(entrega));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DadosListagemEntrega> atualizar(@PathVariable Long id, @RequestBody Entrega dados) {
        Entrega atualizada = entregaService.atualizar(id, dados);
        return ResponseEntity.ok(new DadosListagemEntrega(atualizada));
    }

    @PutMapping("/iniciar/{idPedido}")
    public ResponseEntity<Void> iniciarEntrega(@PathVariable Long idPedido) {
        entregaService.iniciarEntrega(idPedido);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/finalizar/{idPedido}")
    public ResponseEntity<Void> finalizarEntrega(@PathVariable Long idPedido) {
        entregaService.finalizarEntrega(idPedido);
        return ResponseEntity.noContent().build();
    }
}