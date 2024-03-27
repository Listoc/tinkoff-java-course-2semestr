package edu.java.scrapper.repository.jpa;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaLinkRepository extends JpaRepository<JpaLink, Long> {
    Optional<JpaLink> findByUrl(String url);

    List<JpaLink> findAllByLastCheckLessThan(OffsetDateTime dateTime);
}
