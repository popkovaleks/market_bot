package tgbot;

import io.github.cdimascio.dotenv.Dotenv;
import org.hibernate.Session;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import tgbot.config.HibernateConfig;
import tgbot.model.Category;
import tgbot.service.CreateCategoryMenu;
import tgbot.repository.CategoryRepository;

import java.util.ArrayList;
import java.util.List;

import static tgbot.service.StartMenu.startMessage;

public class TgBot implements LongPollingSingleThreadUpdateConsumer {
    Dotenv dotenv = Dotenv.load();
    private final TelegramClient tgClient;

    public TgBot(String botToken) {
        tgClient = new OkHttpTelegramClient(botToken);
    }

    private String state = "start";

    @Override
    public void consume(Update update) {

        if (state.equals("start")) {
            if (update.hasCallbackQuery()) {
                String data = update.getCallbackQuery().getData();
                Long chatId = update.getCallbackQuery().getMessage().getChatId();

                if (data.equals("createcategory")) {
                    if (chatId == Long.parseLong(dotenv.get("ADMIN_CHAT_ID"))) {

                        CreateCategoryMenu createCategoryMenu = new CreateCategoryMenu();
                        SendMessage sendMessage = createCategoryMenu.createCategoryMessage(chatId);
                        try {
                            tgClient.execute(sendMessage);
                        } catch (TelegramApiException e) {
                            throw new RuntimeException(e);
                        }
                        state = "createcategory";
                    }
                } else {

                    CategoryRepository categoryRepository = new CategoryRepository();
                    List<Category> categories = categoryRepository.getAllCategories();

                    List<String> categoryCodes = new ArrayList<>();
                    for (Category category : categories){
                        categoryCodes.add(category.getCategoryCode());
                    }

                    if(categoryCodes.contains(data)){
                        //TODO: вызвать метод подписки на категорию
                    }
                }
            } else if (update.hasMessage() && update.getMessage().hasText()) {
                String messageText = update.getMessage().getText();
                long chatId = update.getMessage().getChatId();

                if (messageText.equals("/start")) {

                    SendMessage sendMessage = startMessage(chatId);
                    try {
                        tgClient.execute(sendMessage);
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                    state = "start";
                } else {
                    SendMessage message = SendMessage
                            .builder()
                            .chatId(chatId)
                            .text("Unknown command")
                            .build();

                    try {
                        tgClient.execute(message);
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                }
            }
        } else if (state.equals("createcategory")) {
            if (update.hasMessage() && update.getMessage().hasText()) {
                String messageText = update.getMessage().getText();
                long chatId = update.getMessage().getChatId();

                if (state.equals("createcategory")) {
                    String[] categoryData = messageText.split(";");
                    if (categoryData.length != 2) {
                        CreateCategoryMenu createCategoryMenu = new CreateCategoryMenu();
                        SendMessage sendMessage = createCategoryMenu.createCategoryMessage(chatId);
                        try {
                            tgClient.execute(sendMessage);
                        } catch (TelegramApiException e) {
                            throw new RuntimeException(e);
                        }
                    }

                    try(Session session = HibernateConfig.getSessionFactory().openSession()){

                        CategoryRepository categoryRepository = new CategoryRepository();
                        session.beginTransaction();
                        categoryRepository.createCategory(session, categoryData[0], categoryData[1]);
                        session.getTransaction().commit();
                    } catch (Exception e){
                    e.printStackTrace();
                }

                    state = "start";
                    SendMessage sendMessage = startMessage(chatId);
                    try {
                        tgClient.execute(sendMessage);
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }

                }
            }
        }
    }
}

