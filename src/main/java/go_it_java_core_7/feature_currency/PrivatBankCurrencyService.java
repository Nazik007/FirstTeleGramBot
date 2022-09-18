package go_it_java_core_7.feature_currency;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

public class PrivatBankCurrencyService implements CurrencyService{

    @Override
    public double getRate(Currency currency) {
        String url = "https://api.privatbank.ua/p24api/pubinfo?json&exchange&coursid=5";
        String json = null;
        try {
            json = Jsoup
                    .connect(url)
                    .ignoreContentType(true)
                    .get()
                    .body()
                    .text();
        } catch (IOException e) {
            e.printStackTrace();
            throw new IllegalStateException("Can't connect to Privat API");
        }

        //Covert json to Java Object
        Type typeToken = TypeToken
                .getParameterized(List.class, CurrencyItem.class )
                .getType();
        List<CurrencyItem> currencyItems = new Gson().fromJson(json,typeToken);

        //Find UAH/USD

        return currencyItems.stream()
                .filter(it -> it.getCcy() == currency)
                .filter(it -> it.getBase_ccy() == Currency.UAH)
                .map(it -> it.getBuy())
                .findFirst()
                .orElseThrow();
    }
}
