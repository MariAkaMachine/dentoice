package com.mariakamachine.dentoice.util.invoice.xml;

import com.mariakamachine.dentoice.model.xml.Laborabrechnung;
import lombok.extern.slf4j.Slf4j;

import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;

import static com.google.common.io.Files.asCharSink;
import static java.lang.String.format;
import static java.nio.charset.StandardCharsets.UTF_8;
import static javax.xml.bind.JAXBContext.newInstance;
import static javax.xml.bind.Marshaller.*;

@Slf4j
public class XmlGenerator {

    private static final String NO_NAMESPACE_SCHEMA_LOCATION = "Laborabrechnungsdaten_(KZBV-VDZI-VDDS)_(V4-4).xsd";

    public static File generateInvoiceXmlFile(Laborabrechnung laborabrechnung) {
        File xmlFile = new File(format("%s.xml", laborabrechnung.getRechnung().getXmlNummer()));
        StringWriter writer = new StringWriter();
        try {
            createLaborabrechnungMarshaller().marshal(laborabrechnung, writer);
            // OLD: com.google.common.io.Files.write(writer.toString(), xmlFile, UTF_8);
            asCharSink(xmlFile, UTF_8).write(writer.toString());
        } catch (IOException | JAXBException e) {
            log.error("could not generate xml file", e);
        }
        return xmlFile;
    }

    private static Marshaller createLaborabrechnungMarshaller() {
        Marshaller marshaller;
        try {
            marshaller = newInstance(Laborabrechnung.class).createMarshaller();
            marshaller.setProperty(JAXB_FORMATTED_OUTPUT, true);
            marshaller.setProperty(JAXB_ENCODING, UTF_8.displayName());
            marshaller.setProperty(JAXB_NO_NAMESPACE_SCHEMA_LOCATION, NO_NAMESPACE_SCHEMA_LOCATION);
        } catch (Exception e) {
            log.error("could not create xml marshaller");
            throw new RuntimeException("could not create xml marshaller", e);
        }
        return marshaller;
    }

}
