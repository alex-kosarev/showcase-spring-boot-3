package pro.akosarev.showcase;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Sql("/sql/tasks_rest_controller/test_data.sql")
@Transactional
@SpringBootTest
@AutoConfigureMockMvc(printOnlyOnFailure = false)
class TasksRestControllerIT {

    @Autowired
    MockMvc mockMvc;

    @Test
    void handleGetAllTasks_ReturnsValidResponseEntity() throws Exception {
        // given
        var requestBuilder = get("/api/tasks")
                .with(httpBasic("user1", "password1"));

        // when
        this.mockMvc.perform(requestBuilder)
                // then
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        content().json("""
                                [
                                    {
                                        "id": "71117396-8694-11ed-9ef6-77042ee83937",
                                        "details": "Первая задача",
                                        "completed": false
                                    },
                                    {
                                        "id": "7172d834-8694-11ed-8669-d7b17d45fba8",
                                        "details": "Вторая задача",
                                        "completed": true
                                    }
                                ]
                                """)
                );

    }

    @Test
    void handleCreateNewTask_PayloadIsValid_ReturnsValidResponseEntity() throws Exception {
        // given
        var requestBuilder = post("/api/tasks")
                .with(httpBasic("user2", "password2"))
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "details": "Третья задача"
                        }
                        """);

        // when
        this.mockMvc.perform(requestBuilder)
                // then
                .andExpectAll(
                        status().isCreated(),
                        header().exists(HttpHeaders.LOCATION),
                        content().contentType(MediaType.APPLICATION_JSON),
                        content().json("""
                                {
                                    "details": "Третья задача",
                                    "completed": false
                                }
                                """),
                        jsonPath("$.id").exists()
                );
    }

    @Test
    void handleCreateNewTask_PayloadIsInvalid_ReturnsValidResponseEntity() throws Exception {
        // given
        var requestBuilder = post("/api/tasks")
                .with(httpBasic("user1", "password1"))
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.ACCEPT_LANGUAGE, "en")
                .content("""
                        {
                            "details": null
                        }
                        """);

        // when
        this.mockMvc.perform(requestBuilder)
                // then
                .andExpectAll(
                        status().isBadRequest(),
                        header().doesNotExist(HttpHeaders.LOCATION),
                        content().contentType(MediaType.APPLICATION_JSON),
                        content().json("""
                                {
                                    "errors": ["Task details must be set"]
                                }
                                """, true)
                );
    }
}
