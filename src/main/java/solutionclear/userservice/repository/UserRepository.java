package solutionclear.userservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import solutionclear.userservice.model.User;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findAllByBirthDateBetween(LocalDate startDate, LocalDate endDate);

    Optional<User> findByEmail(String email);
}
