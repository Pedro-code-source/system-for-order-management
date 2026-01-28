package br.com.restaurante.controller;

import br.com.restaurante.dtos.DadosCadastroPedidoPresencial;
import br.com.restaurante.dtos.DadosListagemPedidoPresencial;
import br.com.restaurante.model.PedidoPresencial;
import br.com.restaurante.service.GarcomService;
import br.com.restaurante.service.PedidoPresencialService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/pedidosPresenciais")
@RequiredArgsConstructor
public class PedidoPresencialController {

    private final PedidoPresencialService pedidoPresencialService;
    private final GarcomService garcomService;

    @PostMapping
    public ResponseEntity<DadosListagemPedidoPresencial> cadastrar(@RequestBody @Valid DadosCadastroPedidoPresencial dto, UriComponentsBuilder uriBuilder){
        PedidoPresencial pedido = garcomService.registrarPedidoPresencial(dto);
        URI uri = uriBuilder.path("/pedidosPresenciais/{id}").buildAndExpand(pedido.getId()).toUri();
        return ResponseEntity.created(uri).body(new DadosListagemPedidoPresencial(pedido));
    }

    @GetMapping
    public ResponseEntity<List<DadosListagemPedidoPresencial>> listar() {
        List<DadosListagemPedidoPresencial> lista = pedidoPresencialService.listarTodos()
                .stream().map(DadosListagemPedidoPresencial::new).toList();
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DadosListagemPedidoPresencial> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(new DadosListagemPedidoPresencial(pedidoPresencialService.buscarPorId(id)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DadosListagemPedidoPresencial> atualizar(@PathVariable Long id, @RequestBody @Valid DadosCadastroPedidoPresencial dto) {
        PedidoPresencial pedido = pedidoPresencialService.atualizar(id, dto);
        return ResponseEntity.ok(new DadosListagemPedidoPresencial(pedido));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        pedidoPresencialService.deletarPorId(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/finalizar")
    public ResponseEntity<Void> finalizarPedido(@PathVariable Long id) {
        pedidoPresencialService.finalizarPedido(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}/cancelar")
    public ResponseEntity<Void> cancelarPedido(@PathVariable Long id) {
        pedidoPresencialService.cancelarPedido(id);
        return ResponseEntity.noContent().build();
    }
}