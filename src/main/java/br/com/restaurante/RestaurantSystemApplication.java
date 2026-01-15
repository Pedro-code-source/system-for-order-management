package br.com.restaurante;

import br.com.restaurante.repository.MesaRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class RestaurantSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(RestaurantSystemApplication.class, args);
    }

    @Bean
    public CommandLineRunner demo(MesaRepository mesaRepository) {
        return (args) -> {
            // AQUI DENTRO o Spring já injetou o repository pra você.
            // Pode testar ou deixar vazio apenas para ver os logs do banco.
            System.out.println("O sistema subiu! Verifique os logs acima para ver as tabelas.");
        };
    }
}
