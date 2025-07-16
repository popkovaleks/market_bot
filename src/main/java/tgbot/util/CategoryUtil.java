package tgbot.util;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import tgbot.model.Category;

import java.util.List;

public class CategoryUtil {

    public void createCategory(String buttonLabel, String categoryCode){
        Category newCategory = new Category();
        newCategory.setCategoryCode(categoryCode);
        newCategory.setButtonLabel(buttonLabel);

        try(Session session = HibernateUtil.getSessionFactory().openSession()){
            session.beginTransaction();
            session.persist(newCategory);
            session.getTransaction().commit();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public List<Category> getAllCategories(){
        List<Category> categories;
        try(Session session = HibernateUtil.getSessionFactory().openSession()){
            session.beginTransaction();
            Query<Category> query = session.createQuery("FROM Category", Category.class);
            categories = query.getResultList();

            session.getTransaction().commit();
        }
        return categories;
    }
}
