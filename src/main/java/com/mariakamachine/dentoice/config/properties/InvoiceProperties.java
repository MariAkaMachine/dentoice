package com.mariakamachine.dentoice.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("invoice")
@EnableConfigurationProperties
@Data
public class InvoiceProperties {

    private String xsdVersion;
    private String softwareVersion;

}
