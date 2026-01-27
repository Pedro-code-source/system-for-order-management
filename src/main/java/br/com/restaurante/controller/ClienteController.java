package br.com.restaurante.controller;

import br.com.restaurante.dtos.*;
import br.com.restaurante.model.Cliente;
import br.com.restaurante.service.ClienteService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
@RestController
@RequestMapping("/clientes")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @PostMapping
    public ResponseEntity<DadosListagemCliente> cadastrar(@RequestBody @Valid DadosCadastroCliente dados, UriComponentsBuilder uriBuilder
    ) {
        Cliente clienteSalvo = clienteService.salvar(dados);

        URI uri = uriBuilder
                .path("/clientes/{id}")
                .buildAndExpand(clienteSalvo.getId())
                .toUri();

        return ResponseEntity
                .created(uri)
                .body(new DadosListagemCliente(clienteSalvo));
    }

    @GetMapping
    public ResponseEntity<List<DadosListagemCliente>> listar() {
        List<DadosListagemCliente> lista = clienteService.listarTodos()
                .stream()
                .map(DadosListagemCliente::new)
                .toList();

        return ResponseEntity.ok(lista);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DadosListagemCliente> buscarPorId(@PathVariable Long id) {
        Cliente cliente = clienteService.buscarPorId(id);
        return ResponseEntity.ok(new DadosListagemCliente(cliente));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DadosListagemCliente> atualizar(
            @PathVariable Long id,
            @RequestBody @Valid DadosCadastroCliente dados
    ) {
        Cliente clienteAtualizado = clienteService.atualizar(id, dados);
        return ResponseEntity.ok(new DadosListagemCliente(clienteAtualizado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        clienteService.deletarPorId(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/fazerPedidoOnline")
    public ResponseEntity<DadosListagemPedidoOnline> fazerPedidoOnline( @RequestBody DadosCadastroPedidoOnline dto){
        clienteService.fazerPedidoOnline(dto);
        return ResponseEntity.status(201).build();
    }

    @PostMapping("/fazerReserva/{idCliente}")
    public ResponseEntity<DadosListagemReserva> fazerReserva(@PathVariable Long idCliente, @RequestBody DadosCadastroReserva dto){
        clienteService.fazerReserva(idCliente, dto);
        return ResponseEntity.status(201).build();
    }

    @DeleteMapping("/cancelarReserva/{reservaId}")
    public ResponseEntity<Void> cancelarReserva (@PathVariable Long reservaId){
        clienteService.cancelarReserva(reservaId);
        return ResponseEntity.noContent().build();
    }
}
