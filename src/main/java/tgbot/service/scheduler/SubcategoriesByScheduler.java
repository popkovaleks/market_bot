package tgbot.service.scheduler;

import org.hibernate.Session;
import tgbot.config.HibernateConfig;
import tgbot.model.Subcategory;
import tgbot.model.dto.SubcategoryFromParserDto;
import tgbot.model.mapper.SubcategoryMapper;
import tgbot.repository.SubcategoryRepository;
import tgbot.service.parser.ParserRequests;

import java.util.List;

public class SubcategoriesByScheduler implements Runnable{

    private String category;

    public SubcategoriesByScheduler(String category){
        this.category = category;
    }
    @Override
    public void run(){

        System.out.println("Шедулер пошел делать свои дела");
        ParserRequests parserRequests = new ParserRequests();

        List<SubcategoryFromParserDto> subcategories = parserRequests.getSubcategoriesByCategory(category);

        SubcategoryMapper mapper = new SubcategoryMapper();

        SubcategoryRepository subcategoryRepository = new SubcategoryRepository();

        try(Session session = HibernateConfig.getSessionFactory().openSession()) {
            session.beginTransaction();
            System.out.println("Открыл транзакцию");
            try {
                session.createQuery("DELETE FROM Subcategory").executeUpdate();
                for (SubcategoryFromParserDto subcategory : subcategories) {
                    subcategoryRepository.save(session, mapper.toEntity(subcategory));

                }

                session.getTransaction().commit();

            } catch (Exception e) {
                session.getTransaction().rollback();
            }
        }

    }
}
