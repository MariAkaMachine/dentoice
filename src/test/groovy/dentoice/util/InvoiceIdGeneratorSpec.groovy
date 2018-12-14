package dentoice.util

import com.mariakamachine.dentoice.util.InvoiceIdGenerator
import org.hibernate.engine.spi.SessionImplementor
import spock.lang.Specification

import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet

class InvoiceIdGeneratorSpec extends Specification {

    def generator = new InvoiceIdGenerator()

    def session = Mock(SessionImplementor)
    def resultSet = Mock(ResultSet)

    void setup() {
        def connection = Mock(Connection)
        def statement = Mock(PreparedStatement)

        session.connection() >> connection
        connection.prepareStatement(_) >> statement
        statement.executeQuery() >> resultSet
    }

    def "test id generation for first invoice in a month"() {
        when:
        def id = generator.generate(session, _)

        then:
        (id as String).endsWith '0001'
    }

    def "test id generation"() {
        given:
        resultSet.next() >> true >> true >> true >> true >> false
        resultSet.getLong('id') >> 18030004 >> 18030003 >> 18030005 >> 18030002

        when:
        def id = generator.generate(session, _)

        then:
        (id as String).endsWith "0006"
    }

    def "test invalid ids"() {
        given:
        resultSet.next() >> true >> false
        resultSet.getLong('id') >> 'invalid'

        when:
        generator.generate(session, _)

        then:
        thrown(RuntimeException)
    }

}
