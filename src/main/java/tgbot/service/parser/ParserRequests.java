package tgbot.service.parser;

import feign.Feign;
import feign.gson.GsonDecoder;
import tgbot.httpclient.Client;
import tgbot.model.BaseProductDto;

import java.util.List;

public class ParserRequests {

    public List<BaseProductDto> getProducts(Long category_id){
        Client wbProducts = Feign.builder()
                .decoder(new GsonDecoder())
                .target(Client.class, "http://localhost:8080");
        List<BaseProductDto> products = wbProducts.getProductsByCategory(category_id);

        return products;
    }

}
