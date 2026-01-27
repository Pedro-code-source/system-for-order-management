package br.com.restaurante.controller;

import br.com.restaurante.dtos.*;
import br.com.restaurante.model.Cliente;
import br.com.restaurante.model.Reserva;
import br.com.restaurante.service.ClienteService;
import br.com.restaurante.service.ReservaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/reservas")
public class ReservaController {

    @Autowired
    private ReservaService reservaService;

    @Autowired
    private ClienteService clienteService;


    @GetMapping
    public ResponseEntity<List<DadosListagemReserva>> listar() {
        List<DadosListagemReserva> lista = reservaService.listarTodos()
                .stream()
                .map(DadosListagemReserva::new)
                .toList();

        return ResponseEntity.ok(lista);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DadosListagemReserva> buscarPorId(@PathVariable Long id) {
        Reserva reserva = reservaService.buscarPorId(id);
        return ResponseEntity.ok(new DadosListagemReserva(reserva));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DadosListagemReserva> atualizar(@PathVariable Long id, @RequestBody @Valid DadosCadastroReserva dados
    ) {
        Reserva reserva = reservaService.atualizar(id, dados);
        return ResponseEntity.ok(new DadosListagemReserva(reserva));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        reservaService.deletarPorId(id);
        return ResponseEntity.noContent().build();
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
