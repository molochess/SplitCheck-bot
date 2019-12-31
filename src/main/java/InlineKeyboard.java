import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import java.util.ArrayList;
import java.util.List;

public class InlineKeyboard {
    InlineKeyboardMarkup keyboard = new InlineKeyboardMarkup();

    public SendMessage withButtonsRegistration() {
        List<List<InlineKeyboardButton>> row = new ArrayList<>();
        List<InlineKeyboardButton> rowInline = new ArrayList<>();
        InlineKeyboardButton buttonJoin = new InlineKeyboardButton().setText("Join");
        rowInline.add(buttonJoin);
        row.add(rowInline);
        this.keyboard.setKeyboard(row);
        return new SendMessage().setReplyMarkup(this.keyboard);
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
