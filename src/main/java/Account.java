import org.jetbrains.annotations.NotNull;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.updateshandlers.SentCallback;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

public class Account implements Serializable{
 //   protected Long chatId;
    protected Map<User, List<Transaction>> userListMap;
    protected List<Transaction> expanses;
    protected double balance;
    private static final long serialVersionUID = -2529999418945499244L;

    Account() {
        userListMap = new HashMap<>();
        expanses = new ArrayList<>();
        balance = 0.0;
    }
   /* Account(Long chatId) {
        userListMap = new HashMap<>();
        expanses = new ArrayList<>();
        this.chatId = chatId;
        this.balance = 0.0;
    }

    public Long getChatId() { return this.chatId; }*/

    public Map<User, List<Transaction>> getUserListMap() {
        return this.userListMap;
    }

    public Double getBalance() {
        return this.balance;
    }

    public Double getUserBalance(User user) {
        Double userBalance = 0.0;
        for (Transaction transaction : userListMap.get(user)) {
            userBalance += transaction.getSum();
        }
        return userBalance;
    }

    public List<Transaction> getExpanses() {
        return expanses;
    }

    public Map<User, Double> getBalanceMap() {
        Double userBalance = 0.0;
        Map<User, Double> listToDouble = new HashMap<>();
        for (User user : userListMap.keySet()) {
            Double balance = 0.0;
            for (Transaction transaction : userListMap.get(user)) {
                balance += transaction.getSum();
            }
            listToDouble.put(user, balance);
        }
        return listToDouble;
    }

    void clearAccount() {
        userListMap.clear();
        expanses.clear();
        balance = 0.0;
    }

    void addUser(User user) {
        this.userListMap.put(user, new ArrayList<>());
    }

    void addExpense(Transaction transaction) {
        this.expanses.add(transaction);
        this.balance += transaction.getSum();
    }

    void addSplitExpense(Transaction transaction) {
        for (User user : userListMap.keySet()) {
            userListMap.get(user).add(transaction);
        }
    }

    void addPay(Transaction transaction, User user) {
        userListMap.get(user).add(transaction);
        this.balance += transaction.getSum();
    }

    public Map<User, Double> getDebtorsMap() {
        Map<User, Double> debtorsMap = getBalanceMap();
        debtorsMap.values().removeIf(balance -> balance >= 0);
        return debtorsMap;
    }

    public Map<User, Double> getOverpayMap() {
        Map<User, Double> overpayMap = getBalanceMap();
        overpayMap.values().removeIf(balance -> balance < 0);
        return overpayMap;
    }

    public List<Transaction> getTransactions() {
        List<Transaction> transactions = new LinkedList<>();
        for (Map.Entry<User, List<Transaction>> entry : userListMap.entrySet()) {
            transactions = entry.getValue();
        }
        transactions.removeIf(transaction -> transaction.getSum() < 0);
        transactions.addAll(expanses);
        transactions.sort(Transaction::compareTransaction);
        return transactions;
    }
}

