package org.ecommersebe.ecommersebe.repositories;

import org.ecommersebe.ecommersebe.models.entities.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
}
