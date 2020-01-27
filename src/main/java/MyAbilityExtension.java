import org.telegram.abilitybots.api.db.DBContext;
import org.telegram.abilitybots.api.objects.Ability;
import org.telegram.abilitybots.api.objects.Locality;
import org.telegram.abilitybots.api.objects.Privacy;
import org.telegram.abilitybots.api.sender.SilentSender;
import org.telegram.abilitybots.api.util.AbilityExtension;
import org.telegram.telegrambots.meta.api.objects.User;

import java.io.Serializable;

import java.util.List;
import java.util.Map;


public class MyAbilityExtension implements AbilityExtension {
    private SilentSender silent;
    DBContext db;
    public MyAbilityExtension(SilentSender silent, DBContext db) {
        this.silent = silent;
        this.db = db;
    }

    public Ability start() {
        return Ability
                .builder()
                .name("start")
                .locality(Locality.ALL)
                .privacy(Privacy.PUBLIC)
                .input(0)
                .action(ctx -> {
                    Map<Long, Account> accountMap = db.getMap("accounts");
                    accountMap.put(ctx.chatId(), new Account());
                    silent.send("Hello! This bot was created to help you and your comrades " +
                            "when you need to split common expenses. " +
                            "Hope you'll love it!)", ctx.chatId());
                    silent.send("Everyone in the chat who wants join this account need to press /join",
                            ctx.chatId());
                    silent.send("To add new purchase or other expense send /purchase " +
                            "with a sum of it and short comment" +
                            "\nTo add your pay send /pay with sum of payment and short comment" +
                            "\nIf you need balance of this account press /balance" +
                            "\n/members shows a list of people who joined account whit their oun balance" +
                            "\n/debtors shows list of malicious debtors" +
                            "\n/overpay shows list of the most generous people in company (they paid too much)" +
                            "\n/trans shows full list of transactions for this account" +
                            "\n/stop clears account history (deletes members and transactions)", ctx.chatId());
                })
                .build();
    }

    public Ability join() {
        return Ability
                .builder()
                .name("join")
                .locality(Locality.ALL)
                .privacy(Privacy.PUBLIC)
                .input(0)
                .action(ctx -> {
                    Map<Long, Account> accountMap = db.getMap("accounts");
                    Account account = accountMap.get(ctx.chatId());
                    silent.send(String.format("User %s join this account", ctx.user().getFirstName()), ctx.chatId());
                    account.addUser(ctx.user());
                    accountMap.put(ctx.chatId(), account);
                    silent.send(String.format("User %s join this account", ctx.user().getFirstName()), ctx.chatId());
                })
                .build();
    }

    public Ability purchase() {
        return Ability
                .builder()
                .name("purchase")
                .locality(Locality.ALL)
                .privacy(Privacy.PUBLIC)
                .action(ctx -> {
                    Double sum = Double.parseDouble(ctx.firstArg());
                    String comment = ctx.secondArg();
                    Map<Long, Account> accountMap = db.getMap("accounts");
                    Account account = accountMap.get(ctx.chatId());
                    account.addExpense(new Transaction(-sum, ctx.user(), ctx.chatId(), comment));
                    account.addSplitExpense(new Transaction(-sum / account.getUserListMap().size(),
                            ctx.user(), ctx.chatId(), comment));
                    accountMap.put(ctx.chatId(), (Account) account);
                    silent.send(String.format("Purchase %s for %s was added", sum, comment), ctx.chatId());
                })
                .build();
    }

    public Ability pay() {
        return Ability
                .builder()
                .name("pay")
                .locality(Locality.ALL)
                .privacy(Privacy.PUBLIC)
                .input(2)
                .action(ctx -> {
                    Double sum = Double.parseDouble(ctx.firstArg());
                    String comment = ctx.secondArg();
                    Transaction transaction = new Transaction(sum, ctx.user(), ctx.chatId(), comment);
                    Map<Long, Account> accountMap = db.getMap("accounts");
                    Account account = accountMap.get(ctx.chatId());
                    account.addPay(transaction, ctx.user());
                    accountMap.put(ctx.chatId(), account);
                    silent.send(String.format("Pay %s for %s was added by %s", sum, comment,
                            ctx.user().getFirstName()), ctx.chatId());
                })
                .build();
    }

