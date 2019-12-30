import java.util.LinkedList;
import java.util.List;

public class Account {
    private Long chatId;
    private List<Transaction> transactions;
    private List<Integer> users;
    private float balance;

    Account(Long chatId) {
        this.chatId = chatId;
        this.balance = 0;
        this.transactions = new LinkedList<>();
        this.users = new LinkedList<>();
    }

    public Long getChatId(){
        return this.chatId;
    }
    public List<Transaction> getTransactions(){
        return this.transactions;
    }
    public List<Integer> getUserId() {
        return this.users;
    }
    public float getBalance(){
        return this.balance;
    }
    public void addTransactions (Transaction transaction){
        this.transactions.add(transaction);
    }
    public float getUserBalance(Integer userId) {
        float userBalance = 0;
        for (Transaction transaction:this.transactions) {
            if (transaction.getUserId() == userId) {
                userBalance += transaction.getSum();
            }
        }
        return userBalance;
    }
    public List<Integer> getDebtors(){
        List<Integer> debtors = new LinkedList<>();
        for (Integer user:this.users) {
            if (getUserBalance(user) > 0){
                debtors.add(user);
            }
        }
        return debtors;
    }
    void clearAccount(){
        this.transactions.clear();
        this.users.clear();
    }
}
