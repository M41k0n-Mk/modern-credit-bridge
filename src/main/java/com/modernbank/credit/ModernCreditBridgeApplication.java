package com.modernbank.credit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Aplicação principal do Modern Credit Bridge.
 * Inicializa a aplicação Spring Boot com auto-configuração.
 * 
 * Pacotes escaneados automaticamente:
 * - com.modernbank.credit.* (todas as camadas)
 * - Componentes @Component, @Service, @Repository, @Controller
 * - Beans configurados com @Configuration
 *
 */
@SpringBootApplication
public class ModernCreditBridgeApplication {

    public static void main(String[] args) {
        SpringApplication.run(ModernCreditBridgeApplication.class, args);
    }
}
