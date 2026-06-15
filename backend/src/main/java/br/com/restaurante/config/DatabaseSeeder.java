package br.com.restaurante.config;

import br.com.restaurante.model.*;
import br.com.restaurante.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DatabaseSeeder {

    @Bean
    public CommandLineRunner seedDatabase(
            AdministradorRepository adminRepo,
            GarcomRepository garcomRepo,
            ClienteRepository clienteRepo
    ) {
        return args -> {
            if (!adminRepo.existsByEmail("admin@restaurant.com")) {
                Administrador admin = new Administrador();
                admin.setEmail("admin@restaurant.com");
                admin.setSenha("admin123");
                admin.setNome("Admin");
                adminRepo.save(admin);
            }

            if (!garcomRepo.existsByEmail("garcom@restaurant.com")) {
                Garcom garcom = new Garcom();
                garcom.setEmail("garcom@restaurant.com");
                garcom.setSenha("garcom123");
                garcom.setNome("Garçom");
                garcomRepo.save(garcom);
            }

            if (!clienteRepo.existsByEmail("cliente@email.com")) {
                Endereco endereco = new Endereco();
                endereco.setRua("Rua das Flores");
                endereco.setNumero("123");
                endereco.setBairro("Centro");
                endereco.setCidade("São Paulo");
                endereco.setCep("01001-000");

                Cliente cliente = new Cliente();
                cliente.setEmail("cliente@email.com");
                cliente.setSenha("cliente123");
                cliente.setNome("Cliente");
                cliente.setTelefone("11999999999");
                cliente.setEndereco(endereco);
                clienteRepo.save(cliente);
            }
        };
    }
}
