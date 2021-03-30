package com.org.cafemgmt.repository;

import com.org.cafemgmt.model.CafeUsers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface UserRepository extends JpaRepository<CafeUsers, Long> {
    @Query("SELECT t FROM CafeUsers t WHERE t.emailAddress = ?1")
    public CafeUsers findByEmailAddress(String emailaddress);

    @Query("SELECT t FROM CafeUsers t WHERE t.id = ?1")
    public CafeUsers findUserById(long id);

    @Query("SELECT t FROM CafeUsers t WHERE t.validityToken = ?1")
    public CafeUsers findUserByValidityToken(UUID token);

    @Query("SELECT t FROM CafeUsers t WHERE UPPER(t.name) LIKE %?1% AND t.internalUser=?2")
    public List<CafeUsers> searchUsers(String queryString, boolean b);

    @Query("SELECT COUNT(t) FROM CafeUsers t WHERE t.authority IN (?1)")
    public long findAdminCount(String authority);
}
