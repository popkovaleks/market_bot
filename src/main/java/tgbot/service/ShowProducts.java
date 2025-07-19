package tgbot.service;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import tgbot.model.dto.BaseProductDto;
import tgbot.service.parser.ParserRequests;


import java.util.List;

public class ShowProducts {

    public SendMessage showProductsByCategory(long chat_id, long category_id){
        ParserRequests parser = new ParserRequests();
        List<BaseProductDto> products = parser.getProducts(category_id);

        return SendMessage
                .builder()
                .chatId(chat_id)
                .text("Найдено товаров:" + products.size())
                .build();
    }
}
