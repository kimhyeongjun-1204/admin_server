package admin_server.backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@Table(name = "Admin")
public class Admin {
    @Id
    @Column(name = "admin_id")
    private Integer adminId;

    @Column(name = "username", length = 20, nullable = false)
    private String username;

    @Column(name = "hashed_password", length = 256, nullable = false)
    private String hashedPassword;

    @Column(name = "signup_date", nullable = false)
    private LocalDate signupDate;

    @Column(name = "name", length = 30, nullable = false)
    private String name;

    @Column(name = "birth", nullable = false)
    private LocalDate birth;
} 