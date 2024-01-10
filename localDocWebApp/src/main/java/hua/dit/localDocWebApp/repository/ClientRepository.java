package hua.dit.localDocWebApp.repository;

import hua.dit.localDocWebApp.entity.Client;
import hua.dit.localDocWebApp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends JpaRepository<Client, Integer> {

    Iterable<Client> findByUser(User user);
}
