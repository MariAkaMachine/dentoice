package com.mariakamachine.dentoice.model.xml;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

import static javax.xml.bind.annotation.XmlAccessType.FIELD;

@AllArgsConstructor
@NoArgsConstructor
@Data
@XmlAccessorType(FIELD)
public class Position {

    @XmlAttribute(name = "Art")
    private Art art;

    @XmlAttribute(name = "Nummer")
    private String nummer;

    @XmlAttribute(name = "Beschreibung")
    private String beschreibung;

    @XmlAttribute(name = "Einzelpreis")
    private Integer einzelpreis;

    @XmlAttribute(name = "Menge")
    private Integer menge;

}
