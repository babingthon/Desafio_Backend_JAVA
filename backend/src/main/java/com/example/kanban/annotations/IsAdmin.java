package com.example.kanban.annotations;

import org.springframework.security.access.prepost.PreAuthorize;
import java.lang.annotation.*;

@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
@PreAuthorize("hasAuthority(T(com.example.kanban.constants.RoleConstants).ROLE_ADMIN)")
public @interface IsAdmin {
}
