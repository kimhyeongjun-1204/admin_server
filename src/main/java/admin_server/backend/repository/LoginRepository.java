package admin_server.backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import admin_server.backend.entity.Admin;

@Repository
public interface LoginRepository extends JpaRepository<Admin, Integer> {
    Optional<Admin> findByUsername(String username);
    Optional<Admin> findTopByOrderByAdminIdDesc();
} 