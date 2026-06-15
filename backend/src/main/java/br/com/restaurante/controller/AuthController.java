package br.com.restaurante.controller;

import br.com.restaurante.dtos.LoginDTO;
import br.com.restaurante.dtos.DadosUsuarioAutenticado;
import br.com.restaurante.model.Usuario;
import br.com.restaurante.model.Administrador;
import br.com.restaurante.model.Cliente;
import br.com.restaurante.model.Garcom;
import br.com.restaurante.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@CrossOrigin(origins = "*")
@RestController
public class AuthController {

    @Autowired
    private UsuarioRepository repository;

    @PostMapping({"/login", "/auth/login"})
    public ResponseEntity<?> login(@RequestBody LoginDTO dados){

        Optional<Usuario> usuario = repository.findByEmail(dados.getEmail());

        if (usuario.isEmpty()){
            return ResponseEntity.status(401).body("Usuário não encontrado!");
        }

        Usuario usuarioExistente = usuario.get();

        if (!usuarioExistente.getSenha().equals(dados.getSenha())){
            return ResponseEntity.status(401).body("Senha Inválida!");
        }

        String role = "";
        String nome = "";

        if (usuarioExistente instanceof Administrador) {
            role = "admin";
            nome = ((Administrador) usuarioExistente).getNome();
        } else if (usuarioExistente instanceof Cliente) {
            role = "client";
            nome = ((Cliente) usuarioExistente).getNome();
        } else if (usuarioExistente instanceof Garcom) {
            role = "waiter";
            nome = ((Garcom) usuarioExistente).getNome();
        }

        return ResponseEntity.ok(new DadosUsuarioAutenticado(
            usuarioExistente.getId(),
            nome,
            usuarioExistente.getEmail(),
            role
        ));
    }
}
