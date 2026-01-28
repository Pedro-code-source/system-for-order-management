package br.com.restaurante.controller;

import br.com.restaurante.dtos.*;
import br.com.restaurante.model.Administrador;
import br.com.restaurante.model.Ingrediente;
import br.com.restaurante.service.AdministradorService;
import br.com.restaurante.service.ReservaService; // <--- Import Novo
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/administradores")
@RequiredArgsConstructor
public class AdministradorController {

    private final AdministradorService administradorService;
    private final ReservaService reservaService; // <--- Injetamos para cancelar reservas

    @PostMapping
    public ResponseEntity<DadosListagemAdmin> cadastrar(@RequestBody @Valid DadosCadastroAdmin dados, UriComponentsBuilder uriBuilder) {
        Administrador novoAdm = new Administrador(dados);

        Administrador salvo = administradorService.salvar(novoAdm);

        URI uri = uriBuilder.path("/administradores/{id}").buildAndExpand(salvo.getId()).toUri();
        return ResponseEntity.created(uri).body(new DadosListagemAdmin(salvo));
    }

    @GetMapping
    public ResponseEntity<List<DadosListagemAdmin>> listar() {
        List<Administrador> lista = administradorService.listarTodos();
        List<DadosListagemAdmin> dto = lista.stream().map(DadosListagemAdmin::new).toList();
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DadosListagemAdmin> buscarPorId(@PathVariable Long id) {
        Administrador adm = administradorService.buscarPorId(id);
        return ResponseEntity.ok(new DadosListagemAdmin(adm));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DadosListagemAdmin> atualizar(@PathVariable Long id, @RequestBody @Valid DadosCadastroAdmin dados) {
        Administrador admAtualizado = new Administrador(dados);

        Administrador salvo = administradorService.atualizar(id, admAtualizado);
        return ResponseEntity.ok(new DadosListagemAdmin(salvo));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        administradorService.deletarPorId(id);
        return ResponseEntity.noContent().build();
    }
}