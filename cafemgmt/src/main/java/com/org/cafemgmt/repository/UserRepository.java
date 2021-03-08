package com.org.cafemgmt.repository;

import com.org.cafemgmt.model.CafeUsers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<CafeUsers, Long> {
    @Query("SELECT t FROM CafeUsers t WHERE t.emailAddress = ?1")
    public CafeUsers findByEmailAddress(String emailaddress);
}
