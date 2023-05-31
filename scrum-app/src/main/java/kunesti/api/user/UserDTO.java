package kunesti.api.user;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class UserDTO {

    private Long id;

    @NotNull
    @Size(max = 255)
    private String username;

    @Size(max = 255)
    private String email;

    @Size(max = 255)
    private String hash;

    private List<Long> groups;

    private Long invite;

    private Long notification;

}
