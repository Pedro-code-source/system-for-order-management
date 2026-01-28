package br.com.restaurante.controller;

import br.com.restaurante.dtos.*;
import br.com.restaurante.model.PedidoOnline;
import br.com.restaurante.service.PedidoOnlineService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/pedidosOnline")
@RequiredArgsConstructor
public class PedidoOnlineController {

    private final PedidoOnlineService pedidoOnlineService;

    @PostMapping
    public ResponseEntity<DadosListagemPedidoOnline> cadastrar(@RequestBody @Valid DadosCadastroPedidoOnline dto, UriComponentsBuilder uriBuilder) {
        PedidoOnline pedido = pedidoOnlineService.cadastrar(dto);
        URI uri = uriBuilder.path("/pedidosOnline/{id}").buildAndExpand(pedido.getId()).toUri();
        return ResponseEntity.created(uri).body(new DadosListagemPedidoOnline(pedido));
    }

    @GetMapping
    public ResponseEntity<List<DadosListagemPedidoOnline>> listar() {
        List<DadosListagemPedidoOnline> lista = pedidoOnlineService.listarTodos()
                .stream().map(DadosListagemPedidoOnline::new).toList();
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DadosListagemPedidoOnline> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(new DadosListagemPedidoOnline(pedidoOnlineService.buscarPorId(id)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DadosListagemPedidoOnline> atualizar(@PathVariable Long id, @RequestBody @Valid DadosCadastroPedidoOnline dto) {
        PedidoOnline pedido = pedidoOnlineService.atualizar(id, dto);
        return ResponseEntity.ok(new DadosListagemPedidoOnline(pedido));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        pedidoOnlineService.deletarPorId(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/finalizar")
    public ResponseEntity<Void> finalizarPedido(@PathVariable Long id) {
        pedidoOnlineService.finalizarPedido(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}/cancelar")
    public ResponseEntity<Void> cancelarPedido(@PathVariable Long id) {
        pedidoOnlineService.cancelarPedido(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/confirmarEndereco")
    public ResponseEntity<Void> confirmarEndereco(@PathVariable Long id) {
        pedidoOnlineService.confirmarEndereco(id);
        return ResponseEntity.noContent().build();
    }
}