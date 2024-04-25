package solutionclear.userservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import solutionclear.userservice.model.User;

import java.time.LocalDate;
import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findAllByBirthDateBetween(LocalDate startDate, LocalDate endDate);
}
