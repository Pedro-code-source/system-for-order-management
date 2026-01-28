package br.com.restaurante.controller;

import br.com.restaurante.dtos.*;
import br.com.restaurante.model.Reserva;
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

    @PostMapping
    public ResponseEntity<DadosListagemReserva> cadastrar(@RequestBody @Valid DadosCadastroReserva dados, UriComponentsBuilder uriBuilder) {
        Reserva reserva = reservaService.cadastrar(dados);

        URI uri = uriBuilder.path("/reservas/{id}").buildAndExpand(reserva.getId()).toUri();
        return ResponseEntity.created(uri).body(new DadosListagemReserva(reserva));
    }

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
    public ResponseEntity<DadosListagemReserva> atualizar(@PathVariable Long id, @RequestBody @Valid DadosCadastroReserva dados) {
        Reserva reserva = reservaService.atualizar(id, dados);
        return ResponseEntity.ok(new DadosListagemReserva(reserva));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        reservaService.deletarPorId(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}/cancelar")
    public ResponseEntity<Void> cancelar(@PathVariable Long id) {
        reservaService.cancelar(id);
        return ResponseEntity.noContent().build();
    }
}