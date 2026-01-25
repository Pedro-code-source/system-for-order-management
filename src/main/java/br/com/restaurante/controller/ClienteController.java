package br.com.restaurante.controller;

import br.com.restaurante.dtos.DadosCadastroCliente;
import br.com.restaurante.dtos.DadosListagemCliente;
import br.com.restaurante.model.Cliente;
import br.com.restaurante.model.Endereco;
import br.com.restaurante.repository.ClienteRepository;
import br.com.restaurante.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/clientes")
public class ClienteController {

    @Autowired private ClienteService clienteService;
    @Autowired private ClienteRepository clienteRepository;

    @PostMapping
    public ResponseEntity cadastrar(@RequestBody DadosCadastroCliente dados, UriComponentsBuilder uriComponentsBuilder) {

        Cliente cliente = new Cliente();
        cliente.setEmail(dados.email());
        cliente.setSenha(dados.senha());
        cliente.setNome(dados.nome());
        cliente.setTelefone(dados.telefone());

        Endereco endereco = new Endereco();

        endereco.setCep(dados.endereco().cep());
        endereco.setNumero(dados.endereco().numero());
        endereco.setRua(dados.endereco().rua());
        endereco.setCidade(dados.endereco().cidade());
        endereco.setBairro(dados.endereco().bairro());

        cliente.setEndereco(endereco);

        clienteService.salvar(cliente);

        URI uri = uriComponentsBuilder.path("/clientes/{id}").buildAndExpand(cliente.getId()).toUri();

        return ResponseEntity.created(uri).body("Cliente Cadastrado com ID: " + cliente.getId());
    }

    @GetMapping
    public ResponseEntity<List<DadosListagemCliente>> listar() {
        var lista = clienteRepository.findAll();
        var listaDto = lista.stream().map(DadosListagemCliente::new).toList();
        return ResponseEntity.ok(listaDto);
    }
}
