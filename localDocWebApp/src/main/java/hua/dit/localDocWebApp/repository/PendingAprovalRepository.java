package hua.dit.localDocWebApp.repository;

import hua.dit.localDocWebApp.entity.PendingAproval;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PendingAprovalRepository  extends JpaRepository<PendingAproval, Integer> {
}
