import org.jetbrains.annotations.NotNull;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.updateshandlers.SentCallback;

import java.util.*;
import java.util.stream.Collectors;

public class Account {
    private Long chatId;
    private Map<User, List<Transaction>> userListMap = new HashMap<>();
    private List<Transaction> expanses = new LinkedList<>();
    // private List<User> users;
    private float balance;

    Account(Long chatId) {
        this.chatId = chatId;
        this.balance = 0;
    }

    public Long getChatId() { return this.chatId; }

    public Map<User, List<Transaction>> getUserListMap() {
        return this.userListMap;
    }

    public float getBalance() {
        return this.balance;
    }

    public float getUserBalance(User user) {
        float userBalance = 0;
        for (Transaction transaction : userListMap.get(user)) {
            userBalance += transaction.getSum();
        }
        return userBalance;
    }

    public List<Transaction> getExpanses() {
        return expanses;
    }

    public Map<User, Float> getBalanceMap() {
        Float userBalance = 0.0F;
        Map<User, Float> listToFloat = new HashMap<>();
        for (User user : userListMap.keySet()) {
            Float balance = 0.0F;
            for (Transaction transaction : userListMap.get(user)) {
                balance += transaction.getSum();
            }
            listToFloat.put(user, balance);
        }
        return listToFloat;
    }

    void clearAccount() {
        userListMap.clear();
        expanses.clear();
        balance = 0;
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

    public Map<User, Float> getDebtorsMap() {
        Map<User, Float> debtorsMap = getBalanceMap();
        debtorsMap.values().removeIf(balance -> balance >= 0);
        return debtorsMap;
    }

    public Map<User, Float> getOverpayMap() {
        Map<User, Float> overpayMap = getBalanceMap();
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

