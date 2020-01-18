import org.telegram.telegrambots.meta.api.objects.User;

import java.time.LocalDate;

public class Transaction {
    private float sum;
    private User user; //?
    private Long chatId; //?
    private String comment; //придумать-таки как организовать в рамках бота
    private LocalDate date;

    Transaction(float sum, User user, Long chatId, String comment) {
        this.sum = sum;
        this.user = user;
        this.chatId = chatId;
        this.comment = comment;
        this.date = LocalDate.now();
    }

    public float getSum() {
        return sum;
    }

    public User getUser() {
        return user;
    }

    public Long getChatId() {
        return chatId;
    }

    public String getComment() {
        return comment;
    }

    public LocalDate getDate() {
        return date;
    }

    public int compareTransaction(Transaction o) {
        return date.compareTo(o.getDate());
    }
}
