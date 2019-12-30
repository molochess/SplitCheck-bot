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
        return chatId;
    }
    public List<Transaction> getTransactions(){
        return transactions;
    }
    public List<Integer> getUserId() {
        return users;
    }
    public float getBalance(){
        return balance;
    }
    public void addTransactions (Transaction transaction){
        transactions.add(transaction);
    }
    public float getUserBalance(Integer userId){
        float userBalance = 0;
        for (Transaction transaction:transactions) {
            if (transaction.getUserId() == userId) {
                userBalance += transaction.getSum();
            }
        }
        return userBalance;
    }
    public List<Integer> getDebtors(){
        List<Integer> debtors = new LinkedList<>();
        for (Integer user:users) {
            if (getUserBalance(user) > 0){
                debtors.add(user);
            }
        }
        return debtors;
    }
    void clearAccount(){
        transactions.clear();
        users.clear();
    }
}
