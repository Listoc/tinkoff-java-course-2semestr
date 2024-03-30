package edu.java.scrapper.repository.jpa;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "link")
public class JpaLink {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long linkId;

    private String url;

    private OffsetDateTime lastCheck;

    @ManyToMany(mappedBy = "links")
    private List<JpaChat> chats = new ArrayList<>();
}
