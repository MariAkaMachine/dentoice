package dentoice.util

import com.mariakamachine.dentoice.util.validation.NumericValidator
import spock.lang.Specification

class NumericValidatorSpec extends Specification {

    def "test numeric value"() {
        expect:
        new NumericValidator().isValid("84375", null)
    }

    def "test non-numeric value"() {
        expect:
        !new NumericValidator().isValid("a84375", null)
    }

}
