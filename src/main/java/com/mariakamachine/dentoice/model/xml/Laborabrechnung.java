package com.mariakamachine.dentoice.model.xml;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import static javax.xml.bind.annotation.XmlAccessType.FIELD;

@AllArgsConstructor
@NoArgsConstructor
@Data
@XmlAccessorType(FIELD)
@XmlRootElement(name = "Laborabrechnung")
public class Laborabrechnung {

    @XmlAttribute(name = "Version")
    private String version;

    @XmlElement(name = "Rechnung")
    private Rechnung rechnung = new Rechnung();

}
