package tgbot.service;

import com.vdurmont.emoji.EmojiParser;
import io.github.cdimascio.dotenv.Dotenv;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;
import tgbot.model.Subcategory;
import tgbot.repository.SubcategoryRepository;

import java.util.ArrayList;
import java.util.List;

public class StartMenu {

    public static SendMessage startMessage(long chatId){
        
        Dotenv dotenv = Dotenv.load();
        
        String greetingMessage = EmojiParser.parseToUnicode("Приветствую! :smile:\nЯ бот для отслеживания скидок на маркетплейсах!\nВыбери категорию");
        SendMessage sendMessage = SendMessage
                .builder()
                .chatId(chatId)
                .text(greetingMessage)
                .build();

        List<InlineKeyboardRow> rows = new ArrayList<>();

        SubcategoryRepository subcategoryRepository = new SubcategoryRepository();
        List<Subcategory> categories = subcategoryRepository.getAllSubcategories();

        for (Subcategory subcategory : categories){
            String buttonText = subcategory.getButtonLabel();
            String callbackData = subcategory.getSubcategoryCode();

            InlineKeyboardButton button = new InlineKeyboardButton(buttonText);
            button.setCallbackData(callbackData);

            rows.add(new InlineKeyboardRow(button));
        }

        if (chatId == Long.parseLong(dotenv.get("ADMIN_CHAT_ID"))){
            String buttonText = "Добавить категорию";
            String callbackData = "createcategory";

            InlineKeyboardButton button = new InlineKeyboardButton(buttonText);
            button.setCallbackData(callbackData);

            rows.add(new InlineKeyboardRow(button));
        }
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup(rows);
        markup.setKeyboard(rows);

        sendMessage.setReplyMarkup(markup);


        return sendMessage;
    }
}
