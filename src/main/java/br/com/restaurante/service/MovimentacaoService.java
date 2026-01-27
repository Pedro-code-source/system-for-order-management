package br.com.restaurante.service;

import br.com.restaurante.dtos.DadosCadastroIngrediente;
import br.com.restaurante.model.Ingrediente;
import br.com.restaurante.model.MovimentacaoDeEstoque;
import br.com.restaurante.model.enums.TipoMovimentacao;
import br.com.restaurante.repository.IngredienteRepository;
import br.com.restaurante.repository.MovimentacaoRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@Service
public class MovimentacaoService {

    private final MovimentacaoRepository movimentacaoRepository;
    private final IngredienteService ingredienteService;
    private final IngredienteRepository ingredienteRepository;

    @Transactional
    public MovimentacaoDeEstoque salvar(MovimentacaoDeEstoque objeto) {
        return movimentacaoRepository.save(objeto);
    }

    @Transactional
    public List<MovimentacaoDeEstoque> listarTodos() {
        return movimentacaoRepository.findAll();
    }

    @Transactional
    public MovimentacaoDeEstoque buscarPorId(Long id) {
        return movimentacaoRepository.findById(id).orElseThrow(() -> new RuntimeException("Movimentação não encontrada"));
    }

    @Transactional
    public void deletarPorId(Long id){
        movimentacaoRepository.deleteById(id);
    }

    @Transactional
    public void registrarEntrada(Ingrediente ingrediente, int quantidade){
        Ingrediente existente = ingredienteService.buscarPorId(ingrediente.getId());

        MovimentacaoDeEstoque movimentacao = new MovimentacaoDeEstoque();
        movimentacao.setTipoMovimentacao(TipoMovimentacao.ENTRADA);
        movimentacao.setIngrediente(ingrediente);
        movimentacao.setQuantidade(quantidade);
        movimentacao.setDataCriacao(LocalDateTime.now());


        existente.setQuantidade(existente.getQuantidade() + quantidade);
        ingredienteRepository.save(existente);

        movimentacaoRepository.save(movimentacao);
    }

    @Transactional
    public void registrarSaida(Ingrediente ingrediente, int quantidade){
        Ingrediente existente = ingredienteService.buscarPorId(ingrediente.getId());

        MovimentacaoDeEstoque movimentacao = new MovimentacaoDeEstoque();
        movimentacao.setTipoMovimentacao(TipoMovimentacao.SAIDA);
        movimentacao.setIngrediente(ingrediente);
        movimentacao.setQuantidade(quantidade);
        movimentacao.setDataCriacao(LocalDateTime.now());

        if (existente.getQuantidade() >= quantidade){
            existente.setQuantidade(existente.getQuantidade() - quantidade);
            ingredienteRepository.save(existente);
        }
        else {
            throw new RuntimeException("Quantidade indisponível em estoque.");
        }
        movimentacaoRepository.save(movimentacao);
    }
}
