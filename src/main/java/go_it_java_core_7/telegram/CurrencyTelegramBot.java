package go_it_java_core_7.telegram;

import go_it_java_core_7.feature_currency.Currency;
import go_it_java_core_7.feature_currency.CurrencyService;
import go_it_java_core_7.feature_currency.PrivatBankCurrencyService;
import go_it_java_core_7.feature_ui.PrettyPrintCurrencyService;
import go_it_java_core_7.telegram.command.HelpCommand;
import go_it_java_core_7.telegram.command.StartCommand;
import org.telegram.telegrambots.extensions.bots.commandbot.TelegramLongPollingCommandBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class CurrencyTelegramBot extends TelegramLongPollingCommandBot {

    private CurrencyService currencyService;
    private PrettyPrintCurrencyService prettyPrintCurrencyService;

    public CurrencyTelegramBot () {
        currencyService = new PrivatBankCurrencyService();
        prettyPrintCurrencyService = new PrettyPrintCurrencyService();
        register(new StartCommand());
        register(new HelpCommand());
    }

    @Override
    public String getBotUsername() {
        return BotConstants.BOT_NAME;
    }

    @Override
    public String getBotToken() {
        return BotConstants.BOT_TOKEN;
    }

    @Override
    public void processNonCommandUpdate(Update update) {
        if (update.hasCallbackQuery()) {

            String callbackQuery = update.getCallbackQuery().getData();
            Currency currency = Currency.valueOf(callbackQuery);

            double currencyRate = currencyService.getRate(currency);
            String prettyText = prettyPrintCurrencyService.convert(currencyRate, currency);

            SendMessage responseMessage = new SendMessage();
            responseMessage.setText(prettyText);
            Long chatID = update.getCallbackQuery().getMessage().getChatId();
            System.out.println("callbackQuery = " + callbackQuery);
            responseMessage.setChatId(Long.toString(chatID));
            try {
                execute(responseMessage);
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }

        }

        if (update.hasMessage()) {
            String message = update.getMessage().getText();
            String responseText = "Ви написали: " + message;
            SendMessage sendMessage = new SendMessage();
            sendMessage.setText(responseText);
            sendMessage.setChatId(Long.toString(update.getMessage().getChatId()));
            try {
                execute(sendMessage);
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
