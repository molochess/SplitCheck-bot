import org.telegram.abilitybots.api.db.DBContext;
import org.telegram.abilitybots.api.objects.Ability;
import org.telegram.abilitybots.api.objects.Locality;
import org.telegram.abilitybots.api.objects.Privacy;
import org.telegram.abilitybots.api.sender.SilentSender;
import org.telegram.abilitybots.api.util.AbilityExtension;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.ArrayList;
import java.util.List;


public class MyAbilityExtension implements AbilityExtension {
    private SilentSender silent;
    DBContext db;
    private List<Account> accountList= new ArrayList<Account>();

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
                    Account account = new Account(ctx.chatId());
                    accountList.add(account);
                    silent.send("Hello! This bot was created to help you and your comrades when you need to split common expenses. " +
                            "Hope you'll love it!)", ctx.chatId());
                    silent.send("Press /start to begin use. Then everyone in the chat who wants join this account need to press /join", ctx.chatId());
                    silent.send("To add new purchase or other expense send /purchase with a sum of it and short comment" +
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
                    for (Account account : accountList) {
                        if (Math.abs(account.getChatId() - ctx.chatId()) == 0) {
                            account.addUser(ctx.user());
                            break;
                        }
                    }
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
                .input(2)
                .action(ctx -> {
                    float sum = Float.parseFloat(ctx.firstArg());
                    String comment = ctx.secondArg();
                    Transaction tr =  new Transaction(-sum, ctx.user(), ctx.chatId(), comment);
                    for (Account account : accountList) {
                        if (Math.abs(account.getChatId() - ctx.chatId()) == 0) {
                            account.addTransactions(tr);
                            account.updateBalance(-sum);
                            break;
                        }
                    }
                    silent.send(String.format("Purchase %s for %s was added by %s", sum, comment, ctx.user().getFirstName()), ctx.chatId());
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
                    float sum = Float.parseFloat(ctx.firstArg());
                    String comment = ctx.secondArg();
                    Transaction tr = new Transaction(sum, ctx.user(), ctx.chatId(), comment);
                    for (Account account : accountList) {
                        if (Math.abs(account.getChatId() - ctx.chatId()) == 0) {
                            account.addTransactions(tr);
                            account.updateBalance(sum);
                        }
                    }
                    silent.send(String.format("Pay %s for %s was added by %s", sum, comment, ctx.user().getFirstName()), ctx.chatId());
                })
                .build();
    }

    /*   public Ability showAll(){
           return Ability
                   .builder()
                   .name("all")
                   .locality(Locality.ALL)
                   .privacy(Privacy.PUBLIC)
                   .action(ctx -> {
                       String message = String.format("%s", ctx.chatId());
                       for (Account account: accountList) {
                           if (Math.abs(account.getChatId() - ctx.chatId()) == 0)
                           message += String.format("\n%s yes", account.getChatId());
                       }
                       silent.send(message, ctx.chatId());
                   })
                   .build();
       }*/
    public Ability showTransactions() {
        return Ability
                .builder()
                .name("trans")
                .locality(Locality.ALL)
                .privacy(Privacy.PUBLIC)
                .action(ctx -> {
                    String message = "Transactions:";
                    for (Account account : accountList) {
                        if (Math.abs(account.getChatId() - ctx.chatId()) == 0) {
                            List<Transaction> transactionList = account.getTransactions();
                            for (Transaction transaction : transactionList) {
                                message += String.format("\n%s - %s by %s", transaction.getSum(), transaction.getComment(), transaction.getUser().getFirstName());
                            }
                        }
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
                    for (Account account : accountList) {
                        if (Math.abs(account.getChatId() - ctx.chatId()) == 0) {
                            message += String.format("\n%s", account.getBalance());
                        }
                    }
                    silent.send(message, ctx.chatId());
                })
                .build();
    }

    public Ability showPeople(){
        return Ability
                .builder()
                .name("members")
                .locality(Locality.ALL)
                .privacy(Privacy.PUBLIC)
                .action(ctx -> {
                    String message = "Members:";
                    //for (Transaction transaction: transactionList) {
                    for (Account account: accountList) {
                        if (Math.abs(account.getChatId() - ctx.chatId()) == 0) {
                            List<User> peopleList = account.getUserId();
                            for (User person : peopleList ) {
                                message += String.format("\n%s: %s", person.getFirstName(), account.getUserBalance(person));
                            }
                        }
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
                    for (Account account : accountList) {
                        if (Math.abs(account.getChatId() - ctx.chatId()) == 0) {
                            List<User> debtorsList = account.getDebtors();
                            for (User person : debtorsList) {
                                message += String.format("\n%s: %s", person.getFirstName(), account.getUserBalance(person));
                            }
                        }
                    }
                    silent.send(message, ctx.chatId());
                })
                .build();
    }

    public Ability showOverpayers(){
        return Ability
                .builder()
                .name("overpay")
                .locality(Locality.ALL)
                .privacy(Privacy.PUBLIC)
                .action(ctx -> {
                    String message = "Overpayers:";
                    for (Account account: accountList) {
                        if (Math.abs(account.getChatId() - ctx.chatId()) == 0) {
                            List<User> overpayersList = account.getOverpayers();
                            for (User person : overpayersList ) {
                                message += String.format("\n%s: %s", person.getFirstName(), account.getUserBalance(person));
                            }
                        }
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
                    for (Account account : accountList) {
                        if (Math.abs(account.getChatId() - ctx.chatId()) == 0) {
                            account.clearAccount();
                        }
                    }
                    silent.send("Account was cleared", ctx.chatId());
                })
                .build();
    }

}
