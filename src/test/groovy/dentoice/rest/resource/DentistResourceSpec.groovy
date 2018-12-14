package dentoice.rest.resource

import com.google.common.collect.Lists
import com.mariakamachine.dentoice.Application
import com.mariakamachine.dentoice.data.entity.DentistEntity
import com.mariakamachine.dentoice.data.repository.DentistRepository
import org.json.JSONArray
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import spock.lang.Specification
import spock.lang.Title
import spock.lang.Unroll

@SpringBootTest(classes = [Application])
@AutoConfigureMockMvc
@ActiveProfiles("unit")
@Title("test dentist management api")
class DentistResourceSpec extends Specification {

    @Autowired
    private MockMvc mockMvc

    @Autowired
    private DentistRepository repository

    void setup() {
        repository.flush()
        repository.deleteAll()
    }

    @Unroll
    def "create dentists with several payload variations"() {
        expect:
        mockMvc.perform(MockMvcRequestBuilders.post("/v1/dentists/create")
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content(payload))
                .andExpect(MockMvcResultMatchers.status().is(response_status))

        where:
        payload                                                                                                                                                                                                    || response_status
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
        mockMvc.perform(MockMvcRequestBuilders.patch("/v1/dentists/${id}")
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
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
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(
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
        mockMvc.perform(MockMvcRequestBuilders.delete("/v1/dentists/${id}"))
                .andExpect(MockMvcResultMatchers.status().isOk())

        then:
        repository.findAll().size() == 0
        !repository.findById(id).isPresent()
    }

    @Unroll
    def "get a dentist from database by id"() {
        given:
        def id = fillDbWithTestEntriesAndReturnIds(1)[0]

        expect:
        mockMvc.perform(MockMvcRequestBuilders.get("/v1/dentists/${id}"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(
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
        def expectedJson = groovy.json.JsonOutput.toJson Lists.asList(expectedData)

        expect:
        mockMvc.perform(MockMvcRequestBuilders.get("/v1/dentists")
                .param("lastName", data.lastName))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(expectedJson))
    }

    @Unroll
    def "get a dentist from database by firstName and lastName"() {
        given:
        def data = [firstName: "Spongebob", lastName: "Sqarepants", street: "124 Conch Street", zip: "Pacific Ocean", city: "Bikini Bottom"]
        def expectedData = [:] << ["id": repository.save(data as DentistEntity).id] << data
        def expectedJson = groovy.json.JsonOutput.toJson Lists.asList(expectedData)

        expect:
        mockMvc.perform(MockMvcRequestBuilders.get("/v1/dentists")
                .param("firstName", data.firstName)
                .param("lastName", data.lastName))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(expectedJson))
    }

    @Unroll
    def "get all dentists from database as list"() {
        given:
        def entries = 7
        fillDbWithTestEntriesAndReturnIds(entries)

        expect:
        mockMvc.perform(MockMvcRequestBuilders.get("/v1/dentists"))
                .andExpect(MockMvcResultMatchers.status().isOk())
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
