package hua.dit.localDocWebApp.repository;

import hua.dit.localDocWebApp.entity.Client;
import hua.dit.localDocWebApp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClientRepository extends JpaRepository<Client, Integer> {

    List<Client> findByUser(User user);

    List<Client> findByEmail(String email);
}
