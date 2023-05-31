package kunesti.api.notification;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import kunesti.api.config.BaseIT;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;


public class NotificationResourceTest extends BaseIT {

    @Test
    @Sql("/data/notificationData.sql")
    void getAllNotifications_success() throws Exception {
        mockMvc.perform(get("/api/notifications")
                        .header(HttpHeaders.AUTHORIZATION, bearerToken())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").value(2))
                .andExpect(jsonPath("$.content[0].id").value(((long)1100)));
    }

    @Test
    @Sql("/data/notificationData.sql")
    void getAllNotifications_filtered() throws Exception {
        mockMvc.perform(get("/api/notifications?filter=1101")
                        .header(HttpHeaders.AUTHORIZATION, bearerToken())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").value(1))
                .andExpect(jsonPath("$.content[0].id").value(((long)1101)));
    }

    @Test
    @Sql("/data/notificationData.sql")
    void getNotification_success() throws Exception {
        mockMvc.perform(get("/api/notifications/1100")
                        .header(HttpHeaders.AUTHORIZATION, bearerToken())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.headline").value("Duis autem vel."));
    }

    @Test
    void getNotification_notFound() throws Exception {
        mockMvc.perform(get("/api/notifications/1766")
                        .header(HttpHeaders.AUTHORIZATION, bearerToken())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.exception").value("NotFoundException"));
    }

    @Test
    void createNotification_success() throws Exception {
        mockMvc.perform(post("/api/notifications")
                        .header(HttpHeaders.AUTHORIZATION, bearerToken())
                        .accept(MediaType.APPLICATION_JSON)
                        .content(readResource("/requests/notificationDTORequest.json"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
        assertEquals(1, notificationRepository.count());
    }

    @Test
    @Sql("/data/notificationData.sql")
    void updateNotification_success() throws Exception {
        mockMvc.perform(put("/api/notifications/1100")
                        .header(HttpHeaders.AUTHORIZATION, bearerToken())
                        .accept(MediaType.APPLICATION_JSON)
                        .content(readResource("/requests/notificationDTORequest.json"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        assertEquals("Nam liber tempor.", notificationRepository.findById(((long)1100)).get().getHeadline());
        assertEquals(2, notificationRepository.count());
    }

    @Test
    @Sql("/data/notificationData.sql")
    void deleteNotification_success() throws Exception {
        mockMvc.perform(delete("/api/notifications/1100")
                        .header(HttpHeaders.AUTHORIZATION, bearerToken())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
        assertEquals(1, notificationRepository.count());
    }

}
