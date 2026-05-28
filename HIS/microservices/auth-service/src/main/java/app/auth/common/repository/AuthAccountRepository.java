package app.auth.common.repository;

import app.auth.common.entity.AuthAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthAccountRepository extends JpaRepository<AuthAccount, String> {

    Optional<AuthAccount> findByUsernameIgnoreCase(String username);
}
