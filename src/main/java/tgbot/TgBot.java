package tgbot;

import io.github.cdimascio.dotenv.Dotenv;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import tgbot.service.CreateCategoryMenu;
import tgbot.service.ShowProducts;
import tgbot.util.CategoryUtil;

import static tgbot.service.StartMenu.startMessage;

public class TgBot implements LongPollingSingleThreadUpdateConsumer {
    Dotenv dotenv = Dotenv.load();
    private final TelegramClient tgClient;

    public TgBot(String botToken){
        tgClient = new OkHttpTelegramClient(botToken);
    }
    //private TelegramClient tgClient = new OkHttpTelegramClient(dotenv.get("token"));
    private String state = "start";

    @Override
    public void consume(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()){
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();
            
            if (state.equals("createcategory")){
                String[] categoryData = messageText.split(";");
                if (categoryData.length != 2){
                    CreateCategoryMenu createCategoryMenu = new CreateCategoryMenu();
                    SendMessage sendMessage = createCategoryMenu.createCategoryMessage(chatId);
                    try {
                        tgClient.execute(sendMessage);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }
                }

                CategoryUtil categoryUtil = new CategoryUtil();
                categoryUtil.createCategory(categoryData[0], categoryData[1]);
                state = "start";
                SendMessage sendMessage = startMessage(chatId);
                try{
                    tgClient.execute(sendMessage);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }

            }else if (messageText.equals("/start")) {

                SendMessage sendMessage = startMessage(chatId);
                try{
                    tgClient.execute(sendMessage);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
                state = "start";
            } else if (messageText.equals("Электроника")) {
                SendMessage sendMessage = new ShowProducts().showProductsByCategory(chatId, 1l);
                try{
                    tgClient.execute(sendMessage);
                }catch (TelegramApiException e){
                    e.printStackTrace();
                }

            } else if (messageText.equals("/keyboard")) {
                SendMessage message = SendMessage
                        .builder()
                        .chatId(chatId)
                        .text(messageText)
                        .build();

                message.setReplyMarkup(ReplyKeyboardMarkup
                        .builder()
                        .keyboardRow(new KeyboardRow("Row 1 button 1", "Row 1 button 2", "Row 1 button 3"))
                        .keyboardRow(new KeyboardRow("Row 2 button 1", "Row 2 button 2", "Row 2 button 3"))
                        .build()
                );

                try {
                    tgClient.execute(message);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
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
            System.out.println(update.getMessage().getText());
        } else if (update.hasCallbackQuery()) {
            String data = update.getCallbackQuery().getData();
            Long chatId = update.getCallbackQuery().getMessage().getChatId();

            if (data.equals("createcategory") && state.equals("start")){
                if (chatId == Long.parseLong(dotenv.get("ADMIN_CHAT_ID"))){

                    CreateCategoryMenu createCategoryMenu = new CreateCategoryMenu();
                    SendMessage sendMessage = createCategoryMenu.createCategoryMessage(chatId);
                    try {
                        tgClient.execute(sendMessage);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }
                    state = "createcategory";
//                    CategoryUtil catUtil = new CategoryUtil();
//                    catUtil.createCategory("Умный дом", "smart_home");
                }
            }
        }
    }
}
