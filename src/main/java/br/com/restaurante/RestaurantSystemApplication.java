package br.com.restaurante;

import br.com.restaurante.dtos.DadosCadastroCliente;
import br.com.restaurante.dtos.DadosCadastroEndereco;
import br.com.restaurante.dtos.DadosCadastroPedidoOnline;
import br.com.restaurante.dtos.DadosCadastroReserva;
import br.com.restaurante.model.*;
import br.com.restaurante.model.enums.CategoriaItem;
import br.com.restaurante.model.enums.FormaPagamento;
import br.com.restaurante.model.enums.StatusMesa;
import br.com.restaurante.model.enums.StatusPedido;
import br.com.restaurante.model.enums.StatusReserva;
import br.com.restaurante.repository.*;
import br.com.restaurante.service.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalDateTime;
import java.util.*;

@SpringBootApplication
public class RestaurantSystemApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(RestaurantSystemApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

    }
}