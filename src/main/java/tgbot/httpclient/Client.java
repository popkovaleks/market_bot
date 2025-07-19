package tgbot.httpclient;


import feign.Param;
import feign.RequestLine;
import tgbot.model.dto.BaseProductDto;
import tgbot.model.dto.SubcategoryFromParserDto;

import java.util.List;

public interface Client {

    @RequestLine("GET /product-price-parser/api/wb/category/{id}")
    public List<BaseProductDto> getProductsByCategory(@Param("id") Long id);

    @RequestLine("GET /product-price-parser/api/category/{categoryCode}/subcategories")
    public List<SubcategoryFromParserDto> getSubcategoriesFromParser(@Param("categoryCode") String string);
}
