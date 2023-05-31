package kunesti.api.model;

import lombok.experimental.FieldNameConstants;


@FieldNameConstants(onlyExplicitlyIncluded = true)
public enum Roles {

    @FieldNameConstants.Include
    ADMIN,
    @FieldNameConstants.Include
    USER,
    @FieldNameConstants.Include
    GROUP_MANAGER

}
