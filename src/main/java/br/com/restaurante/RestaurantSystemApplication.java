package br.com.restaurante;

import br.com.restaurante.model.Mesa;
import br.com.restaurante.model.enums.StatusMesa;
import br.com.restaurante.repository.MesaRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RestaurantSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(RestaurantSystemApplication.class, args);

        MesaRepository mesaRepository = null;

        Mesa mesa = new Mesa();

        mesa.setNumero(10);
        mesa.setCapacidade(10000);
        mesa.setStatus(StatusMesa.LIVRE);

        mesaRepository.save(mesa);
    }

}
