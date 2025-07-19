package tgbot.service.parser;

import feign.Feign;
import feign.gson.GsonDecoder;
import tgbot.httpclient.Client;
import tgbot.model.dto.BaseProductDto;
import tgbot.model.dto.SubcategoryFromParserDto;

import java.util.List;

public class ParserRequests {

    public List<BaseProductDto> getProducts(Long categoryId){
        Client wbProducts = Feign.builder()
                .decoder(new GsonDecoder())
                .target(Client.class, "http://localhost:8080");
        List<BaseProductDto> products = wbProducts.getProductsByCategory(categoryId);

        return products;
    }

    public List<SubcategoryFromParserDto> getSubcategoriesByCategory(String category) {
        Client subcategories = Feign.builder()
                .decoder(new GsonDecoder())
                .target(Client.class, "http://localhost:8080");

        List<SubcategoryFromParserDto> subcategoriesParser = subcategories.getSubcategoriesFromParser(category);
        return subcategoriesParser;
    }
}
