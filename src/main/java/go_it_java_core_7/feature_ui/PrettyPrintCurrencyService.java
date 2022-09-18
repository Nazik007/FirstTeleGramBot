package go_it_java_core_7.feature_ui;

import go_it_java_core_7.feature_currency.Currency;

public class PrettyPrintCurrencyService {
    public String convert (double rate, Currency currency) {
        String template = "Курс ${currency} => UAH = ${rate}";

        float roundedRate = Math.round(rate * 100d) / 100.f;

        return template
                .replace("${currency}", currency.name())
                .replace("${rate}", Double.toString(roundedRate));
    }
}
