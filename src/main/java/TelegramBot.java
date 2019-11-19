import org.telegram.abilitybots.api.bot.AbilityBot;
import org.telegram.abilitybots.api.objects.Ability;
import org.telegram.abilitybots.api.objects.Locality;
import org.telegram.abilitybots.api.objects.Privacy;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.ApiContext;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

public class TelegramBot extends AbilityBot {
    private static final String BOT_NAME = "@boleslavovich_bot";
    private static final String BOT_TOKEN = System.getenv("TOKEN");
    TelegramBot(DefaultBotOptions botOptions) {
        super(BOT_TOKEN, BOT_NAME, botOptions);
    }

 /*   private ReplyKeyboard getKeyboard(){
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setOneTimeKeyboard(true);

        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow keyboardFirstRow = new KeyboardRow();
        keyboardFirstRow.add("Fine");
        keyboardFirstRow.add("Awful");
        KeyboardRow keyboardSecondRow = new KeyboardRow();
        keyboardSecondRow.add("Why me?");
        keyboardSecondRow.add("._.");
        keyboard.add(keyboardFirstRow);
        keyboard.add(keyboardSecondRow);
        replyKeyboardMarkup.setKeyboard(keyboard);
        return replyKeyboardMarkup;
    }*/

   public static void main(String[] args){

    ApiContextInitializer.init();
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
    try {
        DefaultBotOptions botOptions = ApiContext.getInstance(DefaultBotOptions.class);
        telegramBotsApi.registerBot(new TelegramBot(botOptions));
    } catch (TelegramApiException e){
        e.printStackTrace();
    }
}
   /* @Override
    public void onUpdateReceived(Update update) {
        Message message = update.getMessage();
        SendMessage sendMessage = new SendMessage(message.getChatId(), message.getText());

        if (message.getText().contains("Hello") || message.getText().contains("Hi")){
            sendMessage.setText("Hi, buddy!");
        } else if (update.getMessage().getText().contains("How")&&update.getMessage().getText().contains("you")){
            sendMessage.setText("Fine... What about you?");
        }
            try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

    }*/

   public Ability hello() {
       return Ability
               .builder()
               .name("pain")
               .info("painful")
               .input(0)
               .locality(Locality.ALL)
               .privacy(Privacy.PUBLIC)
               .action( ctx -> silent.send("arrrrr", ctx.chatId()))
               .post(ctx->silent.send("hate everything", ctx.chatId()))
               .build();
   }

    @Override
    public String getBotUsername() {
        return BOT_NAME;
    }

    @Override
    public int creatorId() {
        return 0;
    }

    @Override
    public String getBotToken() {
        return BOT_TOKEN;
    }

}