    public Ability showTransactions() {
        return Ability
                .builder()
                .name("trans")
                .locality(Locality.ALL)
                .privacy(Privacy.PUBLIC)
                .action(ctx -> {
                    String message = "Transactions:";
                    Map<Long, Account> accountMap = db.getMap("accounts");
                    Account account = accountMap.get(ctx.chatId());
                    List<Transaction> transactionList = account.getTransactions();
                    for (Transaction transaction : transactionList) {
                        message += String.format("\n%s - %s by %s %s", transaction.getSum(),
                                transaction.getComment(), transaction.getUser().getFirstName(),
                                transaction.getDate());
                    }
                    silent.send(message, ctx.chatId());
                })
                .build();
    }

    public Ability showBalance() {
        return Ability
                .builder()
                .name("balance")
                .locality(Locality.ALL)
                .privacy(Privacy.PUBLIC)
                .action(ctx -> {
                    String message = "Balance of this account:";
                    Map<Long, Account> accountMap = db.getMap("accounts");
                    Account account = accountMap.get(ctx.chatId());
                    message += String.format("\n%s", account.getBalance());
                    silent.send(message, ctx.chatId());
                })
                .build();
    }

    public Ability showExpanses() {
        return Ability
                .builder()
                .name("expanses")
                .locality(Locality.ALL)
                .privacy(Privacy.PUBLIC)
                .action(ctx -> {
                    String message = "Expanses:";
                    Map<Long, Account> accountMap = db.getMap("accounts");
                    Account account = accountMap.get(ctx.chatId());
                    for (User person : account.getUserListMap().keySet()) {
                        message += String.format("\n%s: %s", person.getFirstName(), account.getUserBalance(person));
                    }
                    silent.send(message, ctx.chatId());
                })
                .build();
    }

    public Ability showPeople() {
        return Ability
                .builder()
                .name("members")
                .locality(Locality.ALL)
                .privacy(Privacy.PUBLIC)
                .action(ctx -> {
                    String message = "Members:";
                    Map<Long, Account> accountMap = db.getMap("accounts");
                    Account account = accountMap.get(ctx.chatId());
                    for (User person : account.getUserListMap().keySet()) {
                        message += String.format("\n%s: %s", person.getFirstName(), account.getUserBalance(person));
                    }
                    silent.send(message, ctx.chatId());
                })
                .build();
    }

    public Ability showDebtors() {
        return Ability
                .builder()
                .name("debtors")
                .locality(Locality.ALL)
                .privacy(Privacy.PUBLIC)
                .action(ctx -> {
                    String message = "Debtors:";
                    Map<Long, Account> accountMap = db.getMap("accounts");
                    Account account = accountMap.get(ctx.chatId());
                    for (User person : account.getDebtorsMap().keySet()) {
                        message += String.format("\n%s: %s", person.getFirstName(), account.getUserBalance(person));
                    }
                    silent.send(message, ctx.chatId());
                })
                .build();
    }

    public Ability showOverpay() {
        return Ability
                .builder()
                .name("overpay")
                .locality(Locality.ALL)
                .privacy(Privacy.PUBLIC)
                .action(ctx -> {
                    String message = "Overpaid members:";
                    Map<Long, Account> accountMap = db.getMap("accounts");
                    Account account = accountMap.get(ctx.chatId());
                    for (User person : account.getOverpayMap().keySet()) {
                        message += String.format("\n%s: %s", person.getFirstName(), account.getUserBalance(person));
                    }
                    silent.send(message, ctx.chatId());
                })
                .build();
    }

    public Ability stop() {
        return Ability
                .builder()
                .name("stop")
                .locality(Locality.ALL)
                .privacy(Privacy.PUBLIC)
                .action(ctx -> {
                    Map<Long, Account> accountMap = db.getMap("accounts");
                    Account account = accountMap.get(ctx.chatId());
                    account.clearAccount();
                    accountMap.put(ctx.chatId(),account);
                    silent.send("Account was cleared", ctx.chatId());
                })
                .build();
    }

}
