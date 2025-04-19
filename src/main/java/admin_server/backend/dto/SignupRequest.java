package admin_server.backend.dto;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Getter
@Setter
public class SignupRequest {
    private String username;
    private String password;
    private String name;
    private LocalDate birth;
} 