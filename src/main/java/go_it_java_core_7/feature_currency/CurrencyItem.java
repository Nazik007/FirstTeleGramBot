package go_it_java_core_7.feature_currency;

import lombok.Data;

@Data
public class CurrencyItem {

    private Currency ccy;
    private Currency base_ccy;
    private float buy;
    private float sale;

}
