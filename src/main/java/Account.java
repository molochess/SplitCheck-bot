import org.telegram.telegrambots.meta.api.objects.User;

import java.util.LinkedList;
import java.util.List;

public class Account {
    private Long chatId;
    private List<Transaction> transactions;
    private List<User> users;
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
    public List<User> getUserId() {
        return this.users;
    }
    public float getBalance(){
        return this.balance;
    }
    public void addTransactions (Transaction transaction){
        this.transactions.add(transaction);
    }

    public float getUserBalance(User user) {
        float userBalance = 0;
        for (Transaction transaction : this.transactions) {
            if (transaction.getSum() < 0) {
                userBalance += transaction.getSum() / users.size();
            } else if (Math.abs(transaction.getUser().getId() - user.getId()) == 0) {
                userBalance += transaction.getSum();
            }
        }
        return userBalance;
    }
    public List<User> getDebtors(){
        List<User> debtors = new LinkedList<>();
        for (User user:this.users) {
            if (getUserBalance(user) < 0){
                debtors.add(user);
            }
        }
        return debtors;
    }
    public List<User> getOverpayers(){
        List<User> overpayers = new LinkedList<>();
        for (User user:this.users) {
            if (getUserBalance(user) > 0){
                overpayers.add(user);
            }
        }
        return overpayers;
    }
    void clearAccount(){
        this.transactions.clear();
        this.users.clear();
    }
    void updateBalance(float sum) {
        this.balance += sum;
    }
    void addUser(User user) {
        this.users.add(user);
    }
}
