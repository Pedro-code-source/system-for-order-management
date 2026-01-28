package br.com.restaurante.service;

import br.com.restaurante.dtos.DadosCadastroIngrediente;
import br.com.restaurante.model.*;
import br.com.restaurante.model.enums.*;
import br.com.restaurante.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class AdministradorService {

    private final AdministradorRepository administradorRepository;
    private final MovimentacaoRepository movimentacaoRepository;
    private final IngredienteRepository ingredienteRepository;
    private final ItemCardapioService itemCardapioService;
    private final IngredienteService ingredienteService;

    @Transactional
    public Administrador salvar(Administrador adm){
        if (administradorRepository.existsByEmail(adm.getEmail())) {
            throw new RuntimeException("Já existe um administrador com este e-mail.");
        }
        return administradorRepository.save(adm);
    }

    @Transactional
    public void deletarPorId(Long id){
        if (administradorRepository.existsById(id)){
            administradorRepository.deleteById(id);
        } else {
            throw new RuntimeException("O administrador com ID " + id + " não existe");
        }
    }

    public List<Administrador> listarTodos(){
        return administradorRepository.findAll();
    }

    public Administrador buscarPorId(Long id){
        return administradorRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("O administrador não existe."));
    }

    @Transactional
    public Administrador atualizar(Long id, Administrador novoAdm){
        Administrador existente = buscarPorId(id);
        existente.setNome(novoAdm.getNome());
        existente.setEmail(novoAdm.getEmail());
        existente.setSenha(novoAdm.getSenha());
        return administradorRepository.save(existente);
    }

    @Transactional
    public Ingrediente cadastrarIngrediente(DadosCadastroIngrediente dto){
        return ingredienteService.salvar(dto);
    }

    @Transactional
    public void registrarMovimentacao(Long idIngrediente, int quantidade, TipoMovimentacao tipo){
        Ingrediente ingrediente = ingredienteRepository.findById(idIngrediente)
                .orElseThrow(() -> new RuntimeException("Ingrediente não encontrado"));

        MovimentacaoDeEstoque movimentacaoDeEstoque = new MovimentacaoDeEstoque();
        movimentacaoDeEstoque.setTipoMovimentacao(tipo);
        movimentacaoDeEstoque.setIngrediente(ingrediente);
        movimentacaoDeEstoque.setQuantidade(quantidade);
        movimentacaoDeEstoque.setDataCriacao(LocalDateTime.now());

        movimentacaoRepository.save(movimentacaoDeEstoque);
    }

    @Transactional
    public void cadastrarItemCardapio(String nome, Double preco, String descricao, CategoriaItem categoria, String urlFoto){
        ItemCardapio itemCardapio = new ItemCardapio();
        itemCardapio.setCategoria(categoria);
        itemCardapio.setNome(nome);
        itemCardapio.setDescricao(descricao);
        itemCardapio.setUrlFoto(urlFoto);
        itemCardapio.setPreco(preco);

        itemCardapioService.salvar(itemCardapio);
    }
}