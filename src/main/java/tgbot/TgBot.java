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
import tgbot.model.Subcategory;
import tgbot.service.CategorySubscriber;
import tgbot.service.CreateCategoryMenu;
import tgbot.repository.SubcategoryRepository;

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
                } else if (data.matches("^subcategory:\\d+$")) {

                    SubcategoryRepository subcategoryRepository = new SubcategoryRepository();
                    List<Subcategory> subcategories = subcategoryRepository.getAllSubcategories();

                    List<Long> subcategoryIds = new ArrayList<>();
                    for (Subcategory subcategory : subcategories){
                        subcategoryIds.add(subcategory.getId());
                    }

                    long commandSubcategoryId = Long.parseLong(data.split(":")[1]);
                    if(subcategoryIds.contains(commandSubcategoryId)){

                        CategorySubscriber categorySubscriber = new CategorySubscriber();
                        int status = categorySubscriber.subscribe(chatId, commandSubcategoryId);

                        if (status == 0){
                            try {
                                tgClient.execute(categorySubscriber.successfulMessage(chatId));
                            } catch (TelegramApiException e){
                                throw new RuntimeException();
                            }
                        } else {
                            try {
                                tgClient.execute(categorySubscriber.errorMessage(chatId));
                            } catch (TelegramApiException e){
                                throw new RuntimeException();
                            }
                        }
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

                        SubcategoryRepository subcategoryRepository = new SubcategoryRepository();
                        session.beginTransaction();
                        subcategoryRepository.createSubcategory(session, categoryData[0], categoryData[1]);
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

