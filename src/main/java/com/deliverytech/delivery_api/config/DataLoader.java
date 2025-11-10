package com.deliverytech.delivery_api.config;

import com.deliverytech.delivery_api.entity.*;
import com.deliverytech.delivery_api.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Component
public class DataLoader implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DataLoader.class);

    private final ClienteRepository clienteRepository;
    private final RestauranteRepository restauranteRepository;
    private final ProdutoRepository produtoRepository;
    private final PedidoRepository pedidoRepository;
    private final ItemPedidoRepository itemPedidoRepository;

    public DataLoader(
            ClienteRepository clienteRepository,
            RestauranteRepository restauranteRepository,
            ProdutoRepository produtoRepository,
            PedidoRepository pedidoRepository,
            ItemPedidoRepository itemPedidoRepository) {
        this.clienteRepository = clienteRepository;
        this.restauranteRepository = restauranteRepository;
        this.produtoRepository = produtoRepository;
        this.pedidoRepository = pedidoRepository;
        this.itemPedidoRepository = itemPedidoRepository;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        logger.info("=========================================");
        logger.info("INICIANDO CARREGAMENTO DE DADOS DE TESTE");
        logger.info("=========================================");

        itemPedidoRepository.deleteAll();
        pedidoRepository.deleteAll();
        produtoRepository.deleteAll();
        restauranteRepository.deleteAll();
        clienteRepository.deleteAll();
        
        logger.info("\nCriando clientes...");
        Cliente cliente1 = criarCliente("João Silva", "joao@email.com", true);
        Cliente cliente2 = criarCliente("Maria Santos", "maria@email.com", true);
        Cliente cliente3 = criarCliente("Pedro Oliveira", "pedro@email.com", false);
        
        logger.info("{} clientes criados", clienteRepository.count());
        logger.info("   - Cliente 1: {} ({})", cliente1.getNome(), cliente1.getEmail());
        logger.info("   - Cliente 2: {} ({})", cliente2.getNome(), cliente2.getEmail());
        logger.info("   - Cliente 3: {} ({}) - Inativo", cliente3.getNome(), cliente3.getEmail());

        logger.info("\nCriando restaurantes...");
        Restaurante restaurante1 = criarRestaurante("Pizzaria Bella", "Pizza", new BigDecimal("5.00"), new BigDecimal("4.5"));
        Restaurante restaurante2 = criarRestaurante("Burger King", "Hambúrguer", new BigDecimal("3.50"), new BigDecimal("4.8"));
        
        logger.info("{} restaurantes criados", restauranteRepository.count());

        logger.info("\nCriando produtos...");
        Produto produto1 = criarProduto("Pizza Margherita", new BigDecimal("35.00"), "Pizza", restaurante1, true);
        Produto produto2 = criarProduto("Pizza Calabresa", new BigDecimal("38.00"), "Pizza", restaurante1, true);
        Produto produto3 = criarProduto("Whopper", new BigDecimal("25.00"), "Hambúrguer", restaurante2, true);
        Produto produto4 = criarProduto("Cheeseburger", new BigDecimal("15.00"), "Hambúrguer", restaurante2, true);
        Produto produto5 = criarProduto("Batata Frita", new BigDecimal("12.00"), "Acompanhamento", restaurante2, false);
        
        logger.info("{} produtos criados", produtoRepository.count());
        logger.info("   - {} produtos disponíveis", produtoRepository.findByDisponivelTrue().size());
        logger.info("   - {} produtos indisponíveis (ex: {})", 
            produtoRepository.count() - produtoRepository.findByDisponivelTrue().size(), produto5.getNome());

        logger.info("\nCriando pedidos...");
        Pedido pedido1 = criarPedido(cliente1, "PENDENTE", 
            new BigDecimal("73.00"), restaurante1.getTaxaEntrega(), 
            new BigDecimal("78.00"));
        
        Pedido pedido2 = criarPedido(cliente2, "ENTREGUE", 
            new BigDecimal("40.00"), restaurante2.getTaxaEntrega(), 
            new BigDecimal("43.50"));
        
        logger.info("{} pedidos criados", pedidoRepository.count());

        logger.info("\nCriando itens de pedido...");
        ItemPedido item1 = new ItemPedido(pedido1, produto1, 1);
        ItemPedido item2 = new ItemPedido(pedido1, produto2, 1);
        itemPedidoRepository.saveAll(Arrays.asList(item1, item2));

        ItemPedido item3 = new ItemPedido(pedido2, produto3, 1);
        ItemPedido item4 = new ItemPedido(pedido2, produto4, 1);
        itemPedidoRepository.saveAll(Arrays.asList(item3, item4));
        
        logger.info("{} itens de pedido criados", itemPedidoRepository.count());
        
        logger.info("\n=========================================");
        logger.info("VALIDANDO CONSULTAS DERIVADAS");
        logger.info("=========================================");

        logger.info("\nCenario 1: Busca de Cliente por Email");
        clienteRepository.findByEmail("joao@email.com")
            .ifPresentOrElse(
                cliente -> logger.info("Cliente encontrado: {} - {}", cliente.getNome(), cliente.getEmail()),
                () -> logger.error("Cliente nao encontrado")
            );

        logger.info("\nCenario 2: Produtos por Restaurante");
        List<Produto> produtosRestaurante1 = produtoRepository.findByRestauranteId(restaurante1.getId());
        logger.info("Produtos do restaurante {}: {}", restaurante1.getNome(), produtosRestaurante1.size());
        produtosRestaurante1.forEach(p -> logger.info("   - {} (R$ {})", p.getNome(), p.getPreco()));

        logger.info("\nCenario 3: Pedidos Recentes");
        List<Pedido> pedidosRecentes = pedidoRepository.findTop10ByOrderByDataPedidoDesc();
        logger.info("Pedidos recentes encontrados: {}", pedidosRecentes.size());
        pedidosRecentes.forEach(p -> logger.info("   - Pedido #{} - Cliente: {} - Status: {} - Total: R$ {}", 
            p.getId(), p.getCliente().getNome(), p.getStatus(), p.getValorTotal()));

        logger.info("\nCenario 4: Restaurantes por Taxa (ate R$ 5,00)");
        List<Restaurante> restaurantesComTaxaBaixa = restauranteRepository
            .findByTaxaEntregaLessThanEqual(new BigDecimal("5.00"));
        logger.info("Restaurantes encontrados: {}", restaurantesComTaxaBaixa.size());
        restaurantesComTaxaBaixa.forEach(r -> logger.info("   - {} - Taxa: R$ {}", r.getNome(), r.getTaxaEntrega()));

        logger.info("\nOutras Consultas:");
        
        logger.info("\n   Clientes Ativos:");
        List<Cliente> clientesAtivos = clienteRepository.findByAtivoTrue();
        logger.info("   {} clientes ativos encontrados", clientesAtivos.size());
        
        logger.info("\n   Busca por Nome (contem 'Joao'):");
        List<Cliente> clientesPorNome = clienteRepository.findByNomeContainingIgnoreCase("João");
        logger.info("   {} clientes encontrados", clientesPorNome.size());
        
        logger.info("\n   Verificar Email Existe:");
        boolean emailExiste = clienteRepository.existsByEmail("joao@email.com");
        logger.info("   Email 'joao@email.com' existe: {}", emailExiste);
        
        logger.info("\n   Restaurantes Ativos:");
        List<Restaurante> restaurantesAtivos = restauranteRepository.findByAtivoTrue();
        logger.info("   {} restaurantes ativos encontrados", restaurantesAtivos.size());
        
        logger.info("\n   Restaurantes por Categoria (Pizza):");
        List<Restaurante> restaurantesPizza = restauranteRepository.findByCategoria("Pizza");
        logger.info("   {} restaurantes encontrados", restaurantesPizza.size());
        
        logger.info("\n   Top 5 Restaurantes por Nome:");
        List<Restaurante> top5Restaurantes = restauranteRepository.findTop5ByOrderByNomeAsc();
        logger.info("   {} restaurantes encontrados", top5Restaurantes.size());
        top5Restaurantes.forEach(r -> logger.info("      - {}", r.getNome()));
        
        logger.info("\n   Produtos Disponiveis:");
        List<Produto> produtosDisponiveis = produtoRepository.findByDisponivelTrue();
        logger.info("   {} produtos disponiveis encontrados", produtosDisponiveis.size());
        
        logger.info("\n   Produtos por Categoria (Hamburguer):");
        List<Produto> produtosHamburguer = produtoRepository.findByCategoria("Hambúrguer");
        logger.info("   {} produtos encontrados", produtosHamburguer.size());
        
        logger.info("\n   Produtos com Preco <= R$ 20,00:");
        List<Produto> produtosBaratos = produtoRepository.findByPrecoLessThanEqual(new BigDecimal("20.00"));
        logger.info("   {} produtos encontrados", produtosBaratos.size());
        produtosBaratos.forEach(p -> logger.info("      - {} (R$ {})", p.getNome(), p.getPreco()));
        
        logger.info("\n   Pedidos por Cliente (ID: {})", cliente1.getId());
        List<Pedido> pedidosCliente1 = pedidoRepository.findByClienteId(cliente1.getId());
        logger.info("   {} pedidos encontrados", pedidosCliente1.size());
        
        logger.info("\n   Pedidos por Status (PENDENTE):");
        List<Pedido> pedidosPendentes = pedidoRepository.findByStatus("PENDENTE");
        logger.info("   {} pedidos pendentes encontrados", pedidosPendentes.size());
        
        logger.info("\n   Pedidos por Periodo:");
        LocalDateTime inicio = LocalDateTime.now().minusDays(1);
        LocalDateTime fim = LocalDateTime.now().plusDays(1);
        List<Pedido> pedidosPeriodo = pedidoRepository.findByDataPedidoBetween(inicio, fim);
        logger.info("   {} pedidos no periodo encontrados", pedidosPeriodo.size());
        
        logger.info("\n=========================================");
        logger.info("VALIDANDO CONSULTAS CUSTOMIZADAS");
        logger.info("=========================================");

        logger.info("\n   Pedidos com Valor Acima de R$ 50,00:");
        List<Pedido> pedidosValorAlto = pedidoRepository.findPedidosComValorAcimaDe(new BigDecimal("50.00"));
        logger.info("   {} pedidos encontrados", pedidosValorAlto.size());
        pedidosValorAlto.forEach(p -> logger.info("      - Pedido #{} - Total: R$ {}", p.getId(), p.getValorTotal()));

        logger.info("\n   Relatorio por Periodo e Status:");
        List<Pedido> pedidosRelatorio = pedidoRepository.findPedidosPorPeriodoEStatus(inicio, fim, "ENTREGUE");
        logger.info("   {} pedidos entregues no periodo encontrados", pedidosRelatorio.size());

        logger.info("\n   Total de Vendas por Restaurante:");
        List<Object[]> vendasPorRestaurante = pedidoRepository.findTotalVendasPorRestaurante();
        logger.info("   {} restaurantes com vendas encontrados", vendasPorRestaurante.size());
        vendasPorRestaurante.forEach(v -> logger.info("      - {} (ID: {}) - Total: R$ {}", v[1], v[0], v[2]));

        logger.info("\n   Produtos Mais Vendidos (Top 5):");
        List<Object[]> produtosMaisVendidos = itemPedidoRepository.findProdutosMaisVendidos(5);
        logger.info("   {} produtos encontrados", produtosMaisVendidos.size());
        produtosMaisVendidos.forEach(p -> logger.info("      - {} - Quantidade: {}", p[1], p[2]));

        logger.info("\n   Ranking de Clientes por Nº de Pedidos (Top 5):");
        List<Object[]> rankingClientes = itemPedidoRepository.findRankingClientesPorPedidos(5);
        logger.info("   {} clientes encontrados", rankingClientes.size());
        rankingClientes.forEach(c -> logger.info("      - {} - Total de Pedidos: {}", c[1], c[2]));

        logger.info("\n   Faturamento por Categoria:");
        List<Object[]> faturamentoPorCategoria = itemPedidoRepository.findFaturamentoPorCategoria();
        logger.info("   {} categorias encontradas", faturamentoPorCategoria.size());
        faturamentoPorCategoria.forEach(f -> logger.info("      - {} - Faturamento: R$ {}", f[0], f[1]));

        logger.info("\n=========================================");
        logger.info("CARREGAMENTO E VALIDACAO CONCLUIDOS");
        logger.info("=========================================\n");
    }

    private Cliente criarCliente(String nome, String email, boolean ativo) {
        Cliente cliente = new Cliente();
        cliente.setNome(nome);
        cliente.setEmail(email);
        cliente.setAtivo(ativo);
        return clienteRepository.save(cliente);
    }

    private Restaurante criarRestaurante(String nome, String categoria, BigDecimal taxaEntrega, BigDecimal avaliacao) {
        Restaurante restaurante = new Restaurante();
        restaurante.setNome(nome);
        restaurante.setCategoria(categoria);
        restaurante.setTaxaEntrega(taxaEntrega);
        restaurante.setAvaliacao(avaliacao);
        restaurante.setAtivo(true);
        return restauranteRepository.save(restaurante);
    }

    private Produto criarProduto(String nome, BigDecimal preco, String categoria, Restaurante restaurante, boolean disponivel) {
        Produto produto = new Produto();
        produto.setNome(nome);
        produto.setPreco(preco);
        produto.setCategoria(categoria);
        produto.setRestaurante(restaurante);
        produto.setDisponivel(disponivel);
        return produtoRepository.save(produto);
    }

    private Pedido criarPedido(Cliente cliente, String status, BigDecimal valorSubtotal, BigDecimal valorEntrega, BigDecimal valorTotal) {
        Pedido pedido = new Pedido();
        pedido.setCliente(cliente);
        pedido.setStatus(status);
        pedido.setValorSubtotal(valorSubtotal);
        pedido.setValorEntrega(valorEntrega);
        pedido.setValorTotal(valorTotal);
        pedido.setDataPedido(LocalDateTime.now());
        return pedidoRepository.save(pedido);
    }
}

