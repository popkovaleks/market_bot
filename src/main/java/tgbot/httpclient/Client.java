package tgbot.httpclient;


import feign.Param;
import feign.RequestLine;
import tgbot.dto.BaseProductDto;

import java.util.List;

public interface Client {

    @RequestLine("GET /wb/category/{id}")
    public List<BaseProductDto> getProductsByCategory(@Param("id") Long id);
}
