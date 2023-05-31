package kunesti.api.group;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class GroupDTO {

    private Long id;

    @Size(max = 255)
    private String name;

}
