package tgbot.service;

import com.vdurmont.emoji.EmojiParser;
import io.github.cdimascio.dotenv.Dotenv;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import tgbot.model.Category;
import tgbot.util.CategoryUtil;
import tgbot.util.HibernateUtil;

import java.util.ArrayList;
import java.util.Arrays;
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

        CategoryUtil categoryUtil = new CategoryUtil();
        List<Category> categories = categoryUtil.getAllCategories();

        for (Category category : categories){
            String buttonText = category.getButtonLabel();
            String callbackData = category.getCategoryCode();

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
