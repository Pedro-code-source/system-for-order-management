package br.com.restaurante.service;

import br.com.restaurante.dtos.DadosCadastroCliente;
import br.com.restaurante.model.Cliente;
import br.com.restaurante.model.Endereco;
import br.com.restaurante.repository.ClienteRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;
    private final EnderecoService enderecoService;

    @Transactional
    public Cliente salvar(DadosCadastroCliente dto) {
        if (clienteRepository.existsByEmail(dto.email())) {
            throw new RuntimeException("E-mail já cadastrado.");
        }
        Endereco endereco = enderecoService.salvar(dto.endereco());

        Cliente cliente = new Cliente(dto);
        cliente.setEndereco(endereco);
        return clienteRepository.save(cliente);
    }

    @Transactional
    public void deletarPorId(Long id) {
        if (!clienteRepository.existsById(id)) {
            throw new RuntimeException("Cliente não encontrado para deletar.");
        }
        try {
            clienteRepository.deleteById(id);
            clienteRepository.flush();
        } catch (DataIntegrityViolationException e) {
            throw new RuntimeException("Não é possível excluir o cliente pois ele possui pedidos ou reservas vinculados.");
        }
    }

    public List<Cliente> listarTodos() {
        return clienteRepository.findAll();
    }

    public Cliente buscarPorId(Long id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado com o ID: " + id));
    }

    @Transactional
    public Cliente atualizar(Long id, DadosCadastroCliente clienteAtualizado) {
        Cliente cliente = buscarPorId(id);

        Optional<Cliente> clienteComEsseEmail = clienteRepository.findByEmail(clienteAtualizado.email());
        if (clienteComEsseEmail.isPresent() && !clienteComEsseEmail.get().getId().equals(id)) {
            throw new RuntimeException("O e-mail já pertence a outro cliente.");
        }

        cliente.setNome(clienteAtualizado.nome());
        cliente.setEmail(clienteAtualizado.email());
        cliente.setSenha(clienteAtualizado.senha());
        cliente.setTelefone(clienteAtualizado.telefone());

        if (clienteAtualizado.endereco() != null) {
            Endereco enderecoNovo = cliente.getEndereco();
            enderecoNovo.setRua(clienteAtualizado.endereco().rua());
            enderecoNovo.setBairro(clienteAtualizado.endereco().numero());
            enderecoNovo.setCep(clienteAtualizado.endereco().bairro());
            enderecoNovo.setCidade(clienteAtualizado.endereco().cidade());
            enderecoNovo.setNumero(clienteAtualizado.endereco().cep());

        }

        return clienteRepository.save(cliente);
    }
}