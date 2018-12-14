package com.mariakamachine.dentoice.xml;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import java.util.List;

import static javax.xml.bind.annotation.XmlAccessType.FIELD;

@AllArgsConstructor
@NoArgsConstructor
@Data
@XmlAccessorType(FIELD)
public class MwstGruppe {

    @XmlAttribute(name = "Zwischensumme_netto")
    private Integer netto;

    @XmlAttribute(name = "Mehrwertsteuersatz")
    private Integer mwstSatz;

    @XmlAttribute(name = "Mehrwertsteuerbetrag")
    private Integer mwstBetrag;

    @XmlElement(name = "Position")
    private List<Position> positionen;

}
