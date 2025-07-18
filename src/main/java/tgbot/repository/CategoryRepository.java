package tgbot.repository;

import org.hibernate.Session;
import org.hibernate.query.Query;
import tgbot.config.HibernateConfig;
import tgbot.model.Category;

import java.util.List;

public class CategoryRepository {

    public void createCategory(Session session, String buttonLabel, String categoryCode){
        Category newCategory = new Category();
        newCategory.setCategoryCode(categoryCode);
        newCategory.setButtonLabel(buttonLabel);

        session.persist(newCategory);

    }

    public List<Category> getAllCategories(){
        List<Category> categories;
        try(Session session = HibernateConfig.getSessionFactory().openSession()){
            session.beginTransaction();
            Query<Category> query = session.createQuery("FROM Category", Category.class);
            categories = query.getResultList();

            session.getTransaction().commit();
        }
        return categories;
    }
}
