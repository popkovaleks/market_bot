package tgbot.repository;

import org.hibernate.Session;
import org.hibernate.query.Query;
import tgbot.config.HibernateConfig;
import tgbot.model.Subcategory;

import java.util.List;

public class SubcategoryRepository {

    public void createSubcategory(Session session, String buttonLabel, String categoryCode){
        Subcategory newSubcategory = new Subcategory();
        newSubcategory.setSubcategoryCode(categoryCode);
        newSubcategory.setButtonLabel(buttonLabel);

        session.persist(newSubcategory);

    }

    public void update(Session session, Subcategory subcategory){
        session.merge(subcategory);
    }

    public void save(Session session, Subcategory subcategory){
        session.save(subcategory);
    }

    public List<Subcategory> getAllSubcategories(){
        List<Subcategory> categories;
        try(Session session = HibernateConfig.getSessionFactory().openSession()){
            session.beginTransaction();
            Query<Subcategory> query = session.createQuery("FROM Subcategory", Subcategory.class);
            categories = query.getResultList();

            session.getTransaction().commit();
        }
        return categories;
    }
}
