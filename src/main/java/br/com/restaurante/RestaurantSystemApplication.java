package br.com.restaurante;

import br.com.restaurante.model.*;
import br.com.restaurante.model.enums.CategoriaItem;
import br.com.restaurante.model.enums.FormaPagamento;
import br.com.restaurante.model.enums.StatusMesa;
import br.com.restaurante.repository.AdministradorRepository;
import br.com.restaurante.repository.ClienteRepository;
import br.com.restaurante.repository.GarcomRepository;
import br.com.restaurante.repository.IngredienteRepository;
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
    private GarcomRepository garcomRepository;

    @Autowired
    EntityManager entityManager;
    @Autowired
    private GarcomService garcomService;
    @Autowired
    private MovimentacaoService movimentacaoService;
    @Autowired
    private IngredienteService ingredienteService;
    @Autowired
    private IngredienteRepository ingredienteRepository;

    public static void main(String[] args) {
        SpringApplication.run(RestaurantSystemApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

        Scanner scanner = new Scanner(System.in);
        int opcao = 0;

        while (opcao != 5) {
            System.out.println("\n--- SISTEMA RESTAURANTE ---");
            System.out.println("\n1 - Cadastrar-se");
            System.out.println("2 - Fazer Login Cliente");
            System.out.println("3 - Fazer Login Administrador");
            System.out.println("4 - Fazer Login Garçom");
            System.out.println("5 - Sair");
            System.out.print("\nEscolha: ");

            opcao = scanner.nextInt();
            scanner.nextLine();

            switch (opcao) {
                case 1:

                    Cliente cliente = new Cliente();
                    Endereco endereco = new Endereco();

                    System.out.println("Seu nome: ");
                    String nome = scanner.nextLine();
                    System.out.println("Telefone: ");
                    String telefone = scanner.nextLine();

                    System.out.println("Digite seu endereço:\nCEP:");
                    String cep = scanner.nextLine();
                    System.out.println("Numero da casa: ");
                    String numero = scanner.nextLine();
                    System.out.println("Bairro: ");
                    String bairro = scanner.nextLine();
                    System.out.println("Cidade: ");
                    String cidade = scanner.nextLine();
                    System.out.println("Nome da rua: ");
                    String rua = scanner.nextLine();

                    System.out.println("Digite seu email: ");
                    String email = scanner.nextLine();
                    System.out.println("Crie uma senha: ");
                    String senha = scanner.nextLine();

                    endereco.setNumero(numero);
                    endereco.setCep(cep);
                    endereco.setBairro(bairro);
                    endereco.setRua(rua);
                    endereco.setCidade(cidade);

                    cliente.setEndereco(endereco);
                    cliente.setNome(nome);
                    cliente.setTelefone(telefone);
                    cliente.setEmail(email);
                    cliente.setSenha(senha);

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
                            break;
                        case 2:

                            Mesa mesa = new Mesa(1, 10, StatusMesa.LIVRE);

                            mesaService.salvar(mesa);

                            clienteService.fazerReserva(cliente, mesa, LocalDateTime.of(2026, 10, 10, 10, 10, 1));

                            System.out.println("Reserva feita com sucesso na mesa: " + mesa.getNumero());
                            break;
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
                                        System.out.println("0- Finalizar pedido");

                                        List<ItemCardapio> itensSelecionados = new ArrayList<>();
                                        while (true){
                                            System.out.print("Digite sua opção: ");

                                            int opComida = scanner.nextInt();
                                            if(opComida == 0){
                                                break;
                                            }
                                            else{
                                            itensSelecionados.add(itens.get(opComida -1));}
                                            System.out.println("Item " + itens.get(opComida -1).getNome() + " adicionado ao pedido.");
                                        }
//
                                        clienteService.fazerPedidoOnline(clienteEmail, itensSelecionados, FormaPagamento.PIX);

                                        System.out.println("Pedido realizado com sucesso!");
                                        break;
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
                                        break;

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
                        int optEmail = 1;
                        while (optEmail == 1) {
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
                                                break;

                                            case 2:
                                                System.out.println("O que deseja?\n1- Repor estoque\n2- Dar baixa no estoque");
                                                int optEstoque = scanner.nextInt();

                                                if (optEstoque == 1){
                                                    System.out.println("O que você deseja repor?");
                                                    int cont = 0;
                                                    List<Ingrediente> lista = ingredienteService.listarTodos();

                                                    System.out.println("Ingrediente | Quantidade (gramas)");
                                                    for (Ingrediente i : lista){
                                                        cont++;
                                                        System.out.println(cont + "- " + i.getNome() + " - " + i.getQuantidade());
                                                    }
                                                    int optIngrediente = scanner.nextInt();

                                                    System.out.println("Quantidade (em gramas): ");
                                                    int optQuantidade = scanner.nextInt();
                                                    movimentacaoService.registrarEntrada(lista.get(optIngrediente -1), optQuantidade);

                                                }
                                                else if (optEstoque == 2){
                                                    System.out.println("Em que você deseja dar baixa?");
                                                    int cont = 0;
                                                    List<Ingrediente> lista = ingredienteService.listarTodos();

                                                    System.out.println("Ingrediente | Quantidade disponível (gramas)");
                                                    for (Ingrediente i : lista){
                                                        cont++;
                                                        System.out.println(cont + "- " + i.getNome() + " - " + i.getQuantidade());
                                                    }

                                                    int optIngrediente = scanner.nextInt();

                                                    System.out.println("Quantidade disponível (em gramas): ");
                                                    int optQuantidade = scanner.nextInt();
                                                    movimentacaoService.registrarSaida(lista.get(optIngrediente -1), optQuantidade);

                                                }break;

                                            case 3:
                                                System.out.println("Nome do prato: ");
                                                String prato = scanner.nextLine();

                                                System.out.println("Preço: ");
                                                Double preco = scanner.nextDouble();
                                                scanner.nextLine();

                                                System.out.println("Quantos ingredientes esse prato leva?");
                                                int qtdIngredientes = scanner.nextInt();
                                                scanner.nextLine();

                                                Map<String, Integer> ingredientes = new HashMap<>();
                                                for (int i = 0; i < qtdIngredientes; i++ ){
                                                    int contador = 1;
                                                    List<Ingrediente> ingredientesDisponiveis = ingredienteService.listarTodos();
                                                    System.out.println("Ingredientes disponiveis: " + ingredientesDisponiveis.stream().map(Ingrediente::getNome).toList());
                                                    System.out.println("Digite o nome do ingrediente " + contador + ":");
                                                    String ingredienteNome = scanner.nextLine();
                                                    System.out.println("Agora a quantidade necessária para fazer o prato (em gramas): ");
                                                    int quantidade = scanner.nextInt();
                                                    scanner.nextLine();
                                                    ingredientes.put(ingredienteNome,quantidade);
                                                    contador ++;
                                                }

                                                System.out.println("Adicione uma descrição a seu prato/bebida: ");
                                                String descricao = scanner.nextLine();

                                                CategoriaItem categoria;
                                                System.out.println("Qual o tipo do produto?\n1- Comida\n2- Bebida\n3- Sobremesa");
                                                int optTipo = scanner.nextInt();
                                                scanner.nextLine();
                                                switch (optTipo){
                                                    case 1:
                                                        categoria = CategoriaItem.COMIDA;
                                                        break;
                                                    case 2:
                                                        categoria = CategoriaItem.BEBIDA;
                                                        break;
                                                    case 3:
                                                        categoria = CategoriaItem.SOBREMESA;
                                                        break;
                                                    default:
                                                        categoria = CategoriaItem.COMIDA;
                                                        System.out.println("Opção inválida, definindo como COMIDA por padrão.");
                                                        break;

                                                }

                                                System.out.println("Digite a url da foto do seu produto: ");
                                                String url = scanner.nextLine();



                                                administradorService.cadastrarItemCardapio(prato, preco, ingredientes, descricao, categoria, url);
                                        }
                                        break;
                                    } else {
                                        System.out.println("Senha errada!");
                                        System.out.println("\n1 - Digitar Novamente");
                                        System.out.println("\n2 - Sair");
                                    }

                                    senhaAdmin = scanner.nextInt();
                                }
                                break;
                            }else{
                                System.out.println("ERROR: O email digitado não existe, deseja tentar novamente?\n1- Sim\n2- Não");
                                int opt = scanner.nextInt();
                                if (opt == 1){
                                    continue;
                                }
                                else{
                                    optEmail = 0;
                                }
                            }
                        }
                        break;
//                case 4:
//                    int opcEmail = 1;
//                    while (opcEmail == 1) {
//                        System.out.println("Digite seu e-mail: ");
//                        String emailGarcom = scanner.next();
//
//                        if (garcomRepository.existsByEmail(emailGarcom)) {
//                            int senhaAdmin = 1;
//
//                            while (senhaAdmin == 1) {
//                                System.out.print("\nDigite sua senha: ");
//
//                                String opSenha = scanner.next();
//
//                                if (garcomRepository.existsBySenha(opSenha)) {
//                                    System.out.println("\n1 - Registrar Pedido Presencial");
//                                    System.out.println("2 - Finalizar Pedido");
//                                    System.out.println("3 - Sair");
//
//                                    System.out.print("\nEscolha: ");
//
//                                    int opcaoAdmin = scanner.nextInt();
//                                    scanner.nextLine();

//                                    switch (opcaoAdmin){
//                                        case 1:
////                                            garcomService.registrarPedidoPresencial();
//
//
//                                    }
//                                }}} }
                case 5:
                    System.out.println("Saindo...");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Opção inválida!");
            }
        }
    }
}