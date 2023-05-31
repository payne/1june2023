package kunesti.api.invite;

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


public class InviteResourceTest extends BaseIT {

    @Test
    @Sql("/data/inviteData.sql")
    void getAllInvites_success() throws Exception {
        mockMvc.perform(get("/api/invites")
                        .header(HttpHeaders.AUTHORIZATION, bearerToken())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").value(2))
                .andExpect(jsonPath("$.content[0].id").value(((long)1300)));
    }

    @Test
    @Sql("/data/inviteData.sql")
    void getAllInvites_filtered() throws Exception {
        mockMvc.perform(get("/api/invites?filter=1301")
                        .header(HttpHeaders.AUTHORIZATION, bearerToken())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").value(1))
                .andExpect(jsonPath("$.content[0].id").value(((long)1301)));
    }

    @Test
    @Sql("/data/inviteData.sql")
    void getInvite_success() throws Exception {
        mockMvc.perform(get("/api/invites/1300")
                        .header(HttpHeaders.AUTHORIZATION, bearerToken())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.inviteToken").value("Ut wisi enim."));
    }

    @Test
    void getInvite_notFound() throws Exception {
        mockMvc.perform(get("/api/invites/1966")
                        .header(HttpHeaders.AUTHORIZATION, bearerToken())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.exception").value("NotFoundException"));
    }

    @Test
    void createInvite_success() throws Exception {
        mockMvc.perform(post("/api/invites")
                        .header(HttpHeaders.AUTHORIZATION, bearerToken())
                        .accept(MediaType.APPLICATION_JSON)
                        .content(readResource("/requests/inviteDTORequest.json"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
        assertEquals(1, inviteRepository.count());
    }

    @Test
    void createInvite_missingField() throws Exception {
        mockMvc.perform(post("/api/invites")
                        .header(HttpHeaders.AUTHORIZATION, bearerToken())
                        .accept(MediaType.APPLICATION_JSON)
                        .content(readResource("/requests/inviteDTORequest_missingField.json"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.exception").value("MethodArgumentNotValidException"))
                .andExpect(jsonPath("$.fieldErrors[0].field").value("inviteToken"));
    }

    @Test
    @Sql("/data/inviteData.sql")
    void updateInvite_success() throws Exception {
        mockMvc.perform(put("/api/invites/1300")
                        .header(HttpHeaders.AUTHORIZATION, bearerToken())
                        .accept(MediaType.APPLICATION_JSON)
                        .content(readResource("/requests/inviteDTORequest.json"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        assertEquals("Sed ut perspiciatis.", inviteRepository.findById(((long)1300)).get().getInviteToken());
        assertEquals(2, inviteRepository.count());
    }

    @Test
    @Sql("/data/inviteData.sql")
    void deleteInvite_success() throws Exception {
        mockMvc.perform(delete("/api/invites/1300")
                        .header(HttpHeaders.AUTHORIZATION, bearerToken())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
        assertEquals(1, inviteRepository.count());
    }

}
