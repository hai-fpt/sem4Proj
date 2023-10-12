package com.lms.repository;

import com.lms.models.Configuration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ConfigurationRepository extends JpaRepository<Configuration, Long> {
    @Query(nativeQuery = true,
    value = "select google_client_id from configuration where id = 1")
    String getGoogleClientId();

    @Query(nativeQuery = true,
    value = "select host_address from configuration where id = 1")
    String getHostAddress();
}
