```java
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class OrderManager {
  public void processOrder(int orderId, double amount, String region, String customerEmail) {

  // Bloco A
  double shippingCost = 0;

  if (region.equalsIgnoreCase("SUL")) {
    shippingCost = 20.00;
    }

  else if (region.equalsIgnoreCase("NORTE")) {
    shippingCost = 45.00;
    }

  else {
    shippingCost = 30.00;
    }

  if (amount > 500) {
    shippingCost = 0;
    }

  double finalAmount = amount + shippingCost;

  // Bloco B
  try {
    Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/shop","user", "pass");
    String sql = "INSERT INTO orders (id, total, region) VALUES (?, ?, ?)";
    PreparedStatement stmt = conn.prepareStatement(sql);
    stmt.setInt(1, orderId);
    stmt.setDouble(2, finalAmount);
    stmt.setString(3, region);
    stmt.execute();
    conn.close();
      }

  catch (Exception e) {
    e.printStackTrace();
      }

  // Bloco C
  System.out.println("--- EMAIL DISPATCHER ---");
  System.out.println("To: " + customerEmail);
  System.out.println("Subject: Order Confirmed");
  System.out.println("Body: Your order #" + orderId + " is confirmed. Total: " + finalAmount);
  System.out.printl("------------------------");
  }
}
```

# Parte A: Diagnóstico

Identifique e anote:

1.  Quais são as três responsabilidades distintas que a classe `OrderManager` assumiu para si indevidamente?

A classe fere o Princípio da Responsabilidade Única, há Baixa Coesão. No Bloco A temos as regras de negócios quanto ao valor do frete variar de acordo com a região destinada ou acima do valor do montante. No bloco B há uma conexão direta com o banco de dados. Por fim, no bloco C, a classe assume o papel de formatar uma mensagem por e-mail para o cliente.

2.  O que acontece com a lógica de negócio (cálculo de frete) se o banco de dados cair ou a senha do banco mudar? Isso é aceitável?

Se o banco cair, o método inteiro falha e lança uma exceção, interrompendo o processo. A lógica de negócio fica totalmente refém da infraestrutura de dados. Isso não é aceitável, pois demonstra um Acoplamento Patológico. A sua regra de cálculo de frete não deveria parar de funcionar só porque o banco de dados está fora do ar.

# Parte B: Refatoração

Reescreva a solução em Java segregando as responsabilidades.

- Não utilize frameworks (como Spring ou Hibernate) agora; faça a injeção de dependência manualmente via construtor.
- Crie classes especialistas para cada responsabilidade identificada.
- A classe OrderManager (ou OrderService) deve apenas coordenar o fluxo.

```java
// Main.java
public class Main {
    public static void main(String[] args) {
        // Injeção manual das dependências
        ShippingCalculator calculator = new ShippingCalculator();
        OrderRepository repository = new OrderRepository();
        EmailService emailService = new EmailService();

        OrderService orderService = new OrderService(calculator, repository, emailService);

        // Testando o fluxo
        orderService.processOrder(123, 600.00, "SUL", "cliente@email.com");
    }
}
```

```java
// ShippingCalculator.java
public class ShippingCalculator {

    public double calculateShipping(String region, double amount) {
        double shippingCost = 0;

        if (region.equalsIgnoreCase("SUL")) {
            shippingCost = 20.00;
        } else if (region.equalsIgnoreCase("NORTE")) {
            shippingCost = 45.00;
        } else {
            shippingCost = 30.00;
        }

        if (amount > 500) {
            shippingCost = 0;
        }

        return shippingCost;
    }
}
```

```java
// OrderRepository.java
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class OrderRepository {

    public void save(int orderId, double finalAmount, String region) throws Exception {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/shop", "user", "pass");
            String sql = "INSERT INTO orders (id, total, region) VALUES (?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, orderId);
            stmt.setDouble(2, finalAmount);
            stmt.setString(3, region);
            stmt.execute();
        } finally {
            if (conn != null) {
                try { conn.close(); } catch (Exception e) { /* log */ }
            }
        }
    }
}
```

```java
// EmailService.java
public class EmailService {

    public void sendOrderConfirmation(String customerEmail, int orderId, double finalAmount) {
        System.out.println("--- EMAIL DISPATCHER ---");
        System.out.println("To: " + customerEmail);
        System.out.println("Subject: Order Confirmed");
        System.out.println("Body: Your order #" + orderId + " is confirmed. Total: " + finalAmount);
        System.out.println("------------------------");
    }
}
```

```java
// OrderService.java (antigo OrderManager)
public class OrderService {

    private final ShippingCalculator shippingCalculator;
    private final OrderRepository orderRepository;
    private final EmailService emailService;

    // Injeção de dependência via construtor (manual)
    public OrderService(
            ShippingCalculator shippingCalculator,
            OrderRepository orderRepository,
            EmailService emailService) {
        this.shippingCalculator = shippingCalculator;
        this.orderRepository = orderRepository;
        this.emailService = emailService;
    }

    public void processOrder(int orderId, double amount, String region, String customerEmail) {
        // 1. Calcula o frete usando a classe especialista
        double shippingCost = shippingCalculator.calculateShipping(region, amount);
        double finalAmount = amount + shippingCost;

        // 2. Tenta salvar no banco (mas não deixa o fluxo principal quebrar)
        try {
            orderRepository.save(orderId, finalAmount, region);
        } catch (Exception e) {
            // Log do erro, mas não interrompe o processo
            System.err.println("Erro ao salvar pedido no banco: " + e.getMessage());
            // Aqui poderia ter um mecanismo de retry ou fila para tentar depois
        }

        // 3. Envia o email (sempre executa, independente do banco)
        emailService.sendOrderConfirmation(customerEmail, orderId, finalAmount);
    }
}
```
