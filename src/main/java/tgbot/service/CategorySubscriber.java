package tgbot.service;

import org.hibernate.Session;
import tgbot.config.HibernateConfig;
import tgbot.model.Subcategory;
import tgbot.model.User;
import tgbot.repository.UserRepository;


import java.util.Set;

public class CategorySubscriber {

    public void subscribe(long chatId, long subcategoryId){

        try(Session session = HibernateConfig.getSessionFactory().openSession()){
            User user = session.find(User.class, chatId);

            if (user == null){
                UserRepository userRepository = new UserRepository();
                user = userRepository.createUser(session, chatId);
            }
            if (user != null){
                session.beginTransaction();
                try {
                    Set<Subcategory> subcategories = user.getSubcategories();
                    subcategories.add(session.find(Subcategory.class, subcategoryId));
                    user.setSubcategories(subcategories);
                    session.save(user);
                    session.getTransaction().commit();
                } catch (Exception e) {
                    session.getTransaction().rollback();
                }
            }
        }

    }
}
