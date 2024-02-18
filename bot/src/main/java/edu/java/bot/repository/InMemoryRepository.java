package edu.java.bot.repository;

import edu.java.bot.model.User;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class InMemoryRepository implements Repository {
    private final Map<Long, List<String>> links;
    private final static List<String> EMPTY_LIST = Collections.emptyList();

    public InMemoryRepository() {
        this.links = new HashMap<>();
    }

    @Override
    public List<String> getLinksByUser(User user) {
        if (!links.containsKey(user.id())) {
            return null;
        }

        return links.getOrDefault(user.id(), EMPTY_LIST);
    }

    @Override
    public boolean addLinkToUser(User user, String link) {
        if (!links.containsKey(user.id())) {
            return false;
        }

        return links.get(user.id()).add(link);
    }

    @Override
    public boolean addUser(User user) {
        if (links.containsKey(user.id())) {
            return false;
        }

        links.put(user.id(), new ArrayList<>());

        return true;
    }

    @Override
    public boolean deleteLinkByUser(User user, String link) {
        if (!links.containsKey(user.id())) {
            return false;
        }

        links.get(user.id()).remove(link);

        return true;
    }
}
