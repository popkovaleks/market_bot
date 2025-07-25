package tgbot.service.scheduler;

import org.hibernate.Session;
import tgbot.config.HibernateConfig;
import tgbot.model.Subcategory;
import tgbot.model.dto.SubcategoryFromParserDto;
import tgbot.model.mapper.SubcategoryMapper;
import tgbot.repository.SubcategoryRepository;
import tgbot.service.parser.ParserRequests;

import java.util.List;
import java.util.stream.Collectors;

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

        List<Long> newIds = subcategories.stream().map(SubcategoryFromParserDto::getId).collect(Collectors.toList());

        SubcategoryMapper mapper = new SubcategoryMapper();

        SubcategoryRepository subcategoryRepository = new SubcategoryRepository();

        try(Session session = HibernateConfig.getSessionFactory().openSession()) {
            session.beginTransaction();
            System.out.println("Открыл транзакцию");

            try {

                for (SubcategoryFromParserDto subcategoryDto : subcategories) {
                    Subcategory subcategory = session.find(Subcategory.class, subcategoryDto.getId());

                    if (subcategory != null){
                        subcategoryRepository.update(session, mapper.toEntity(subcategoryDto));
                    } else {
                        subcategoryRepository.save(session, mapper.toEntity(subcategoryDto));
                    }

                }

                session.createQuery("DELETE FROM Subcategory s where s.id NOT IN (:ids)")
                        .setParameter("ids", newIds)
                        .executeUpdate();

                session.getTransaction().commit();

            } catch (Exception e) {
                session.getTransaction().rollback();
            }
        }

    }
}
