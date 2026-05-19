package app.auth.register.repository;

import app.auth.common.entity.AuthAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RegisterAccountRepository extends JpaRepository<AuthAccount, String> {

    Optional<AuthAccount> findByUsernameIgnoreCase(String username);

    long countByUsernameIgnoreCase(String username);
}
