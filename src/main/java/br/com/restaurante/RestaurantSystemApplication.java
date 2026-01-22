package br.com.restaurante;

import br.com.restaurante.model.*;
import br.com.restaurante.model.enums.CategoriaItem;
import br.com.restaurante.model.enums.FormaPagamento;
import br.com.restaurante.model.enums.StatusMesa;
import br.com.restaurante.repository.AdministradorRepository;
import br.com.restaurante.repository.ClienteRepository;
import br.com.restaurante.service.AdministradorService;
import br.com.restaurante.service.ClienteService;
import br.com.restaurante.service.ItemCardapioService;
import br.com.restaurante.service.MesaService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@SpringBootApplication
public class RestaurantSystemApplication implements CommandLineRunner {

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private ItemCardapioService itemCardapioService;

    @Autowired
    private MesaService mesaService;

    @Autowired
    private AdministradorService administradorService;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private AdministradorRepository administradorRepository;

    @Autowired
    EntityManager entityManager;

    public static void main(String[] args) {
        SpringApplication.run(RestaurantSystemApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

        Scanner scanner = new Scanner(System.in);
        int opcao = 0;

        while (opcao != 9) {
            System.out.println("\n--- SISTEMA RESTAURANTE ---");
            System.out.println("\n1 - Cadastrar-se");
            System.out.println("2 - Fazer Login Cliente");
            System.out.println("3 - Fazer Login Administrador");
            System.out.println("4 - Fazer Login Garçom");
            System.out.println("9 - Sair");
            System.out.print("\nEscolha: ");

            opcao = scanner.nextInt();
            scanner.nextLine();

            switch (opcao) {
                case 1:
                    Administrador administrador = new Administrador("admin@hotmail.com", "admin123");

                    administradorService.salvar(administrador);

                    Cliente cliente = new Cliente();

                    Endereco endereco = new Endereco("Manoel Guedes da Costa", "120", "Centro", "Esperança", "58135-000");

                    cliente.setNome("João");
                    cliente.setEmail("joao@hotmail.com");
                    cliente.setSenha("joao123");
                    cliente.setEndereco(endereco);
                    cliente.setTelefone("(83) 9 8877-0691");

                    clienteService.salvar(cliente);

                    System.out.println("\n1 - Fazer Pedido Online");
                    System.out.println("2 - Fazer Reserva");
                    System.out.println("3 - Sair");
                    System.out.print("\nEscolha: ");

                    int opcao1 = scanner.nextInt();
                    scanner.nextLine();

                    switch (opcao1) {
                        case 1:

                            ItemCardapio itemCardapio = new ItemCardapio();
                            itemCardapio.setNome("Pizza");
                            itemCardapio.setCategoria(CategoriaItem.COMIDA);
                            itemCardapio.setDescricao("Pizza de Frango");
                            itemCardapio.setPreco(20.00);

                            itemCardapioService.salvar(itemCardapio);

                            List<ItemCardapio> itens = new ArrayList<>();
                            itens.add(itemCardapio);

                            clienteService.fazerPedidoOnline(cliente, itens, FormaPagamento.PIX);

                            System.out.println("Pedido feito com sucesso. O item é: " + itemCardapio.getNome());
                        case 2:

                            Mesa mesa = new Mesa(1, 10, StatusMesa.LIVRE);

                            mesaService.salvar(mesa);

                            clienteService.fazerReserva(cliente, mesa, LocalDateTime.of(2026, 10, 10, 10, 10, 1));

                            System.out.println("Reserva feita com sucesso na mesa: " + mesa.getNumero());
                        case 3:
                            System.exit(0);
                    }
                case 2:
                    System.out.print("\nDigite seu e-mail: ");

                    String opEmail = scanner.next();

                    if (clienteRepository.existsByEmail(opEmail)) {
                        int opcaoSenhaNovamente = 1;

                        while (opcaoSenhaNovamente == 1) {
                            System.out.print("\nDigite sua senha: ");

                            String opSenha = scanner.next();

                            if (clienteRepository.existsBySenha(opSenha)) {
                                System.out.println("\n1 - Fazer Pedido Online");
                                System.out.println("2 - Fazer Reserva");
                                System.out.println("3 - Sair");
                                System.out.print("\nEscolha: ");

                                int opcaoCliente = scanner.nextInt();
                                scanner.nextLine();

                                Cliente clienteEmail = clienteRepository.findByEmail(opEmail).orElseThrow();

                                switch (opcaoCliente) {
                                    case 1:
                                        List<ItemCardapio> itens = itemCardapioService.listarTodos();

                                        int cont = 0;

                                        for (ItemCardapio item : itens) {
                                            cont++;
                                            System.out.println(cont + " - " + item.getNome());
                                        }

                                        System.out.print("Digite sua opção: ");

                                        int opComida = scanner.nextInt();

                                        List<ItemCardapio> itensSelecionados = new ArrayList<>();

                                        switch (opComida) {
                                            case 1:
                                                itensSelecionados.add(itens.get(0));
                                                System.out.println(itens.get(0).getNome() + " foi selecionado!");
                                                break;
                                            case 2:
                                                itensSelecionados.add(itens.get(1));
                                                System.out.println(itens.get(1).getNome() + " foi selecionado!");
                                                break;
                                            case 3:
                                                itensSelecionados.add(itens.get(2));
                                                System.out.println(itens.get(2).getNome() + " foi selecionado!");
                                                break;
                                        }

                                        clienteService.fazerPedidoOnline(clienteEmail, itensSelecionados, FormaPagamento.PIX);

                                        System.out.println("Pedido realizado com sucesso!");
                                    case 2:
                                        String sql = "SELECT m FROM Mesa m WHERE m.status = br.com.restaurante.model.enums.StatusMesa.LIVRE";

                                        TypedQuery<Mesa> query = entityManager.createQuery(sql, Mesa.class);

                                        List<Mesa> mesasDisponiveis = query.getResultList();

                                        System.out.println("Mesas Disponíveis: " + mesasDisponiveis.stream().map(Mesa::getNumero).toList());
                                        System.out.print("Digite o número da mesa que você quer reserva: ");
                                        int mesaEscolhida = scanner.nextInt();

                                        Mesa mesaExistente = null;
                                        for (Mesa mesa : mesasDisponiveis) {
                                            if (mesa.getNumero() == mesaEscolhida) {
                                                mesaExistente = mesa;
                                            }
                                        }

                                        if (mesaExistente == null) {
                                            System.out.println("Mesa indisponivel.");
                                        } else {
                                            clienteService.fazerReserva(clienteEmail, mesaExistente, LocalDateTime.of(2026, 10, 10, 10, 10, 10));
                                            System.out.println("Reserva feita com sucesso em nome de: " + clienteEmail.getNome());
                                        }

                                }
                                break;
                            } else {
                                System.out.println("Senha errada!");
                                System.out.println("\n1 - Digitar Novamente");
                                System.out.println("\n2 - Sair");

                                opcaoSenhaNovamente = scanner.nextInt();
                            }
                        }

                    } else {
                        System.out.print("\nE-mail não encontrado em nosso sistema: " + opEmail);
                        System.out.print("\n1 - Cadastrar-se");
                        System.out.print("2 - Digitar novamente");
                    }
                    break;

                case 3:

                    System.out.println("Digite seu e-mail: ");
                    String emailAdmin = scanner.next();

                    if (administradorRepository.existsByEmail(emailAdmin)) {
                        int senhaAdmin = 1;

                        while (senhaAdmin == 1) {
                            System.out.print("\nDigite sua senha: ");

                            String opSenha = scanner.next();

                            if (administradorRepository.existsBySenha(opSenha)) {
                                System.out.println("\n1 - Cadastrar Ingredientes");
                                System.out.println("2 - Registrar Movimentação");
                                System.out.println("3 - Cadastrar Itens no Cardápio");
                                System.out.println("4 - Sair");
                                System.out.print("\nEscolha: ");

                                int opcaoAdmin = scanner.nextInt();
                                scanner.nextLine();

                                Administrador administradorEmail = administradorRepository.findByEmail(emailAdmin).orElseThrow();

                                switch (opcaoAdmin) {
                                    case 1:
                                        System.out.println("Nome do Ingrediente: ");
                                        String nomeIngrediente = scanner.next();

                                        System.out.println("Quantidade Inicial (em gramas): ");
                                        int qtdInicial = scanner.nextInt();

                                        administradorService.cadastrarIngrediente(nomeIngrediente, qtdInicial);

                                        System.out.println("Ingrediente: " + nomeIngrediente + " cadastrado com sucesso!");
                                }
                                break;
                            } else {
                                System.out.println("Senha errada!");
                                System.out.println("\n1 - Digitar Novamente");
                                System.out.println("\n2 - Sair");
                            }

                            senhaAdmin = scanner.nextInt();
                        }
                    }


                case 9:
                    System.out.println("Saindo...");
                    break;
                default:
                    System.out.println("Opção inválida!");
                    System.exit(0);
            }
        }
    }
}