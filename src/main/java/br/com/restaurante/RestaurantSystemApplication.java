package br.com.restaurante;

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

    // --- Services ---
    @Autowired private ClienteService clienteService;
    @Autowired private ItemCardapioService itemCardapioService;
    @Autowired private MesaService mesaService;
    @Autowired private AdministradorService administradorService;
    @Autowired private GarcomService garcomService;
    @Autowired private MovimentacaoService movimentacaoService;
    @Autowired private IngredienteService ingredienteService;

    // --- Repositories ---
    @Autowired private ClienteRepository clienteRepository;
    @Autowired private AdministradorRepository administradorRepository;
    @Autowired private GarcomRepository garcomRepository;
    @Autowired private PedidoPresencialRepository pedidoPresencialRepository;
    @Autowired private ReservaRepository reservaRepository;

    @Autowired private EntityManager entityManager;

    public static void main(String[] args) {
        SpringApplication.run(RestaurantSystemApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        int opcao = 0;

        while (opcao != 5) {
            exibirMenuPrincipal();
            opcao = lerInteiro(scanner);

            switch (opcao) {
                case 1 -> cadastrarCliente(scanner);
                case 2 -> menuCliente(scanner);
                case 3 -> menuAdministrador(scanner);
                case 4 -> menuGarcom(scanner);
                case 5 -> {
                    System.out.println("Saindo do sistema...");
                    System.exit(0);
                }
                default -> System.out.println("Opção inválida!");
            }
        }
    }

    private void exibirMenuPrincipal() {
        System.out.println("\n--- SISTEMA RESTAURANTE ---");
        System.out.println("1 - Cadastrar-se (Cliente)");
        System.out.println("2 - Área do Cliente");
        System.out.println("3 - Área do Administrador");
        System.out.println("4 - Área do Garçom");
        System.out.println("5 - Sair");
        System.out.print("Escolha: ");
    }

    // ============================================================================================
    // CASE 1: CADASTRO DE CLIENTE
    // ============================================================================================

    private void cadastrarCliente(Scanner scanner) {
        System.out.println("\n--- CADASTRO DE CLIENTE ---");
        Cliente cliente = new Cliente();
        Endereco endereco = new Endereco();

        System.out.print("Seu nome: ");
        cliente.setNome(scanner.nextLine());
        System.out.print("Telefone: ");
        cliente.setTelefone(scanner.nextLine());

        System.out.println("--- Endereço ---");
        System.out.print("CEP (apenas números): ");
        endereco.setCep(scanner.nextLine());
        System.out.print("Número: ");
        endereco.setNumero(scanner.nextLine());
        System.out.print("Bairro: ");
        endereco.setBairro(scanner.nextLine());
        System.out.print("Cidade: ");
        endereco.setCidade(scanner.nextLine());
        System.out.print("Rua: ");
        endereco.setRua(scanner.nextLine());

        System.out.print("Seu e-mail: ");
        cliente.setEmail(scanner.nextLine());
        System.out.print("Crie uma senha: ");
        cliente.setSenha(scanner.nextLine());

        cliente.setEndereco(endereco);

        try {
            clienteService.salvar(cliente);
            System.out.println("Cadastro realizado com sucesso!");

            posCadastro(scanner, cliente);
        } catch (Exception e) {
            System.out.println("Erro ao cadastrar: " + e.getMessage());
        }
    }

    private void posCadastro(Scanner scanner, Cliente cliente) {
        System.out.println("\n1 - Fazer Pedido Online Agora");
        System.out.println("2 - Fazer Reserva Agora");
        System.out.println("3 - Voltar ao Menu Principal");
        System.out.print("Escolha: ");
        int opcao = lerInteiro(scanner);

        if (opcao == 1) fluxoPedidoOnline(scanner, cliente);
        else if (opcao == 2) fluxoReserva(scanner, cliente);
    }


    // ============================================================================================
    // CASE 2: ÁREA DO CLIENTE
    // ============================================================================================

    private void menuCliente(Scanner scanner) {
        System.out.print("\nDigite seu e-mail: ");
        String email = scanner.next();

        if (!clienteRepository.existsByEmail(email)) {
            System.out.println("E-mail não encontrado.");
            return;
        }

        System.out.print("Digite sua senha: ");
        String senha = scanner.next();

        if (!clienteRepository.existsBySenha(senha)) {
            System.out.println("Senha incorreta.");
            return;
        }

        Cliente cliente = clienteRepository.findByEmail(email).orElseThrow();
        System.out.println("Bem-vindo(a), " + cliente.getNome());

        int opcao = 0;
        while (opcao != 4) { // Alterado para 4 opções
            System.out.println("\n--- MENU CLIENTE ---");
            System.out.println("1 - Fazer Pedido Online");
            System.out.println("2 - Fazer Reserva");
            System.out.println("3 - Cancelar Reserva"); // <--- NOVO
            System.out.println("4 - Logout");
            System.out.print("Escolha: ");
            opcao = lerInteiro(scanner);

            switch (opcao) {
                case 1 -> fluxoPedidoOnline(scanner, cliente);
                case 2 -> fluxoReserva(scanner, cliente);
                case 3 -> fluxoCancelarReservaCliente(scanner, cliente); // <--- NOVO
                case 4 -> System.out.println("Fazendo logout...");
                default -> System.out.println("Opção inválida.");
            }
        }
    }

    private void fluxoPedidoOnline(Scanner scanner, Cliente cliente) {
        List<ItemCardapio> cardapio = itemCardapioService.listarTodos();
        List<ItemCardapio> carrinho = new ArrayList<>();

        while (true) {
            System.out.println("\n--- CARDÁPIO ---");
            for (int i = 0; i < cardapio.size(); i++) {
                System.out.println((i + 1) + " - " + cardapio.get(i).getNome() + " (R$ " + cardapio.get(i).getPreco() + ")");
            }
            System.out.println("0 - Finalizar Pedido");
            System.out.print("Escolha o item: ");

            int escolha = lerInteiro(scanner);

            if (escolha == 0) break;
            if (escolha > 0 && escolha <= cardapio.size()) {
                carrinho.add(cardapio.get(escolha - 1));
                System.out.println(cardapio.get(escolha - 1).getNome() + " adicionado!");
            } else {
                System.out.println("Opção inválida.");
            }
        }

        if (!carrinho.isEmpty()) {
            clienteService.fazerPedidoOnline(cliente, carrinho, FormaPagamento.PIX);
            System.out.println("Pedido Online realizado com sucesso!");
        } else {
            System.out.println("Carrinho vazio.");
        }
    }

    private void fluxoReserva(Scanner scanner, Cliente cliente) {
        String sql = "SELECT m FROM Mesa m WHERE m.status = br.com.restaurante.model.enums.StatusMesa.LIVRE";
        TypedQuery<Mesa> query = entityManager.createQuery(sql, Mesa.class);
        List<Mesa> mesasDisponiveis = query.getResultList();

        if (mesasDisponiveis.isEmpty()) {
            System.out.println("Não há mesas livres no momento.");
            return;
        }

        System.out.println("Mesas Disponíveis: " + mesasDisponiveis.stream().map(Mesa::getNumero).toList());
        System.out.print("Digite o número da mesa: ");
        int numeroMesa = lerInteiro(scanner);

        Mesa mesaEscolhida = mesasDisponiveis.stream()
                .filter(m -> m.getNumero() == numeroMesa)
                .findFirst()
                .orElse(null);

        if (mesaEscolhida != null) {
            clienteService.fazerReserva(cliente, mesaEscolhida, LocalDateTime.of(2026, 10, 10, 20, 0));
            System.out.println("Reserva confirmada!");
        } else {
            System.out.println("Mesa inválida ou ocupada.");
        }
    }

    private void fluxoCancelarReservaCliente(Scanner scanner, Cliente cliente) {
        List<Reserva> minhasReservas = reservaRepository.findAll().stream()
                .filter(r -> r.getCliente().getId().equals(cliente.getId()) && r.getStatus() == StatusReserva.CONFIRMADA)
                .toList();

        if (minhasReservas.isEmpty()) {
            System.out.println("Você não possui reservas ativas.");
            return;
        }

        System.out.println("\n--- SUAS RESERVAS ATIVAS ---");
        for (Reserva r : minhasReservas) {
            System.out.println("ID: " + r.getId() + " | Mesa: " + r.getMesa().getNumero() + " | Data: " + r.getDataHora());
        }

        System.out.print("Digite o ID da reserva para cancelar: ");
        long idReserva = scanner.nextLong();
        scanner.nextLine();

        Reserva reservaSelecionada = minhasReservas.stream()
                .filter(r -> r.getId() == idReserva)
                .findFirst()
                .orElse(null);

        if (reservaSelecionada != null) {
            try {
                clienteService.cancelarReserva(reservaSelecionada);
                System.out.println("Reserva cancelada com sucesso!");
            } catch (Exception e) {
                System.out.println("Erro: " + e.getMessage());
            }
        } else {
            System.out.println("Reserva não encontrada ou ID inválido.");
        }
    }


    // ============================================================================================
    // CASE 3: ÁREA DO ADMINISTRADOR
    // ============================================================================================


    private void menuAdministrador(Scanner scanner) {
        System.out.print("\nE-mail Admin: ");
        String email = scanner.next();

        if (!administradorRepository.existsByEmail(email)) {
            System.out.println("Admin não encontrado.");
            return;
        }

        System.out.print("Senha: ");
        String senha = scanner.next();

        if (!administradorRepository.existsBySenha(senha)) {
            System.out.println("Senha incorreta.");
            return;
        }

        System.out.println("Login Admin realizado.");
        int opcao = 0;

        while (opcao != 5) {
            System.out.println("\n--- MENU ADMIN ---");
            System.out.println("1 - Cadastrar Ingredientes");
            System.out.println("2 - Controle de Estoque");
            System.out.println("3 - Cadastrar Item no Cardápio");
            System.out.println("4 - Cancelar Qualquer Reserva"); // <--- NOVO
            System.out.println("5 - Logout");
            System.out.print("Escolha: ");
            opcao = lerInteiro(scanner);

            switch (opcao) {
                case 1 -> {
                    System.out.print("Nome do Ingrediente: ");
                    String nome = scanner.next();
                    System.out.print("Qtd Inicial (g): ");
                    int qtd = lerInteiro(scanner);
                    administradorService.cadastrarIngrediente(nome, qtd);
                    System.out.println("Cadastrado!");
                }
                case 2 -> controleEstoque(scanner);
                case 3 -> cadastrarPrato(scanner);
                case 4 -> fluxoCancelarReservaAdmin(scanner); // <--- NOVO
                case 5 -> System.out.println("Saindo do Admin...");
                default -> System.out.println("Inválido.");
            }
        }
    }

    private void controleEstoque(Scanner scanner) {
        System.out.println("1 - Repor Estoque | 2 - Dar Baixa");
        int tipo = lerInteiro(scanner);

        List<Ingrediente> lista = ingredienteService.listarTodos();
        for (int i = 0; i < lista.size(); i++) {
            System.out.println((i+1) + " - " + lista.get(i).getNome() + " (" + lista.get(i).getQuantidade() + "g)");
        }

        System.out.print("Escolha o ingrediente: ");
        int idx = lerInteiro(scanner);
        System.out.print("Quantidade (g): ");
        int qtd = lerInteiro(scanner);

        if (idx > 0 && idx <= lista.size()) {
            Ingrediente ing = lista.get(idx - 1);
            if (tipo == 1) movimentacaoService.registrarEntrada(ing, qtd);
            else movimentacaoService.registrarSaida(ing, qtd);
            System.out.println("Estoque atualizado.");
        }
    }

    private void cadastrarPrato(Scanner scanner) {
        System.out.print("Nome do Prato: ");
        String nome = scanner.nextLine();

        System.out.print("Preço: ");
        double preco = scanner.nextDouble();
        scanner.nextLine();

        Map<String, Integer> receita = new HashMap<>();
        System.out.print("Quantos ingredientes leva? ");
        int qtdIng = lerInteiro(scanner);

        for (int i = 0; i < qtdIng; i++) {
            System.out.print("Nome do Ingrediente " + (i+1) + ": ");
            String nomeIng = scanner.nextLine();
            System.out.print("Quantidade (g): ");
            int qtd = lerInteiro(scanner);
            receita.put(nomeIng, qtd);
        }

        System.out.print("Descrição: ");
        String desc = scanner.nextLine();
        System.out.print("URL da Foto: ");
        String url = scanner.nextLine();

        System.out.println("Categoria (1-Comida, 2-Bebida, 3-Sobremesa): ");
        int catOpt = lerInteiro(scanner);
        CategoriaItem categoria = switch (catOpt) {
            case 2 -> CategoriaItem.BEBIDA;
            case 3 -> CategoriaItem.SOBREMESA;
            default -> CategoriaItem.COMIDA;
        };

        administradorService.cadastrarItemCardapio(nome, preco, receita, desc, categoria, url);
        System.out.println("Item cadastrado!");
    }

    private void fluxoCancelarReservaAdmin(Scanner scanner) {
        List<Reserva> todasReservas = reservaRepository.findAll().stream()
                .filter(r -> r.getStatus() == StatusReserva.CONFIRMADA)
                .toList();

        if (todasReservas.isEmpty()) {
            System.out.println("Não há reservas ativas no sistema.");
            return;
        }

        System.out.println("\n--- TODAS AS RESERVAS ATIVAS ---");
        for (Reserva r : todasReservas) {
            System.out.println("ID: " + r.getId() + " | Cliente: " + r.getCliente().getNome() + " | Mesa: " + r.getMesa().getNumero());
        }

        System.out.print("Digite o ID da reserva para cancelar: ");
        long idReserva = scanner.nextLong();
        scanner.nextLine();

        try {
            // Chama o método que criamos no AdministradorService
            administradorService.cancelarReserva(idReserva);
            System.out.println("Reserva cancelada pelo administrador!");
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }


    // ============================================================================================
    // CASE 4: ÁREA DO GARÇOM
    // ============================================================================================

    private void menuGarcom(Scanner scanner) {
        System.out.print("\nE-mail Garçom: ");
        String email = scanner.next();

        if (!garcomRepository.existsByEmail(email)) {
            System.out.println("Garçom não encontrado.");
            return;
        }

        System.out.print("Senha: ");
        String senha = scanner.next();

        if (!garcomRepository.existsBySenha(senha)) {
            System.out.println("Senha incorreta.");
            return;
        }

        Garcom garcom = garcomRepository.findByEmail(email).orElseThrow();
        System.out.println("Bem-vindo, " + garcom.getNome());

        int opcao = 0;
        while (opcao != 3) {
            System.out.println("\n--- MENU GARÇOM ---");
            System.out.println("1 - Novo Pedido Presencial");
            System.out.println("2 - Finalizar Pedido (Mudar Status)");
            System.out.println("3 - Logout");
            System.out.print("Escolha: ");
            opcao = lerInteiro(scanner);

            switch (opcao) {
                case 1 -> {
                    List<Mesa> mesasLivres = mesaService.listarTodos().stream()
                            .filter(m -> m.getStatus() == StatusMesa.LIVRE)
                            .toList();

                    if (mesasLivres.isEmpty()) {
                        System.out.println("Não há mesas livres no momento.");
                        break;
                    }

                    System.out.println("Mesas Livres: " + mesasLivres.stream().map(Mesa::getNumero).toList());
                    System.out.print("Digite o número da mesa: ");
                    int numMesa = lerInteiro(scanner);

                    Mesa mesa = mesasLivres.stream()
                            .filter(m -> m.getNumero() == numMesa)
                            .findFirst()
                            .orElse(null);

                    if (mesa == null) {
                        System.out.println("Mesa inválida ou ocupada.");
                        break;
                    }

                    List<ItemCardapio> itensPedido = new ArrayList<>();
                    List<ItemCardapio> cardapio = itemCardapioService.listarTodos();

                    while (true) {
                        System.out.println("\n--- Adicionar Item ---");
                        for (int i = 0; i < cardapio.size(); i++) {
                            System.out.println((i + 1) + " - " + cardapio.get(i).getNome() + " (R$ " + cardapio.get(i).getPreco() + ")");
                        }
                        System.out.println("0 - Concluir Seleção");
                        System.out.print("Escolha: ");
                        int esc = lerInteiro(scanner);

                        if (esc == 0) break;
                        if (esc > 0 && esc <= cardapio.size()) {
                            itensPedido.add(cardapio.get(esc - 1));
                            System.out.println("Item adicionado!");
                        }
                    }

                    if (itensPedido.isEmpty()) {
                        System.out.println("Cancelado: Nenhum item selecionado.");
                        break;
                    }

                    System.out.println("\nForma de Pagamento Prevista:");
                    System.out.println("1-Pix | 2-Crédito | 3-Débito | 4-Dinheiro");
                    int pag = lerInteiro(scanner);
                    FormaPagamento fp = switch (pag) {
                        case 1 -> FormaPagamento.PIX;
                        case 2 -> FormaPagamento.CARTAO_CREDITO;
                        case 3 -> FormaPagamento.CARTAO_DEBITO;
                        default -> FormaPagamento.DINHEIRO;
                    };

                    try {
                        garcomService.registrarPedidoPresencial(garcom.getId(), mesa.getId(), itensPedido, fp);
                        System.out.println("Pedido registrado com sucesso! Mesa " + numMesa + " agora está OCUPADA.");
                    } catch (Exception e) {
                        System.out.println("Erro ao registrar: " + e.getMessage());
                    }
                }
                case 2 -> {
                    List<PedidoPresencial> pedidosAbertos = pedidoPresencialRepository.findAll().stream()
                            .filter(p -> p.getStatus() != StatusPedido.PEDIDO_PRONTO)
                            .toList();

                    if (pedidosAbertos.isEmpty()) {
                        System.out.println("Não há pedidos em preparo no momento.");
                        break;
                    }

                    System.out.println("\n--- PEDIDOS EM ABERTO ---");
                    for (PedidoPresencial p : pedidosAbertos) {
                        System.out.println("ID: " + p.getId() + " | Mesa: " + p.getMesa().getNumero() + " | Status: " + p.getStatus());
                    }

                    System.out.print("Digite o ID do Pedido para finalizar: ");
                    long idPedido = scanner.nextLong();
                    scanner.nextLine();

                    try {
                        garcomService.fecharPedido(idPedido);
                        System.out.println("Pedido " + idPedido + " atualizado para PRONTO.");
                    } catch (Exception e) {
                        System.out.println("Erro: " + e.getMessage());
                    }
                }
                case 3 -> System.out.println("Saindo do Garçom...");
                default -> System.out.println("Opção inválida.");
            }
        }
    }

    private int lerInteiro(Scanner scanner) {
        try {
            int i = scanner.nextInt();
            scanner.nextLine();
            return i;
        } catch (InputMismatchException e) {
            scanner.nextLine();
            return -1;
        }
    }
}