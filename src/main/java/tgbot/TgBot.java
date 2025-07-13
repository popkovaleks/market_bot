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
import tgbot.service.ShowProducts;

import static tgbot.service.StartMenu.startMessage;

public class TgBot implements LongPollingSingleThreadUpdateConsumer {
    //Dotenv dotenv = Dotenv.load();
    private final TelegramClient tgClient;

    public TgBot(String botToken){
        tgClient = new OkHttpTelegramClient(botToken);
    }
    //private TelegramClient tgClient = new OkHttpTelegramClient(dotenv.get("token"));
    @Override
    public void consume(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()){
            String message_text = update.getMessage().getText();
            long chat_id = update.getMessage().getChatId();

            if (message_text.equals("/start")) {

                SendMessage sendMessage = startMessage(chat_id);
                try{
                    tgClient.execute(sendMessage);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }

            } else if (message_text.equals("Электроника")) {
                SendMessage sendMessage = new ShowProducts().showProductsByCategory(chat_id, 1l);
                try{
                    tgClient.execute(sendMessage);
                }catch (TelegramApiException e){
                    e.printStackTrace();
                }

            } else if (message_text.equals("/keyboard")) {
                SendMessage message = SendMessage
                        .builder()
                        .chatId(chat_id)
                        .text(message_text)
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
                        .chatId(chat_id)
                        .text("Unknown command")
                        .build();

                try {
                    tgClient.execute(message);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }
            System.out.println(update.getMessage().getText());
        }
    }
}
