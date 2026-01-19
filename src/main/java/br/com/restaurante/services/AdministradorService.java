package br.com.restaurante.services;

import br.com.restaurante.model.Administrador;
import br.com.restaurante.model.Ingrediente;
import br.com.restaurante.model.ItemCardapio;
import br.com.restaurante.model.MovimentacaoDeEstoque;
import br.com.restaurante.model.enums.CategoriaItem;
import br.com.restaurante.model.enums.TipoMovimentacao;
import br.com.restaurante.model.enums.UnidadeMedida;
import br.com.restaurante.repository.AdministradorRepository;
import br.com.restaurante.repository.IngredienteRepository;
import br.com.restaurante.repository.ItemCardapioRepository;
import br.com.restaurante.repository.MovimentacaoRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@AllArgsConstructor
@Service
public class AdministradorService {

    private final AdministradorRepository administradorRepository;
    private final MovimentacaoRepository movimentacaoRepository;
    private final ItemCardapioRepository itemCardapioRepository;
    private final IngredienteRepository ingredienteRepository;

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
    public void cadastrarIngrediente(String nome, Double estoqueInicial, UnidadeMedida unidadeMedida){

        Ingrediente ingrediente = new Ingrediente();

        ingrediente.setNome(nome);
        ingrediente.setQuantidade(estoqueInicial);
        ingrediente.setUnidadeMedida(unidadeMedida);

        ingredienteRepository.save(ingrediente);
    }

    @Transactional
    public void registrarMovimentacao(Ingrediente ingrediente, Float quantidade, TipoMovimentacao tipo){

        MovimentacaoDeEstoque movimentacaoDeEstoque = new MovimentacaoDeEstoque();
        movimentacaoDeEstoque.setTipoMovimentacao(tipo);
        movimentacaoDeEstoque.setIngrediente(ingrediente);
        movimentacaoDeEstoque.setQuantidade(quantidade);
        movimentacaoDeEstoque.setDataCriacao(LocalDateTime.now());

        movimentacaoRepository.save(movimentacaoDeEstoque);
    }

    @Transactional
    public void cadastrarItemCardapio(String nome, Double preco, Map<Ingrediente, Double> receita, String descricao, CategoriaItem categoria, String urlFoto){

        ItemCardapio itemCardapio = new ItemCardapio();
        itemCardapio.setCategoria(categoria);
        itemCardapio.setNome(nome);
        itemCardapio.setDescricao(descricao);
        itemCardapio.setUrlFoto(urlFoto);
        itemCardapio.setPreco(preco);

        itemCardapioRepository.save(itemCardapio);
    }
}
