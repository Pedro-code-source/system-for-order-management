package br.com.restaurante.controller;

import br.com.restaurante.dtos.DadosCadastroPedidoOnline;
import br.com.restaurante.dtos.DadosCadastroPedidoPresencial;
import br.com.restaurante.dtos.DadosListagemPedidoOnline;
import br.com.restaurante.dtos.DadosListagemPedidoPresencial;
import br.com.restaurante.model.PedidoOnline;
import br.com.restaurante.model.PedidoPresencial;
import br.com.restaurante.service.GarcomService;
import br.com.restaurante.service.PedidoPresencialService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pedidosPresenciais")
public class PedidoPresencialController {

    @Autowired
    private PedidoPresencialService pedidoPresencialService;

    @Autowired
    private GarcomService garcomService;

    @PostMapping("/fazerPedidoPresencial")
    public ResponseEntity<DadosCadastroPedidoPresencial> fazerPedidoOnline(@RequestBody DadosCadastroPedidoPresencial dto){
        garcomService.registrarPedidoPresencial(dto);
        return ResponseEntity.status(201).build();
    }


    @GetMapping
    public ResponseEntity<List<DadosListagemPedidoPresencial>> listar() {
        List<DadosListagemPedidoPresencial> lista = pedidoPresencialService.listarTodos()
                .stream()
                .map(DadosListagemPedidoPresencial::new)
                .toList();

        return ResponseEntity.ok(lista);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DadosListagemPedidoPresencial> buscarPorId(@PathVariable Long id) {
        PedidoPresencial pedido = pedidoPresencialService.buscarPorId(id);
        return ResponseEntity.ok(new DadosListagemPedidoPresencial(pedido));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DadosListagemPedidoPresencial> atualizar(
            @PathVariable Long id,
            @RequestBody @Valid DadosCadastroPedidoPresencial dto
    ) {
        PedidoPresencial pedido = pedidoPresencialService.atualizar(id, dto);
        return ResponseEntity.ok(new DadosListagemPedidoPresencial(pedido));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        pedidoPresencialService.deletarPorId(id);
        return ResponseEntity.noContent().build();
    }

}



