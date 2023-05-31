package kunesti.api.group;

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


public class GroupResourceTest extends BaseIT {

    @Test
    @Sql("/data/groupData.sql")
    void getAllGroups_success() throws Exception {
        mockMvc.perform(get("/api/groups")
                        .header(HttpHeaders.AUTHORIZATION, bearerToken())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").value(2))
                .andExpect(jsonPath("$.content[0].id").value(((long)1000)));
    }

    @Test
    @Sql("/data/groupData.sql")
    void getAllGroups_filtered() throws Exception {
        mockMvc.perform(get("/api/groups?filter=1001")
                        .header(HttpHeaders.AUTHORIZATION, bearerToken())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").value(1))
                .andExpect(jsonPath("$.content[0].id").value(((long)1001)));
    }

    @Test
    @Sql("/data/groupData.sql")
    void getGroup_success() throws Exception {
        mockMvc.perform(get("/api/groups/1000")
                        .header(HttpHeaders.AUTHORIZATION, bearerToken())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Duis autem vel."));
    }

    @Test
    void getGroup_notFound() throws Exception {
        mockMvc.perform(get("/api/groups/1666")
                        .header(HttpHeaders.AUTHORIZATION, bearerToken())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.exception").value("NotFoundException"));
    }

    @Test
    void createGroup_success() throws Exception {
        mockMvc.perform(post("/api/groups")
                        .header(HttpHeaders.AUTHORIZATION, bearerToken())
                        .accept(MediaType.APPLICATION_JSON)
                        .content(readResource("/requests/groupDTORequest.json"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
        assertEquals(1, groupRepository.count());
    }

    @Test
    @Sql("/data/groupData.sql")
    void updateGroup_success() throws Exception {
        mockMvc.perform(put("/api/groups/1000")
                        .header(HttpHeaders.AUTHORIZATION, bearerToken())
                        .accept(MediaType.APPLICATION_JSON)
                        .content(readResource("/requests/groupDTORequest.json"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        assertEquals("Nam liber tempor.", groupRepository.findById(((long)1000)).get().getName());
        assertEquals(2, groupRepository.count());
    }

    @Test
    @Sql("/data/groupData.sql")
    void deleteGroup_success() throws Exception {
        mockMvc.perform(delete("/api/groups/1000")
                        .header(HttpHeaders.AUTHORIZATION, bearerToken())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
        assertEquals(1, groupRepository.count());
    }

}
