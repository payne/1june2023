package kunesti.api.security;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class RegistrationRequest {

    @NotNull
    @Size(max = 255)
    private String username;

    @Size(max = 255)
    private String email;

    @NotNull
    @Size(max = 255)
    private String password;

}
