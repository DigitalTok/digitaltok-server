package com.digital_tok.template.repository;

import com.digital_tok.template.domain.PriorityTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PriorityTemplateRepository extends JpaRepository<PriorityTemplate, Long> {
}
