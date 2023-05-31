package kunesti.api.invite;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class InviteDTO {

    private Long id;

    @NotNull
    @Size(max = 255)
    private String inviteToken;

}
