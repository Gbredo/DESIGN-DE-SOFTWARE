# Atividade Prática: Refatoração de Múltiplos Componentes Legados

## Fase 1: Diagnóstico Arquitetural

### 1. Análise da classe `CalculadoraBeneficios`

```java
public class CalculadoraBeneficios { 
 
    public double calcularValeAlimentacao(String cargo, double salarioBase) { 
        if (cargo.equalsIgnoreCase("ESTAGIARIO")) { 
            return 250.00; 
        } else if (cargo.equalsIgnoreCase("JUNIOR") || cargo.equalsIgnoreCase("PLENO")) { 
            return 600.00; 
        } else if (cargo.equalsIgnoreCase("SENIOR") || cargo.equalsIgnoreCase("DIRETOR")) { 
            return 850.00; 
        } 
        throw new IllegalArgumentException("Cargo não reconhecido na matriz de benefícios."); 
    } 
 
    public double calcularAuxilioSaude(String cargo) { 
        if (cargo.equalsIgnoreCase("DIRETOR")) { 
            return 1500.00; 
        } 
        return 300.00; 
    } 
}
```

- A classe viola diretamente o Princípio do Aberto/Fechado (OCP), que determina que uma entidade de software deve estar aberta para extensão, mas fechada para modificação.
- Atualmente, a lógica depende de pesadas ramificações condicionais (if/else) baseadas em texto.
- Para cumprir a exigência do memorando e adicionar os cargos de "Especialista" e "Consultor Externo", o desenvolvedor seria obrigado a alterar diretamente o código-fonte original, inserindo novos blocos else if.
- Essa prática arrisca a integridade da estrutura atual, pois introduz a possibilidade de gerar efeitos colaterais (bugs) nas regras já consolidadas e testadas dos outros cargos, tornando a manutenção futura extremamente difícil.

### 2. Análise da classe GeradorRelatorioRH

```java
import java.io.FileWriter; 
import java.io.IOException; 
 
public class GeradorRelatorioRH { 
 
    public void emitirRelatorioMensal(String nomeFuncionario, double salarioLiquido, String formato) { 
        if (formato.equalsIgnoreCase("PDF")) { 
            System.out.println("Iniciando conversão binária para PDF..."); 
            System.out.println("Documento PDF gerado: Funcionario: " + nomeFuncionario + " | Salario: " + 
salarioLiquido); 
        } else if (formato.equalsIgnoreCase("CSV")) { 
            try { 
                FileWriter csvWriter = new FileWriter("relatorio_rh.csv", true); 
                
csvWriter.append(nomeFuncionario).append(";").append(String.valueOf(salarioLiquido)).appen
d("\n"); 
                csvWriter.close(); 
            } catch (IOException e) { 
                e.printStackTrace(); 
            } 
        } else { 
            System.out.println("Formato não suportado para emissão."); 
        } 
    } 
} 
```
- Esta classe apresenta uma dupla violação de princípios estruturais. Primeiramente, fere o Princípio do Aberto/Fechado (OCP) devido ao uso de estruturas condicionais rigidamente acopladas aos formatos de exportação.
- A exigência governamental de adicionar o formato XML obrigaria a modificação direta da classe com a inserção de um novo bloco else if, impedindo a extensão limpa do sistema.
- Em segundo lugar, viola o Princípio da Responsabilidade Única (SRP) ao misturar a lógica de formatação de apresentação (como a concatenação de dados para o CSV) com a lógica de infraestrutura de I/O (manipulação direta de arquivos no sistema operacional via FileWriter e tratamento de IOException). Isso confere à classe múltiplos motivos para mudar, elevando seu acoplamento.

### 3. Análise da classe ProcessadorFolhaPagamento

```java
import java.sql.Connection; 
import java.sql.DriverManager; 
import java.sql.PreparedStatement; 
 
public class ProcessadorFolhaPagamento { 
 
    public void processarPagamento(String cpf, String nome, String cargo, double salarioBase, String 
formatoRelatorio) { 
         
        CalculadoraBeneficios calcBeneficios = new CalculadoraBeneficios(); 
        double va = calcBeneficios.calcularValeAlimentacao(cargo, salarioBase); 
        double saude = calcBeneficios.calcularAuxilioSaude(cargo); 
         
        double descontos = salarioBase * 0.11;  
        double salarioLiquido = (salarioBase + va + saude) - descontos; 
 
        try { 
            Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@servidor_bd:1521:rh", 
"sysdba", "senha_forte"); 
            String sql = "INSERT INTO pagamentos (cpf, valor_liquido, status) VALUES (?, ?, 'PROCESSADO')"; 
            PreparedStatement stmt = conn.prepareStatement(sql); 
            stmt.setString(1, cpf); 
            stmt.setDouble(2, salarioLiquido); 
            stmt.executeUpdate(); 
            conn.close(); 
        } catch (Exception e) { 
            e.printStackTrace(); 
            return; 
        } 
 
        GeradorRelatorioRH relatorio = new GeradorRelatorioRH(); 
        relatorio.emitirRelatorioMensal(nome, salarioLiquido, formatoRelatorio); 
 
        System.out.println("Enviando requisição de transferência via API REST para o Banco Central..."); 
        System.out.println("{\"cpf\": \"" + cpf + "\", \"valor\": " + salarioLiquido + "}"); 
    } 
} 
```
- A classe apresenta uma grave violação do Princípio da Responsabilidade Única (SRP) ao assumir o papel de uma "God Class" (Classe Deus). Em um único método, ela acumula múltiplas funções distintas: orquestra regras de negócio matemáticas (cálculos de RH), manipula a infraestrutura de persistência (conexão direta via JDBC com banco Oracle) e gerencia a comunicação externa (simulação de envio para API REST).
- Além disso, a inicialização direta de objetos por meio de new CalculadoraBeneficios() e new GeradorRelatorioRH() gera um Acoplamento Patológico. O orquestrador passa a depender rigidamente dessas implementações exatas; se a estrutura interna de qualquer uma dessas dependências mudar, o ProcessadorFolhaPagamento fatalmente será corrompido, quebrando junto e inviabilizando a manutenção do sistema.

