package tgbot.service;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public class CreateCategoryMenu {

    public SendMessage createCategoryMessage(long chatId){

        String message = "Введите название категории и код через точку с запятой(;)";

        SendMessage sendMessage = SendMessage
                .builder()
                .chatId(chatId)
                .text(message)
                .build();

        return sendMessage;

    }
}
