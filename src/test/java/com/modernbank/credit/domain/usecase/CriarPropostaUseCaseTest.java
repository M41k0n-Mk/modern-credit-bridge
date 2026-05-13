package com.modernbank.credit.domain.usecase;

import com.modernbank.credit.domain.model.Proposta;
import com.modernbank.credit.domain.repository.PropostaRepository;
import com.modernbank.credit.domain.service.ClienteHistoricoService;
import com.modernbank.credit.domain.service.RiscoCliente;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Testes unitários para CriarPropostaUseCase.
 * 
 * Padrão: AAA (Arrange, Act, Assert)
 * - Arrange: Preparar dados e mocks
 * - Act: Executar a ação
 * - Assert: Verificar o resultado
 * 
 * @author ModernBank
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("Testes - CriarPropostaUseCase")
class CriarPropostaUseCaseTest {

    @Mock
    private PropostaRepository repositoryMock;

    @Mock
    private ClienteHistoricoService clienteHistoricoService;

    private CriarPropostaUseCase useCase;

    @BeforeEach
    void setUp() {
        this.useCase = new CriarPropostaUseCase(repositoryMock, clienteHistoricoService);
    }

    @Test
    @DisplayName("Deve criar proposta com sucesso ao chamar repositório")
    void deveCriarPropostaBuscandoDoRepositorio() {
        // Arrange
        String cpf = "12345678900";
        BigDecimal valor = new BigDecimal("1000.00");
        UUID idGerado = UUID.randomUUID();

        Proposta propostaEntrada = new Proposta(cpf, valor);
        Proposta propostaSalva = new Proposta(idGerado, cpf, valor, "PENDENTE");

        when(clienteHistoricoService.avaliarRisco(any(), any())).thenReturn(RiscoCliente.BAIXO);
        when(repositoryMock.salvar(any(Proposta.class)))
                .thenReturn(propostaSalva);

        // Act
        Proposta resultado = useCase.executar(propostaEntrada);

        // Assert
        assertNotNull(resultado);
        assertEquals(idGerado, resultado.getId());
        assertEquals(cpf, resultado.getCpf());
        assertEquals(valor, resultado.getValor());
        assertEquals("PENDENTE", resultado.getStatus());

        // Verifica se o repositório foi chamado com uma proposta
        verify(repositoryMock).salvar(any(Proposta.class));
    }

    @Test
    @DisplayName("Deve lançar exceção quando proposta é nula")
    void deveLancarExcecaoQuandoPropostaNula() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            useCase.executar(null);
        });
    }

    @Test
    @DisplayName("Deve lançar exceção quando repositório é nulo")
    void deveLancarExcecaoQuandoRepositorioNulo() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            new CriarPropostaUseCase(null, clienteHistoricoService);
        });
    }

    @Test
    @DisplayName("Deve lançar exceção quando ClienteHistoricoService é nulo")
    void deveLancarExcecaoQuandoClienteHistoricoServiceNulo() {
        assertThrows(IllegalArgumentException.class, () -> {
            new CriarPropostaUseCase(repositoryMock, null);
        });
    }

    @Test
    @DisplayName("Deve validar CPF na proposta dentro do use case")
    void deveValidarCpfNaProposta() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            new Proposta("", new BigDecimal("1000.00"));
        });
    }

    @Test
    @DisplayName("Deve validar valor na proposta dentro do use case")
    void deveValidarValorNaProposta() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            new Proposta("12345678900", new BigDecimal("0.00"));
        });
    }

    @Test
    @DisplayName("Deve chamar o repositório exatamente uma vez")
    void deveCharmarRepositorioUmaVez() {
        // Arrange
        Proposta proposta = new Proposta("12345678900", new BigDecimal("1000.00"));
        Proposta propostaSalva = new Proposta(UUID.randomUUID(), proposta.getCpf(), proposta.getValor(), "PENDENTE");

        when(clienteHistoricoService.avaliarRisco(any(), any())).thenReturn(RiscoCliente.BAIXO);
        when(repositoryMock.salvar(any(Proposta.class)))
                .thenReturn(propostaSalva);

        // Act
        useCase.executar(proposta);

        // Assert
        verify(repositoryMock).salvar(any(Proposta.class));
    }
}
