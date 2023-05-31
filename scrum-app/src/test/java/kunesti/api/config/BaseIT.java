package kunesti.api.config;

import java.nio.charset.Charset;
import kunesti.api.ApiApplication;
import kunesti.api.group.GroupRepository;
import kunesti.api.invite.InviteRepository;
import kunesti.api.notification.NotificationRepository;
import kunesti.api.user.UserRepository;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlMergeMode;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.StreamUtils;


/**
 * Abstract base class to be extended by every IT test. Starts the Spring Boot context, with all data
 * wiped out before each test.
 */
@SpringBootTest(
        classes = ApiApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@AutoConfigureMockMvc
@ActiveProfiles("it")
@Sql({"/data/clearAll.sql", "/data/userData.sql"})
@SqlMergeMode(SqlMergeMode.MergeMode.MERGE)
public abstract class BaseIT {

    @Autowired
    public MockMvc mockMvc;

    @Autowired
    public GroupRepository groupRepository;

    @Autowired
    public NotificationRepository notificationRepository;

    @Autowired
    public UserRepository userRepository;

    @Autowired
    public InviteRepository inviteRepository;

    @SneakyThrows
    public String readResource(final String resourceName) {
        return StreamUtils.copyToString(getClass().getResourceAsStream(resourceName), Charset.forName("UTF-8"));
    }

    public String bearerToken() {
        // user bootify, expires 2040-01-01
        return "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9." +
                "eyJzdWIiOiJib290aWZ5IiwiaXNzIjoiYm9vdGlmeSIsImV4cCI6MjIwODk4ODgwMCwiaWF0IjoxNjg1NDg0NjQzfQ." +
                "nTEqMwBg7xwpoedP_BdNFASQVeRtQEJ6dFW715T3HtItpomtaLIc8uXYsPublux029FQ9pxesu6VnR-JC3-U7w";
    }

}
