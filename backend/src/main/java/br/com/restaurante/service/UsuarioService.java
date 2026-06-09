package br.com.restaurante.service;

import br.com.restaurante.model.Usuario;
import br.com.restaurante.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {
    private UsuarioRepository usuarioService;

    @Transactional
    public List<Usuario> listarTodos(){return usuarioService.findAll();};
}

