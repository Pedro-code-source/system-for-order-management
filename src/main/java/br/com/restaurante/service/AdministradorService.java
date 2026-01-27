package br.com.restaurante.service;

import br.com.restaurante.dtos.DadosCadastroIngrediente;
import br.com.restaurante.model.*;
import br.com.restaurante.model.enums.CategoriaItem;
import br.com.restaurante.model.enums.StatusMesa;
import br.com.restaurante.model.enums.StatusReserva;
import br.com.restaurante.model.enums.TipoMovimentacao;
import br.com.restaurante.repository.AdministradorRepository;
import br.com.restaurante.repository.MesaRepository;
import br.com.restaurante.repository.MovimentacaoRepository;
import br.com.restaurante.repository.ReservaRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
@Service
public class AdministradorService {

    private final AdministradorRepository administradorRepository;
    private final MovimentacaoRepository movimentacaoRepository;
    private final ItemCardapioService itemCardapioService;
    private final IngredienteService ingredienteService;
    private final ReservaRepository reservaRepository;
    private final MesaRepository mesaRepository;

    @Transactional
    public Administrador salvar(Administrador adm){
        return administradorRepository.save(adm);
    }

    @Transactional
    public void deletarPorId(Long id){
        if (administradorRepository.existsById(id)){
            administradorRepository.deleteById(id);
        }
        else {
            throw new RuntimeException("O administrador com ID " + id + " não existe");
        }
    }

    @Transactional
    public List<Administrador> listarTodos(){
        return administradorRepository.findAll();
    }

    @Transactional
    public Administrador buscarPorId(Long id){
        return administradorRepository.findById(id).orElseThrow(()-> new RuntimeException("O administrador não existe."));
    }

    @Transactional
    public Administrador atualizar(Long id, Administrador novoAdm){
        Administrador existente = buscarPorId(id);
        if (existente != null){
            existente.setEmail(novoAdm.getEmail());
            existente.setSenha(novoAdm.getSenha());

            return administradorRepository.save(existente);
        }
        else {
            throw new RuntimeException("Administrador com ID " + id + " não existe.");
        }
    }
    @Transactional
    public Ingrediente cadastrarIngrediente(DadosCadastroIngrediente dto){
        return ingredienteService.salvar(dto);
    }

    @Transactional
    public void registrarMovimentacao(Ingrediente ingrediente, int quantidade, TipoMovimentacao tipo){

        MovimentacaoDeEstoque movimentacaoDeEstoque = new MovimentacaoDeEstoque();
        movimentacaoDeEstoque.setTipoMovimentacao(tipo);
        movimentacaoDeEstoque.setIngrediente(ingrediente);
        movimentacaoDeEstoque.setQuantidade(quantidade);
        movimentacaoDeEstoque.setDataCriacao(LocalDateTime.now());

        movimentacaoRepository.save(movimentacaoDeEstoque);
    }

    @Transactional
    public void cadastrarItemCardapio(String nome, Double preco, Map<String, Integer> receita, String descricao, CategoriaItem categoria, String urlFoto){

        ItemCardapio itemCardapio = new ItemCardapio();
        itemCardapio.setCategoria(categoria);
        itemCardapio.setNome(nome);
        itemCardapio.setDescricao(descricao);
        itemCardapio.setUrlFoto(urlFoto);
        itemCardapio.setPreco(preco);

        itemCardapioService.salvar(itemCardapio);
    }

    @Transactional
    public void cancelarReserva(Long idReserva) {

        Reserva reserva = reservaRepository.findById(idReserva)
                .orElseThrow(() -> new RuntimeException("Reserva não encontrada."));

        if (reserva.getStatus() != StatusReserva.CONFIRMADA) {
            throw new RuntimeException("Esta reserva já está cancelada ou finalizada.");
        }
        reserva.setStatus(StatusReserva.CANCELADA);
        reserva.getMesa().setStatus(StatusMesa.LIVRE);
        reservaRepository.save(reserva);
        mesaRepository.save(reserva.getMesa());
    }
}
