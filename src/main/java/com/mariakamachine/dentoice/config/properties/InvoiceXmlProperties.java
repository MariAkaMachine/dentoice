package com.mariakamachine.dentoice.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("invoice.xml")
@Data
public class InvoiceXmlProperties {

    private String xsdVersion;
    private String softwareVersion;
    private Double mwstInProzent;

}
