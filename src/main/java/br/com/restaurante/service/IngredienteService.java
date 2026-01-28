    package br.com.restaurante.service;

    import br.com.restaurante.dtos.DadosCadastroIngrediente;
    import br.com.restaurante.model.Ingrediente;
    import br.com.restaurante.repository.IngredienteRepository;
    import jakarta.transaction.Transactional;
    import lombok.AllArgsConstructor;
    import org.springframework.stereotype.Service;

    import java.util.List;

    @AllArgsConstructor
    @Service
    public class IngredienteService {
        private final IngredienteRepository ingredienteRepository;

        @Transactional
        public Ingrediente salvar(DadosCadastroIngrediente dto) {

            if (ingredienteRepository.existsByNome(dto.nome())) {
                throw new RuntimeException("Ingrediente já cadastrado: " + dto.nome());
            }

            Ingrediente ingrediente = new Ingrediente();
            ingrediente.setNome(dto.nome());
            ingrediente.setQuantidade(dto.quantidade());

            return ingredienteRepository.save(ingrediente);
        }

        public List<Ingrediente> listarTodos() {
            return ingredienteRepository.findAll();
        }

        public Ingrediente buscarPorId(Long id) {
            return ingredienteRepository.findById(id).orElseThrow(() -> new RuntimeException("Ingrediente não encontrado"));
        }

        @Transactional
        public void deletarPorId(Long id) {
            ingredienteRepository.deleteById(id);
        }

        @Transactional
        public Ingrediente atualizar(Long id, DadosCadastroIngrediente dto) {

            Ingrediente ingrediente = ingredienteRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Ingrediente não encontrado com o ID: " + id));

            ingrediente.setNome(dto.nome());
            ingrediente.setQuantidade(dto.quantidade());

            return ingredienteRepository.save(ingrediente);
        }

    }