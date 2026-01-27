package br.com.restaurante.controller;

import br.com.restaurante.dtos.*;
import br.com.restaurante.model.Cliente;
import br.com.restaurante.model.PedidoOnline;
import br.com.restaurante.service.ClienteService;
import br.com.restaurante.service.PedidoOnlineService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/pedidosOnline")
public class PedidoOnlineController {

    @Autowired
    private PedidoOnlineService pedidoOnlineService;

    @Autowired
    private ClienteService clienteService;

    @PostMapping("/fazerPedidoOnline")
    public ResponseEntity<DadosListagemPedidoOnline> fazerPedidoOnline(@RequestBody DadosCadastroPedidoOnline dto){
        clienteService.fazerPedidoOnline(dto);
        return ResponseEntity.status(201).build();
    }


    @GetMapping
    public ResponseEntity<List<DadosListagemPedidoOnline>> listar() {
        List<DadosListagemPedidoOnline> lista = pedidoOnlineService.listarTodos()
                .stream()
                .map(DadosListagemPedidoOnline::new)
                .toList();

        return ResponseEntity.ok(lista);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DadosListagemPedidoOnline> buscarPorId(@PathVariable Long id) {
        PedidoOnline pedidoOnline = pedidoOnlineService.buscarPorId(id);
        return ResponseEntity.ok(new DadosListagemPedidoOnline(pedidoOnline));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DadosListagemPedidoOnline> atualizar(
            @PathVariable Long id,
            @RequestBody @Valid DadosCadastroPedidoOnline dto
    ) {
        PedidoOnline pedido = pedidoOnlineService.atualizar(id, dto);
        return ResponseEntity.ok(new DadosListagemPedidoOnline(pedido));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        pedidoOnlineService.deletarPorId(id);
        return ResponseEntity.noContent().build();
    }

}