## Fase 2: Refatoração Estrutural

### A. Abstração de Benefícios (OCP)

```java
public interface PoliticaBeneficios {
    double calcularValeAlimentacao(double salarioBase);
    double calcularAuxilioSaude();
}
```

```java
public class BeneficiosDiretor implements PoliticaBeneficios {

    @Override
    public double calcularValeAlimentacao(double salarioBase) {
        return 850.00;
    }

    @Override
    public double calcularAuxilioSaude() {
        return 1500.00;
    }
}
```

```java
public class BeneficiosEspecialista implements PoliticaBeneficios {

    @Override
    public double calcularValeAlimentacao(double salarioBase) {
        return 2000.00;
    }

    @Override
    public double calcularAuxilioSaude() {
        return 5000.00;
    }
}
```

### B. Abstração de Formatação (OCP e SRP)

```java
// O Contrato (Interface) adaptado para o domínio de RH
public interface ExportadorRelatorio {
    void exportar(String nomeFuncionario, double salarioLiquido);
}

// Extensão 1
public class ExportadorPDF implements ExportadorRelatorio {
    @Override
    public void exportar(String nomeFuncionario, double salarioLiquido) {
        System.out.println("Gerando PDF -> Funcionário: " + nomeFuncionario + " | Salário: " + salarioLiquido);
    }
}

// Extensão 2
public class ExportadorCSV implements ExportadorRelatorio {
    @Override
    public void exportar(String nomeFuncionario, double salarioLiquido) {
        // Simulando a gravação limpa no disco
        System.out.println("Gravando CSV: " + nomeFuncionario + ";" + salarioLiquido);
    }
}

// Extensão 3: A Nova Exigência do Memorando!
public class ExportadorXML implements ExportadorRelatorio {
    @Override
    public void exportar(String nomeFuncionario, double salarioLiquido) {
        System.out.println("<relatorio>");
        System.out.println("  <funcionario>" + nomeFuncionario + "</funcionario>");
        System.out.println("  <salario>" + salarioLiquido + "</salario>");
        System.out.println("</relatorio>");
    }
}
```

```java
public class GeradorRelatorioRH {
    private ExportadorRelatorio exportador;

    // Construtor recebendo a estratégia escolhida
    public GeradorRelatorioRH(ExportadorRelatorio exportador) {
        this.exportador = exportador;
    }

    public void emitirRelatorioMensal(String nomeFuncionario, double salarioLiquido) {
        // A mágica do polimorfismo acontece aqui!
        exportador.exportar(nomeFuncionario, salarioLiquido);
    }
}
```

### C. Segregação de Infraestrutura (SRP)

```java
// Contrato isolado para persistência no Banco de Dados
public interface RepositorioPagamento {
    void salvar(String cpf, double salarioLiquido);
}
```

```java
// Contrato isolado para comunicação externa (API)
public interface ServicoTransferencia {
    void transferir(String cpf, double valor);
}
```

### D. Inversão de Dependência

```java
public class ProcessadorFolhaPagamento {

    // Dependências blindadas por interfaces
    private final PoliticaBeneficios calculadora;
    private final RepositorioPagamento repositorio;
    private final ExportadorRelatorio geradorRelatorio;
    private final ServicoTransferencia servicoTransferencia;

    // Injeção de Dependência pelo construtor
    public ProcessadorFolhaPagamento(
            PoliticaBeneficios calculadora,
            RepositorioPagamento repositorio,
            ExportadorRelatorio geradorRelatorio,
            ServicoTransferencia servicoTransferencia) {

        this.calculadora = calculadora;
        this.repositorio = repositorio;
        this.geradorRelatorio = geradorRelatorio;
        this.servicoTransferencia = servicoTransferencia;
    }

    public void processarPagamento(String cpf, String nome, double salarioBase) {
        // 1. Regras de Negócio (Matemática pura, sem condicionais de cargo)
        double va = calculadora.calcularValeAlimentacao(salarioBase);
        double saude = calculadora.calcularAuxilioSaude();
        double descontos = salarioBase * 0.11;
        double salarioLiquido = (salarioBase + va + saude) - descontos;

        // 2. Persistência (Delega para o especialista em Banco de Dados)
        repositorio.salvar(cpf, salarioLiquido);

        // 3. Apresentação (Delega para o especialista em Relatórios)
        geradorRelatorio.exportar(nome, salarioLiquido);

        // 4. Integração (Delega para o especialista em Rede/APIs)
        servicoTransferencia.transferir(cpf, salarioLiquido);
    }
}
```