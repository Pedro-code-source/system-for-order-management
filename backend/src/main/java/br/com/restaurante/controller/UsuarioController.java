package br.com.restaurante.controller;

import br.com.restaurante.model.Usuario;
import br.com.restaurante.repository.UsuarioRepository;
import br.com.restaurante.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin (origins = "*")
@RestController
@RequestMapping("/usuarios")
public class UsuarioController {
    @Autowired
    private UsuarioRepository repository;

    @PostMapping
    public ResponseEntity<?> cadastrar(@RequestBody Usuario dados){
        Optional<Usuario> usuarioExistente = repository.findByEmail(dados.getEmail());

        if(usuarioExistente.isPresent()){
            return ResponseEntity.badRequest().body("Email já cadastrado!");
        }
        Usuario novoUsuario = repository.save(dados);

        return ResponseEntity.ok(novoUsuario);

    }

    @GetMapping
    public ResponseEntity<List<Usuario>> listar(){
        List<Usuario> lista = repository.findAll();

        return ResponseEntity.ok(lista);
    }
}
