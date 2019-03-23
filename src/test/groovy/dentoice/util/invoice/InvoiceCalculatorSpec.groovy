package dentoice.util.invoice

import com.mariakamachine.dentoice.data.entity.CostWrapperEntity
import com.mariakamachine.dentoice.data.entity.InvoiceEntity
import com.mariakamachine.dentoice.data.jsonb.EffortJsonb
import com.mariakamachine.dentoice.data.jsonb.MaterialJsonb
import com.mariakamachine.dentoice.util.invoice.InvoiceSum
import com.mariakamachine.dentoice.util.invoice.MonthlyInvoiceSum
import spock.lang.Specification
import spock.lang.Unroll

import static com.mariakamachine.dentoice.util.invoice.InvoiceCalculator.calculateInvoice
import static com.mariakamachine.dentoice.util.invoice.InvoiceCalculator.calculateMonthlyInvoiceSum

class InvoiceCalculatorSpec extends Specification {

    @Unroll
    def "test correct invoice sum calculation"() {
        given:
        def efforts = effortsData.stream()
                .map { data -> [position: "0", name: "dummy", quantity: data[0], pricePerUnit: data[1]] as EffortJsonb }
                .toArray()

        def materials = materialsData.stream()
                .map { data -> [position: "0", name: "dummy", quantity: data[0], pricePerUnit: data[1], isMetal: data[2]] as MaterialJsonb }
                .toArray()

        def invoice = new InvoiceEntity()
        invoice.setCosts([efforts: efforts, materials: materials] as CostWrapperEntity)
        invoice.setMwst 7

        expect:
        expectedSum == calculateInvoice(invoice)

        where:
        effortsData                                        | materialsData                                                                    | expectedSum
        [[1.0, 1.0]]                                       | [[1.0, 1.0, true]]                                                               | [efforts: 1.00, materials: 1.00, metal: 1.00, netto: 2.00, mwst: 0.14, brutto: 2.14] as InvoiceSum
        [[1.4, 14.3], [2.5, 1]]                            | [[1.0, 2.2, true], [2.5, 17.3, true], [45.3, 7.2123, false], [98.1, 4.3, false]] | [efforts: 22.52, materials: 794.00, metal: 45.45, netto: 816.52, mwst: 57.16, brutto: 873.68] as InvoiceSum
        [[56, 5]]                                          | [[1.0, 2.2, false], [2.5, 17.3, false], [45.3, 7.2123, false]]                   | [efforts: 280.00, materials: 372.17, metal: 0.00, netto: 652.17, mwst: 45.65, brutto: 697.82] as InvoiceSum
        [[1.2, 3.4], [5.6, 7.812], [12, 1], [128.0, 12.5]] | [[1.0, 2.2, true], [2.5, 17.3, true], [45.3, 7.2123, true]]                      | [efforts: 1659.83, materials: 372.17, metal: 372.17, netto: 2031.99, mwst: 142.24, brutto: 2174.23] as InvoiceSum
    }

    @Unroll
    def "test monthly invoice sum calculation"() {
        given:
        def efforts = effortsData.stream()
                .map { data -> [position: "0", name: "dummy", quantity: data[0], pricePerUnit: data[1]] as EffortJsonb }
                .toArray()

        def materials = materialsData.stream()
                .map { data -> [position: "0", name: "dummy", quantity: data[0], pricePerUnit: data[1], isMetal: data[2]] as MaterialJsonb }
                .toArray()

        def invoice = new InvoiceEntity()
        invoice.setCosts([efforts: efforts, materials: materials] as CostWrapperEntity)
        invoice.setMwst 7

        def invoices = []
        for (int i = 0; i < runs; i++) {
            invoices << invoice
        }

        expect:
        expectedSum == calculateMonthlyInvoiceSum(invoices, 3)

        where:
        effortsData                                        | materialsData                                                                    | expectedSum                                                                                | runs
        [[1.0, 1.0]]                                       | [[1.0, 1.0, true]]                                                               | [subtotal: 4.28, efforts: 2.14, skonto: 0.06, total: 4.22] as MonthlyInvoiceSum            | 2
        [[1.4, 14.3], [2.5, 1]]                            | [[1.0, 2.2, true], [2.5, 17.3, true], [45.3, 7.2123, false], [98.1, 4.3, false]] | [subtotal: 873.68, efforts: 24.10, skonto: 0.72, total: 872.96] as MonthlyInvoiceSum       | 1
        [[56, 5]]                                          | [[1.0, 2.2, false], [2.5, 17.3, false], [45.3, 7.2123, false]]                   | [subtotal: 2791.28, efforts: 1198.40, skonto: 35.95, total: 2755.33] as MonthlyInvoiceSum  | 4
        [[1.2, 3.4], [5.6, 7.812], [12, 1], [128.0, 12.5]] | [[1.0, 2.2, true], [2.5, 17.3, true], [45.3, 7.2123, true]]                      | [subtotal: 6522.69, efforts: 5328.06, skonto: 159.84, total: 6362.85] as MonthlyInvoiceSum | 3
    }

}
