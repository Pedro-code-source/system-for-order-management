package br.com.restaurante;

import br.com.restaurante.model.Cliente;
import br.com.restaurante.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Scanner;

@SpringBootApplication
public class RestaurantSystemApplication implements CommandLineRunner {

    @Autowired
    private ClienteService clienteService;

    public static void main(String[] args) {
        SpringApplication.run(RestaurantSystemApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

        Scanner scanner = new Scanner(System.in);
        int opcao = 0;

        while (opcao != 9) {
            System.out.println("\n--- SISTEMA RESTAURANTE ---");
            System.out.println("\n1 - Cadastrar Cliente");
            System.out.println("2 - Listar Clientes");
            System.out.println("3 - Buscar por ID");
            System.out.println("4 - Atualizar");
            System.out.println("5 - Deletar");
            System.out.println("9 - Sair");
            System.out.print("\nEscolha: ");

            opcao = scanner.nextInt();
            scanner.nextLine();

            switch (opcao) {
                case 1:
                    Cliente cliente = new Cliente();
                    cliente.setNome("João");
                    cliente.setEmail("joao@hotmail.com");
                    cliente.setSenha("joao123");
                    cliente.setNome("João");
                    cliente.setNome("João");
                    // Lógica de pedir dados (System.out.print) e chamar clienteService.salvar()
                    break;
                case 2:
                    // Lógica de chamar clienteService.listarTodos() e imprimir com forEach
                    break;
                // ... outros cases
                case 9:
                    System.out.println("Saindo...");
                    break;
                default:
                    System.out.println("Opção inválida!");
            }
        }
    }
}