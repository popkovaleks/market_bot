package tgbot.service;

import com.vdurmont.emoji.EmojiParser;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StartMenu {

    public static SendMessage startMessage(long chat_id){
        String greeting_message = EmojiParser.parseToUnicode("Приветствую! :smile:\nЯ бот для отслеживания скидок на маркетплейсах!\nВыбери категорию");
        SendMessage sendMessage = SendMessage
                .builder()
                .chatId(chat_id)
                .text(greeting_message)
                .build();

        sendMessage.setReplyMarkup(ReplyKeyboardMarkup
                .builder()
                .keyboardRow(new KeyboardRow("Электроника"))
                .build());

        String smartphoneButtonText = "\uD83D\uDCF1 Смартфоны";
        InlineKeyboardButton smartphonesButton = new InlineKeyboardButton(smartphoneButtonText);
        smartphonesButton.setCallbackData("smartphones");

        List<InlineKeyboardRow> rows = new ArrayList<>();
//        rows.add(smartphonesButton);
        rows.add(new InlineKeyboardRow(smartphonesButton));


        String laptopButtonText = "\uD83D\uDCBB Ноутбуки";
        InlineKeyboardButton laptopButton = new InlineKeyboardButton(laptopButtonText);
        laptopButton.setCallbackData("laptops");

        rows.add(new InlineKeyboardRow(laptopButton));

        InlineKeyboardMarkup markup = new InlineKeyboardMarkup(rows);
        markup.setKeyboard(rows);

        sendMessage.setReplyMarkup(markup);

        return sendMessage;
    }
}
