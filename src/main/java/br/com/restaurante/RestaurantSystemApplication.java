package br.com.restaurante;

import br.com.restaurante.model.Endereco;
import br.com.restaurante.repository.EnderecoRepository;
import br.com.restaurante.repository.MesaRepository;
import br.com.restaurante.services.EnderecoService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class RestaurantSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(RestaurantSystemApplication.class, args);
    }
    }

