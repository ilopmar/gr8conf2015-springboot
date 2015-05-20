package eu.gr8conf

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.query.Param
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@SpringBootApplication
class DemoApplication {

    static void main(String[] args) {
        SpringApplication.run DemoApplication, args
    }
}

@Entity
class User {

    @Id
    @GeneratedValue
    Long id

    String name
    String lastName
}

interface UserRepository extends JpaRepository<User, Long> {
    Collection<User> findByName(@Param("name") String name)
}

@RestController
@RequestMapping("/users")
class UserRestController {

    @Autowired
    UserRepository userRepository

    @RequestMapping(method = RequestMethod.GET)
    Collection<User> listAllUsers() {
        userRepository.findAll()
    }

    @RequestMapping(value = "{userId}", method = RequestMethod.GET)
    User getUser(@PathVariable(value = "userId") Long id) {
        userRepository.findOne(id)
    }

    @RequestMapping(value = "/find", method = RequestMethod.GET)
    Collection<User> findUserByName(@RequestParam String name) {
        userRepository.findByName(name)
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    User saveUser(@RequestBody User user) {
        userRepository.save(user)
    }
}

@ConfigurationProperties(value = 'application')
class ApplicationProperties {
    String name
    String version
}

@RestController
@RequestMapping("/appinfo")
class InfoController {

    @Autowired
    ApplicationProperties applicationProperties

    @Value('${application.message}')
    String message

    @Bean
    ApplicationProperties applicationProperties() {
        new ApplicationProperties()
    }

    @RequestMapping(method = RequestMethod.GET)
    Map getInfo() {
        [
            name   : applicationProperties.name,
            version: applicationProperties.version,
            message: message,
        ]
    }
}