package dentoice.util.invoice.xml

import com.mariakamachine.dentoice.config.properties.InvoiceProperties
import com.mariakamachine.dentoice.data.entity.CostWrapperEntity
import com.mariakamachine.dentoice.data.entity.DentistEntity
import com.mariakamachine.dentoice.data.entity.InvoiceEntity
import com.mariakamachine.dentoice.data.jsonb.EffortJsonb
import com.mariakamachine.dentoice.data.jsonb.MaterialJsonb
import com.mariakamachine.dentoice.util.invoice.xml.InvoiceConverter
import spock.lang.Specification

import static com.mariakamachine.dentoice.util.invoice.xml.XmlGenerator.generateInvoiceXmlFile
import static java.time.LocalDate.of

class XmlGeneratorSpec extends Specification {

    def "generate valid xml from invoice entity"() {
        given:
        def properties = [xsdVersion: "4.0", softwareVersion: "0.0.1"] as InvoiceProperties

        def efforts = [
                [position: "0732", name: "Desinfektion", quantity: 1.00, pricePerUnit: 6.32] as EffortJsonb,
                [position: "0001", name: "Modell aus Hartgips", quantity: 2.00, pricePerUnit: 8.08] as EffortJsonb
        ] as List

        def materials = [
                [position: "9005", name: "PlatinLloyd 100", quantity: 3.6, pricePerUnit: 51.25, notes: "BEGO Legierung CE 4004", isMetal: true] as MaterialJsonb
        ] as List

        def invoice = [id           : 18031234,
                       dentist      : [id: 1, lastName: "Squarepants", street: "Pineapple", zip: "0815", city: "Bikini Bottom"] as DentistEntity,
                       patient      : "Pfeil, Susanne",
                       description  : "36 VG-Krone",
                       xmlNumber    : "617200-F7357-KB-66399-2044-2",
                       invoiceType  : "INVOICE",
                       insuranceType: "KASSE",
                       date         : of(2018, 03, 13),
                       mwst         : 7,
                       costs        : [efforts: efforts, materials: materials] as CostWrapperEntity
        ] as InvoiceEntity

        when:
        def xmlResource = generateInvoiceXmlFile new InvoiceConverter().convertToXmlModel(invoice, properties)

        then:
        xmlResource.fileName == "${invoice.xmlNumber}.xml"
        xmlResource.resource.inputStream.newReader().text ==
                '''\
                <?xml version="1.0" encoding="UTF-8" standalone="yes"?>
                <Laborabrechnung Version="4.0" xsi:noNamespaceSchemaLocation="Laborabrechnungsdaten_(KZBV-VDZI-VDDS)_(V4-4).xsd" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
                    <Rechnung Laborsoftwarehersteller="Marius Baumann" Laborsoftware="dentoice" Laborsoftwareversion="0.0.1" Laborname="Dentaltechnik Udo Baumann" Herstellungsort="D-Rottenburg" Abrechnungsbereich="BW" Laborlieferdatum="2018-03-13" Laborrechnungsnummer="18031234" Auftragsnummer="617200-F7357-KB-66399-2044-2" Gesamtbetrag_netto="20698" Mehrwertsteuer_gesamt="1449" Gesamtbetrag_brutto="22147">
                        <MWST-Gruppe Zwischensumme_netto="20698" Mehrwertsteuersatz="70" Mehrwertsteuerbetrag="1449">
                            <Position Art="BEL" Nummer="0732" Beschreibung="Desinfektion" Einzelpreis="632" Menge="1000"/>
                            <Position Art="BEL" Nummer="0001" Beschreibung="Modell aus Hartgips" Einzelpreis="808" Menge="2000"/>
                            <Position Art="MAT" Beschreibung="PlatinLloyd 100" Einzelpreis="5125" Menge="3600"/>
                        </MWST-Gruppe>
                    </Rechnung>
                </Laborabrechnung>
                '''.stripIndent()
    }

}
