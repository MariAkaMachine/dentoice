package dentoice.util.invoice

import com.mariakamachine.dentoice.data.entity.CostWrapperEntity
import com.mariakamachine.dentoice.data.jsonb.EffortJsonb
import com.mariakamachine.dentoice.data.jsonb.MaterialJsonb
import com.mariakamachine.dentoice.util.invoice.InvoiceSum
import com.mariakamachine.dentoice.util.invoice.InvoiceSumCalculator
import spock.lang.Specification

class InvoiceSumCalculatorSpec extends Specification {

    def "test correct invoice sum calculation"() {
        given:
        def efforts = effortsData.stream()
                .map { data -> [position: "0", description: "dummy", quantity: data[0], pricePerUnit: data[1]] as EffortJsonb }
                .toArray()

        def materials = materialsData.stream()
                .map { data -> [position: "0", description: "dummy", quantity: data[0], pricePerUnit: data[1], isMetal: data[2]] as MaterialJsonb }
                .toArray()

        def costs = [efforts: efforts, materials: materials] as CostWrapperEntity

        expect:
        expected == new InvoiceSumCalculator().calculate(costs)

        where:
        effortsData                                        | materialsData                                                                    | expected
        [[1.0, 1.0]]                                       | [[1.0, 1.0, true]]                                                               | [efforts: 1.00, materials: 1.00, metal: 1.00, total: 2.00] as InvoiceSum
        [[1.4, 14.3], [2.5, 1]]                            | [[1.0, 2.2, true], [2.5, 17.3, true], [45.3, 7.2123, false], [98.1, 4.3, false]] | [efforts: 22.52, materials: 794.00, metal: 45.45, total: 816.52] as InvoiceSum
        [[56, 5]]                                          | [[1.0, 2.2, false], [2.5, 17.3, false], [45.3, 7.2123, false]]                   | [efforts: 280.00, materials: 372.17, metal: 0.00, total: 652.17] as InvoiceSum
        [[1.2, 3.4], [5.6, 7.812], [12, 1], [128.0, 12.5]] | [[1.0, 2.2, true], [2.5, 17.3, true], [45.3, 7.2123, true]]                      | [efforts: 1659.83, materials: 372.17, metal: 372.17, total: 2031.99] as InvoiceSum
    }

}
