package com.zidio.keystones.repository;

import com.zidio.keystones.entity.EmailLogs;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailLogRepository extends JpaRepository<EmailLogs, Long> {
}
