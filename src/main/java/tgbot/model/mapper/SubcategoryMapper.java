package tgbot.model.mapper;

import com.github.slugify.Slugify;
import tgbot.model.Subcategory;
import tgbot.model.dto.SubcategoryFromParserDto;

public class SubcategoryMapper {

    public Subcategory toEntity(SubcategoryFromParserDto dto){
        Subcategory subcategory = new Subcategory();
        Slugify slugify = new Slugify();

        subcategory.setId(dto.getId());
        subcategory.setButtonLabel(dto.getName());
        subcategory.setSubcategoryCode(slugify.slugify(dto.getName()));

        return subcategory;
    }
}
