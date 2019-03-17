package br.com.ferramentadeviagem.mvp.event;

/**
 * Created by Develop on 03/11/17.
 */

public class OnFireLoadExchangeRatesEvent
{
    private final String currencyFrom;
    private final String[] currenciesTo;

    public OnFireLoadExchangeRatesEvent(String currencyFrom, String[] currenciesTo)
    {
        this.currencyFrom = currencyFrom;
        this.currenciesTo = currenciesTo;
    }

    public String getCurrencyFrom()
    {
        return currencyFrom;
    }

    public String[] getCurrenciesTo()
    {
        return currenciesTo;
    }
}
