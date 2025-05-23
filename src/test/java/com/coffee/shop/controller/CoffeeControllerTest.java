package com.coffee.shop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.coffee.shop.dto.CoffeeRequest;
import com.coffee.shop.model.Coffee;
import com.coffee.shop.repository.CoffeeRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.Before;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.Duration;

@SpringBootTest
@AutoConfigureMockMvc
//@Testcontainers
@Transactional
public class CoffeeControllerTest {

    /*@Container
    public static final MySQLContainer<?> mysqlContainer = new MySQLContainer<>("mysql:8.0.33")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test")
            .withEnv("MYSQL_ROOT_PASSWORD", "root")
            .withEnv("MYSQL_DATABASE", "testdb")
            .withEnv("MYSQL_USER", "test")
            .withEnv("MYSQL_PASSWORD", "test")
            .withCommand("mysqld", "--character-set-server=utf8mb4", "--collation-server=utf8mb4_unicode_ci")
            .withStartupTimeout(Duration.ofMinutes(10))
            .withStartupAttempts(5)
            .withReuse(false);

    @DynamicPropertySource
    static void overrideProps(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mysqlContainer::getJdbcUrl);
        registry.add("spring.datasource.username", mysqlContainer::getUsername);
        registry.add("spring.datasource.password", mysqlContainer::getPassword);
        registry.add("spring.datasource.driver-class-name", () -> "com.mysql.cj.jdbc.Driver");
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop");
        registry.add("spring.jpa.database-platform", () -> "org.hibernate.dialect.MySQL8Dialect");
        registry.add("spring.jpa.show-sql", () -> "true");
    }*/

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CoffeeRepository coffeeRepository;
	
	@Before
    public void setUp(){
        this.mockMvc = MockMvcBuilders
            .standaloneSetup(CoffeeController.class)
            .build();
    }

    @Test
    void testCreateCoffee() throws Exception {
        CoffeeRequest request = new CoffeeRequest("Latte", "Espresso with steamed milk", 3.5);

        mockMvc.perform(post("/api/v1/coffees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("Latte"))
                .andExpect(jsonPath("$.description").value("Espresso with steamed milk"))
                .andExpect(jsonPath("$.price").value(3.5));
    }

    @Test
    void testGetAllCoffees() throws Exception {
        /*Coffee coffee = new Coffee();
        coffee.setName("Espresso");
        coffee.setDescription("Strong coffee");
        coffee.setPrice(2.0);
        coffeeRepository.save(coffee);*/

        mockMvc.perform(get("/api/v1/coffees"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Chip Frappuccino"));
    }

    @Test
    void testUpdateCoffee() throws Exception {
        Coffee saved = coffeeRepository.save(new Coffee(null, "Espresso", "Strong", 2.0));

        CoffeeRequest updated = new CoffeeRequest("Double Espresso", "Very strong", 2.5);

        mockMvc.perform(put("/api/v1/coffees/" + saved.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updated)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Double Espresso"))
                .andExpect(jsonPath("$.price").value(2.5));
    }

    @Test
    void testDeleteCoffee() throws Exception {
        Coffee saved = coffeeRepository.save(new Coffee(null, "Americano", "Diluted espresso", 2.5));

        mockMvc.perform(delete("/api/v1/coffees/" + saved.getId()))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/v1/coffees/" + saved.getId()))
                .andExpect(status().isNotFound());
    }

    @Test
    void testClearCache() throws Exception {
        mockMvc.perform(post("/api/v1/coffees/cache/clear"))
                .andExpect(status().isOk());
    }

    @Test
    void testClearPaginatedCache() throws Exception {
        mockMvc.perform(post("/api/v1/coffees/cache/clear/coffeesPaginated"))
                .andExpect(status().isOk());
    }

    @Test
    void testGetCoffeeById() throws Exception {
        // Guardamos un café en la base de datos
        Coffee saved = coffeeRepository.save(new Coffee(null, "Cappuccino", "With milk foam", 3.0));

        // Hacemos la solicitud para obtener el café por ID
        mockMvc.perform(get("/api/v1/coffees/" + saved.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(saved.getId()))
                .andExpect(jsonPath("$.name").value("Cappuccino"))
                .andExpect(jsonPath("$.description").value("With milk foam"))
                .andExpect(jsonPath("$.price").value(3.0));
    }

    @Test
    void testGetCoffeeById_NotFound() throws Exception {
        // Intentamos acceder a un ID que no existe
        mockMvc.perform(get("/api/v1/coffees/99999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCreateCoffee_InvalidName() throws Exception {
        CoffeeRequest invalidRequest = new CoffeeRequest(null, "Rich flavor", 2.0);

        mockMvc.perform(post("/api/v1/coffees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    /*@Test
    void testCreateCoffee_NegativePrice() throws Exception {
        CoffeeRequest invalidRequest = new CoffeeRequest("Mocha", "Chocolate and coffee", -1.0);

        mockMvc.perform(post("/api/v1/coffees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }*/
}
