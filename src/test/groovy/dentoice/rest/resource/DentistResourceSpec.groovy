package dentoice.rest.resource

import com.mariakamachine.dentoice.Application
import com.mariakamachine.dentoice.data.entity.DentistEntity
import com.mariakamachine.dentoice.data.repository.DentistRepository
import com.mariakamachine.dentoice.service.InvoiceService
import org.json.JSONArray
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import spock.lang.Specification
import spock.lang.Title
import spock.lang.Unroll

import static groovy.json.JsonOutput.toJson
import static org.codehaus.groovy.runtime.InvokerHelper.asList
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest(classes = [Application])
@AutoConfigureMockMvc
@ActiveProfiles("unit")
@Title("test dentist management api")
class DentistResourceSpec extends Specification {

    @Autowired
    private MockMvc mockMvc

    @Autowired
    private DentistRepository repository

    @Autowired
    private InvoiceService invoiceService

    void setup() {
        repository.flush()
        repository.deleteAll()
    }

    @Unroll
    def "create dentists with several payload variations"() {
        expect:
        mockMvc.perform(post("/dentists/create")
                .contentType(APPLICATION_JSON_UTF8_VALUE)
                .content(payload))
                .andExpect(status().is(responseStatus))

        where:
        payload                                                                                                                                                                                                    || responseStatus
        '{ "lastName": "Dentist", "street": "Panorama Boulevard", "zip": "CV0815", "city": "Sunshine State" }'                                                                                                     || 201
        '{ "id": "1", "lastName": "Dentist", "street": "Panorama Boulevard", "zip": "CV0815", "city": "Sunshine State" }'                                                                                          || 201
        '{ "title" :"Prof. Dr.", "firstName":"John", "lastName": "Snow", "street": "House Stark Rd 1", "zip": "WF 123", "city": "Winterfell", "phone": "123456789", "fax": "123456789", "email": "snow@got.com" }' || 201
        '{ "street": "Panorama Boulevard", "zip": "CV0815", "city": "Sunshine State" }'                                                                                                                            || 400
    }

    @Unroll
    def "update a dentist with different attributes"() {
        given:
        def id = fillDbWithTestEntriesAndReturnIds(1)[0]

        expect:
        mockMvc.perform(patch("/dentists/${id}")
                .contentType(APPLICATION_JSON_UTF8_VALUE)
                .content(
                """
                  {
                    "firstName": "Robert",
                    "lastName": "Stark",
                    "street": "House Stark Rd 1",
                    "zip": "CV0815",
                    "city": "Winterfell",
                    "email": "robbie@got.com"
                  }
                """))
                .andExpect(status().isOk())
                .andExpect(content().json(
                """
                  {
                    "id": ${id},
                    "firstName": "Robert",
                    "lastName": "Stark",
                    "street": "House Stark Rd 1",
                    "zip": "CV0815",
                    "city": "Winterfell",
                    "email": "robbie@got.com"
                  }
                """))
    }

    @Unroll
    def "delete a dentist from the database"() {
        given:
        def id = fillDbWithTestEntriesAndReturnIds(1)[0]

        when:
        mockMvc.perform(delete("/dentists/${id}"))
                .andExpect(status().isNoContent())

        then:
        repository.findAll().size() == 0
        !repository.findById(id).isPresent()
    }

    @Unroll
    def "get a dentist from database by id"() {
        given:
        def id = fillDbWithTestEntriesAndReturnIds(1)[0]

        expect:
        mockMvc.perform(get("/dentists/${id}"))
                .andExpect(status().isOk())
                .andExpect(content().json(
                """
                  {
                    "id": ${id},
                    "lastName": "dummy",
                    "street": "0 avenue",
                    "zip": "number 0",
                    "city": "wild wild test"
                  }
                """))
    }

    @Unroll
    def "get a dentist from database by lastName"() {
        given:
        def data = [lastName: "Sqarepants", street: "124 Conch Street", zip: "Pacific Ocean", city: "Bikini Bottom"]
        def expectedData = [:] << ["id": repository.save(data as DentistEntity).id] << data
        def expectedJson = toJson asList(expectedData)

        expect:
        mockMvc.perform(get("/dentists")
                .param("lastName", data.lastName))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedJson))
    }

    @Unroll
    def "get a dentist from database by firstName and lastName"() {
        given:
        def data = [firstName: "Spongebob", lastName: "Sqarepants", street: "124 Conch Street", zip: "Pacific Ocean", city: "Bikini Bottom"]
        def expectedData = [:] << ["id": repository.save(data as DentistEntity).id] << data
        def expectedJson = toJson asList(expectedData)

        expect:
        mockMvc.perform(get("/dentists")
                .param("firstName", data.firstName)
                .param("lastName", data.lastName))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedJson))
    }

    @Unroll
    def "get all dentists from database as list"() {
        given:
        def entries = 7
        fillDbWithTestEntriesAndReturnIds(entries)

        expect:
        mockMvc.perform(get("/dentists"))
                .andExpect(status().isOk())
                .andExpect({ assert new JSONArray(it.getResponse().getContentAsString()).length() == entries })
    }

    long[] fillDbWithTestEntriesAndReturnIds(int number) {
        def ids = []
        number.times {
            ids << repository.save([lastName: "dummy", street: "${it} avenue", zip: "number ${it}", city: "wild wild test"] as DentistEntity).id
        }
        return ids
    }

}
