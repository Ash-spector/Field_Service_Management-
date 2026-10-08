package com.zidio.keystones.repository;

import com.zidio.keystones.entity.Site;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SiteRepository extends JpaRepository<Site, Long> {
    List<Site> findByCustomerId(Long customerId);

    @Query("SELECT s FROM Site s WHERE s.customer.id = :customerId AND (:search IS NULL OR LOWER(s.name) LIKE LOWER(CONCAT('%', :search, '%')))")
    Page<Site> findByCustomerIdAndSearch(@Param("customerId") Long customerId, @Param("search") String search, Pageable pageable);
}
