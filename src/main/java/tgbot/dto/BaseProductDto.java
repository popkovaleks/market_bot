package tgbot.dto;

import lombok.Getter;
import lombok.Setter;
import tgbot.enums.Source;

@Getter
@Setter
public class BaseProductDto {
    public class BaseProduct {
        private String id;
        private String name;
        private Source source;
        private Long newPrice;
        private Long oldPrice;
        private String link;
    }
}
