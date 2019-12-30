import org.telegram.abilitybots.api.db.DBContext;
import org.telegram.abilitybots.api.objects.Ability;
import org.telegram.abilitybots.api.objects.Locality;
import org.telegram.abilitybots.api.objects.Privacy;
import org.telegram.abilitybots.api.objects.Reply;
import org.telegram.abilitybots.api.sender.SilentSender;
import org.telegram.abilitybots.api.util.AbilityExtension;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.ArrayList;
import java.util.List;


public class MyAbilityExtension implements AbilityExtension {
    private SilentSender silent;
    DBContext db;
    private List<Account> accountList= new ArrayList<Account>();
    //private List<Transaction> transactionList = new ArrayList<Transaction>();
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
                .action(ctx -> {
                    accountList.add(new Account(ctx.chatId()));
                    silent.send("mhe", ctx.chatId());
                    silent.execute(InlineKeyboard.withButtonsRegistration(ctx.chatId()));
               //   new SendMessage().setChatId(ctx.chatId()).setReplyMarkup(InlineKeyboard.withButtonsRegistration());
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
                    Transaction tr =  new Transaction(-sum, ctx.user().getId(), ctx.chatId(), comment);
                    //transactionList.add(tr);
                    for (Account account : accountList) {
                        if (account.getChatId() == ctx.chatId()) {
                            account.addTransactions(tr);
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
                    Transaction tr =  new Transaction(sum, ctx.user().getId(), ctx.chatId(), comment);
                    //transactionList.add(tr);
                    for (Account account : accountList) {
                        if (account.getChatId() == ctx.chatId()) {
                            account.addTransactions(tr);
                        }
                    }
                    silent.send(String.format("Pay %s for %s was added by %s", sum, comment, ctx.user().getFirstName()), ctx.chatId());
                })
                .build();
    }

    public Ability showTransactions(){
        return Ability
                .builder()
                .name("trans")
                .locality(Locality.ALL)
                .privacy(Privacy.PUBLIC)
                .action(ctx -> {
                    String message = "Transactions:";
                    //for (Transaction transaction: transactionList) {
                    for (Account account: accountList) {
                        if (account.getChatId() == ctx.chatId()) {
                            List<Transaction> transactionList = account.getTransactions();
                            for (Transaction transaction : transactionList) {
                                message += String.format("\n %s - %s by %s", transaction.getSum(), transaction.getComment(), transaction.getUserId());
                            }
                        }
                    }

                    silent.send(message, ctx.chatId());
                })
                .build();
    }

    public Ability showPeople(){
        return Ability
                .builder()
                .name("people")
                .locality(Locality.ALL)
                .privacy(Privacy.PUBLIC)
                .action(ctx -> {
                    String message = "Members:";
                    //for (Transaction transaction: transactionList) {
                    for (Account account: accountList) {
                        if (account.getChatId() == ctx.chatId()) {
                            List<Integer> peopleList = account.getUserId();
                            for (Integer person : peopleList ) {
                                message += String.format("\n%s: %s", person, account.getUserBalance(person));
                            }
                        }
                    }
                    silent.send(message, ctx.chatId());
                })
                .build();
    }

    public Ability stop(){
        return Ability
                .builder()
                .name("stop")
                .locality(Locality.ALL)
                .privacy(Privacy.PUBLIC)
                .action(ctx -> {
                    for (Account account : accountList) {
                        if (account.getChatId() == ctx.chatId()){
                            account.clearAccount();
                        }
                    }
                    //for (Transaction transaction : transactionList) {
                      //  if (transaction.getChatId() == ctx.chatId()){
                        //    transactionList.remove(transaction);
                        //}
                    //
                    silent.send("Account was cleared", ctx.chatId());
                })
                .build();
    }

}
