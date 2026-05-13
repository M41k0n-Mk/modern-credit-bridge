package com.modernbank.credit.infrastructure.output.mainframe;

import com.modernbank.credit.domain.service.ClienteHistoricoService;
import com.modernbank.credit.domain.service.RiscoCliente;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * Adapter de infraestrutura que simula a consulta ao Neptune
 * para avaliar o risco de um cliente. Em produção, este componente
 * faria chamadas via mensageria/HTTP/conectores proprietários.
 */
@Component
public class ClienteHistoricoServiceAdapter implements ClienteHistoricoService {

    @Override
    public RiscoCliente avaliarRisco(String cpf, BigDecimal valor) {
        if (cpf == null || cpf.isBlank()) {
            return RiscoCliente.MEDIO;
        }

        // Heurística simples apenas para demonstração:
        // - CPFs terminando com "000" considerados alto risco (suspeitos)
        // - Valores muito altos recebem risco médio
        if (cpf.endsWith("000")) {
            return RiscoCliente.ALTO;
        }
        if (valor != null && valor.compareTo(new BigDecimal("10000")) > 0) {
            return RiscoCliente.MEDIO;
        }
        return RiscoCliente.BAIXO;
    }
}
