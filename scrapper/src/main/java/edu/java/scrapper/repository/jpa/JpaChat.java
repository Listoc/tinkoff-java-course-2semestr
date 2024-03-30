package edu.java.scrapper.repository.jpa;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "chat")
public class JpaChat {
    @Id
    private long chatId;

    @ManyToMany
    @JoinTable(name = "chat_link_map",
               joinColumns = @JoinColumn(name = "chat_id"),
               inverseJoinColumns = @JoinColumn(name = "link_id"))
    private List<JpaLink> links = new ArrayList<>();

    public void addLink(JpaLink jpaLink) {
        links.add(jpaLink);
        jpaLink.getChats().add(this);
    }

    public void removeLink(JpaLink jpaLink) {
        links.remove(jpaLink);
        jpaLink.getChats().remove(this);
    }
}
