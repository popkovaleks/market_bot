package tgbot.repository;

import org.hibernate.Session;
import tgbot.model.User;

public class UserRepository {

    public User createUser(Session session, long chatId) {

        User user = new User();
        user.setChatId(chatId);

        session.beginTransaction();
        try{
            session.save(user);
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            user = null;
        }

        return user;
    }
}
