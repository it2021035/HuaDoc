package hua.dit.localDocWebApp.repository;

import hua.dit.localDocWebApp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {


    User findByUsername(String username);
    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);

    User findByEmail(String email);

}