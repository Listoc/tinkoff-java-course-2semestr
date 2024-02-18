package edu.java.bot.repository;

import edu.java.bot.model.User;
import java.util.List;

public interface Repository {
    List<String> getLinksByUser(User user);

    boolean addLinkToUser(User user, String link);

    boolean addUser(User user);

    boolean deleteLinkByUser(User user, String link);
}
