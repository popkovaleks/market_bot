package tgbot.service;

import org.apache.kafka.common.network.Send;
import org.hibernate.Session;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import tgbot.config.HibernateConfig;
import tgbot.model.Subcategory;
import tgbot.model.User;
import tgbot.repository.UserRepository;


import java.util.Set;

public class CategorySubscriber {

    public int subscribe(long chatId, long subcategoryId){
        int status;

        try(Session session = HibernateConfig.getSessionFactory().openSession()){
            User user = session.find(User.class, chatId);

            if (user == null){
                UserRepository userRepository = new UserRepository();
                user = userRepository.createUser(session, chatId);
            }
            if (user != null) {
                session.beginTransaction();
                try {
                    Set<Subcategory> subcategories = user.getSubcategories();
                    subcategories.add(session.find(Subcategory.class, subcategoryId));
                    user.setSubcategories(subcategories);
                    session.save(user);
                    session.getTransaction().commit();
                    status = 0;
                } catch (Exception e) {
                    session.getTransaction().rollback();
                    status = 1;
                }
            } else {
                status = 1;
            }


        }
        return status;
    }

    public SendMessage successfulMessage(long chatId){

        String message = "Вы успешно подписались на новую категорию";

        SendMessage sendMessage = SendMessage
                .builder()
                .text(message)
                .chatId(chatId)
                .build();

        return sendMessage;
    }

    public SendMessage errorMessage(long chatId){
        String message = "Что-то пошло не так";

        SendMessage sendMessage = SendMessage
                .builder()
                .text(message)
                .chatId(chatId)
                .build();

        return sendMessage;
    }
}
