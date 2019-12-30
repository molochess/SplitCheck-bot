import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import java.util.ArrayList;
import java.util.List;

public class InlineKeyboard {
    public static SendMessage withButtonsRegistration(Long chatId) {
        InlineKeyboardMarkup keyboard = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> row = new ArrayList<>();
        List<InlineKeyboardButton> rowInline = new ArrayList<>();
        InlineKeyboardButton buttonJoin = new InlineKeyboardButton().setText("Присоединиться");
        rowInline.add(buttonJoin);
        row.add(rowInline);
        keyboard.setKeyboard(row);
        return new SendMessage().setChatId(chatId).setReplyMarkup(keyboard).setText("arrr");
    }
 /*   public static void withButtonsAddTransaction() {
//         сделать командой
        InlineKeyboardMarkup inlineKeyboard = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> row = new ArrayList<>();
        List<InlineKeyboardButton> rowInline = new ArrayList<>();
        rowInline.add(new InlineKeyboardButton().setText("Добавить покупку").setCallbackData(""));
        rowInline.add(new InlineKeyboardButton().setText("Добавить платеж").setCallbackData(""));
        row.add(rowInline);
        inlineKeyboard.setKeyboard(row);
    }*/
}
