package tgbot.service;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

public class StartMenu {

    public static SendMessage startMessage(long chat_id){
        String greeting_message = "Приветствую!\nЯ бот для отслеживания скидок на маркетплейсах!\nВыбери категорию";
        SendMessage sendMessage = SendMessage
                .builder()
                .chatId(chat_id)
                .text(greeting_message)
                .build();

        sendMessage.setReplyMarkup(ReplyKeyboardMarkup
                .builder()
                .keyboardRow(new KeyboardRow("Электроника"))
                .build());

        return sendMessage;
    }
}
