import org.telegram.telegrambots.meta.api.objects.User;

public class Transaction {
    private float sum;
    private User user;
    private  Long chatId;
    private String comment;

    Transaction(float sum, User user, Long chatId, String comment){
        this.sum = sum;
        this.user = user;
        this.chatId = chatId;
        this.comment = comment;
    }

    public float getSum(){
        return sum;
    }
    public User getUser(){
        return user;
    }
    public Long getChatId(){
        return chatId;
    }
    public String getComment() { return comment; }
}
