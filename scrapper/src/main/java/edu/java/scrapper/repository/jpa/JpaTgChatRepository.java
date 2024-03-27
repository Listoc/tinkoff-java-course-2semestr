package edu.java.scrapper.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaTgChatRepository extends JpaRepository<JpaChat, Long> {
}
