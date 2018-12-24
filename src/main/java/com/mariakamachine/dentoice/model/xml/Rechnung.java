package com.mariakamachine.dentoice.model.xml;

import com.mariakamachine.dentoice.util.invoice.xml.LocalDateXmlAdapter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.time.LocalDate;
import java.util.List;

import static javax.xml.bind.annotation.XmlAccessType.FIELD;

@AllArgsConstructor
@NoArgsConstructor
@Data
@XmlAccessorType(FIELD)
public class Rechnung {

    @XmlAttribute(name = "Laborsoftwarehersteller")
    private final String softwareHersteller = "Marius Baumann";

    @XmlAttribute(name = "Laborsoftware")
    private final String LaborSoftware = "dentoice";

    @XmlAttribute(name = "Laborsoftwareversion")
    private String laborSoftwareVersion;

    @XmlAttribute(name = "Laborname")
    private final String laborName = "Dentaltechnik Udo Baumann";

    @XmlAttribute(name = "Herstellungsort")
    private final String herstellungsort = "D-Rottenburg";

    @XmlAttribute(name = "Abrechnungsbereich")
    private final String abrechnungsbereich = "BW";

    @XmlJavaTypeAdapter(value = LocalDateXmlAdapter.class)
    @XmlAttribute(name = "Laborlieferdatum")
    private LocalDate laborLieferDatum;

    @XmlAttribute(name = "Laborrechnungsnummer")
    private Long laborRechnungsNummer;

    @XmlAttribute(name = "Auftragsnummer")
    private String xmlNummer;

    @XmlAttribute(name = "Gesamtbetrag_netto")
    private Integer netto;

    @XmlAttribute(name = "Mehrwertsteuer_gesamt")
    private Integer mwst;

    @XmlAttribute(name = "Gesamtbetrag_brutto")
    private Integer brutto;

    @XmlElement(name = "MWST-Gruppe")
    private List<MwstGruppe> mwstGruppe;

}
