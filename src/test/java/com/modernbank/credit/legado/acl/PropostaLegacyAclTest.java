package com.modernbank.credit.legado.acl;

import com.modernbank.credit.context.legado.acl.PropostaLegacyAcl;
import com.modernbank.credit.context.legado.adapter.PropostaLegacyDTO;
import com.modernbank.credit.context.legado.adapter.LegadoMainframeClient;
import com.modernbank.credit.context.propostas.domain.model.Proposta;
import com.modernbank.credit.context.propostas.domain.model.PropostaStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ACL: Mapeamento de registros legado para Proposta")
class PropostaLegacyAclTest {

    @Test
    @DisplayName("Deve mapear uma linha legado para Proposta corretamente")
    void deveMapearLinhaLegadoParaProposta() {
        // Arrange - pega a primeira linha de exemplo
        List<String> linhas = LegadoMainframeClient.lerLinhasExemplo();
        String linha = linhas.get(0); // "0001|12345678900|00000010000|20260515|S"
        PropostaLegacyDTO dto = PropostaLegacyDTO.fromLine(linha);

        // Act
        Proposta proposta = PropostaLegacyAcl.mapearParaProposta(dto);

        // Assert
        assertNotNull(proposta);
        assertEquals("12345678900", proposta.getCpf().getValor());
        assertEquals(new BigDecimal("100.00"), proposta.getValor().getValor());
        assertEquals(PropostaStatus.SUBMETIDA, proposta.getStatus());
    }

    @Test
    @DisplayName("Deve mapear todas as linhas de exemplo sem lançar exceção")
    void deveMapearTodasLinhasExemplo() {
        List<String> linhas = LegadoMainframeClient.lerLinhasExemplo();
        for (String linha : linhas) {
            PropostaLegacyDTO dto = PropostaLegacyDTO.fromLine(linha);
            Proposta proposta = PropostaLegacyAcl.mapearParaProposta(dto);
            assertNotNull(proposta);
        }
    }
}

