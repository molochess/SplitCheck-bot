public class Transaction {
    private float sum;
    private Integer userId;
    private  Long chatId;
    private String comment;

    Transaction(float sum, Integer userId, Long chatId, String comment){
        this.sum = sum;
        this.userId = userId;
        this.chatId = chatId;
        this.comment = comment;
    }

    public float getSum(){
        return sum;
    }
    public Integer getUserId(){
        return userId;
    }
    public Long getChatId(){
        return chatId;
    }
    public String getComment() { return comment; }
}
