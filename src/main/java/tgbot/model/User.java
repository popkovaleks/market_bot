package tgbot.model;

import javax.persistence.*;

import java.util.HashSet;
import java.util.Set;


@Entity
@Table(name = "users")
public class User {

    @Id
    private Long chatId;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_subcategory",
            joinColumns = @JoinColumn(name = "user_chat_id", referencedColumnName = "chatId"),
            inverseJoinColumns = @JoinColumn(name = "subcategory_id"))
    private Set<Subcategory> subcategories = new HashSet<>();

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public Set<Subcategory> getSubcategories() {
        return subcategories;
    }

    public void setSubcategories(Set<Subcategory> subcategories) {
        this.subcategories = subcategories;
    }

}
