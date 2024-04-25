package solutionclear.userservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import solutionclear.userservice.dto.UpdateUserDto;
import solutionclear.userservice.dto.UserCreateRequestDto;
import solutionclear.userservice.model.User;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.testcontainers.shaded.org.apache.commons.lang3.builder.EqualsBuilder.reflectionEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerTest {
    private static final int minAge = 18;
    private static final String VALID_FIRST_NAME = "John";
    private static final String VALID_LAST_NAME = "Doe";
    private static final LocalDate VALID_BIRTH_DATE = LocalDate.of(2003, 9, 13);
    private static final LocalDate INVALID_BIRTH_DATE = LocalDate.now().minusYears(minAge - 1);
    public static final String VALID_EMAIL = "john@doe.com";
    public static final String VALID_ADDRESS = "Kyiv 123";
    public static final String VALID_PHONE = "123456789";
    public static final Long VALID_ID = 1L;
    public static final Long INVALID_ID = -1L;

    private static MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    static void beforeAll(
            @Autowired DataSource dataSource,
            @Autowired WebApplicationContext applicationContext
    ) throws SQLException {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(applicationContext)
                .build();
        teardown(dataSource);
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(
                    connection, new ClassPathResource("db/users/create-two-users.sql")
            );
        }
    }

    @AfterAll
    static void afterAll(@Autowired DataSource dataSource) throws SQLException {
        teardown(dataSource);
    }

    static void teardown(DataSource dataSource) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(
                    connection, new ClassPathResource("db/users/delete-all-from-users.sql")
            );
        }
    }

    @Sql(
            scripts = "classpath:db/users/delete-all-from-users.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    @DisplayName("Verify createUser() method work")
    public void createUser_ValidRequestDto_ReturnCarResponseDto() throws Exception {
        UserCreateRequestDto validRequestDto = createValidRequestDto();
        User expected = createUser();
        String jsonRequest = objectMapper.writeValueAsString(validRequestDto);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/users")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated())
                .andReturn();

        User actual = objectMapper
                .readValue(result.getResponse().getContentAsString(), User.class);

        assertNotNull(actual);
        assertNotNull(actual.getId());
        reflectionEquals(expected, actual, "id");
    }

    private UserCreateRequestDto createValidRequestDto() {
        return new UserCreateRequestDto(
                VALID_FIRST_NAME,
                VALID_LAST_NAME,
                VALID_BIRTH_DATE,
                VALID_EMAIL,
                VALID_ADDRESS,
                VALID_PHONE
        );
    }


    private User createUser() {
        User user = new User();
        user.setFirstName(VALID_FIRST_NAME);
        user.setLastName(VALID_LAST_NAME);
        user.setBirthDate(VALID_BIRTH_DATE);
        user.setEmail(VALID_EMAIL);
        user.setAddress(VALID_ADDRESS);
        user.setPhoneNumber(VALID_PHONE);
        user.setId(VALID_ID);
        return user;
    }

    private UpdateUserDto updateUserDto() {
        UpdateUserDto updateUserDto = new UpdateUserDto();
        updateUserDto.setFirstName(VALID_FIRST_NAME);
        updateUserDto.setLastName(VALID_LAST_NAME);
        updateUserDto.setBirthDate(VALID_BIRTH_DATE);
        updateUserDto.setEmail(VALID_EMAIL);
        updateUserDto.setAddress(VALID_ADDRESS);
        updateUserDto.setPhoneNumber(VALID_PHONE);
        return updateUserDto;
    }
}
