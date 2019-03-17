package br.com.ferramentadeviagem.mvp.event;

import br.com.ferramentadeviagem.models.CurrencyExchange;

/**
 * Created by Develop on 03/11/17.
 */

public class OnLoadExchangeRatesSuccessEvent
{
    private final CurrencyExchange currencyExchange;

    public OnLoadExchangeRatesSuccessEvent(CurrencyExchange currencyExchange)
    {
        this.currencyExchange = currencyExchange;
    }

    public CurrencyExchange getCurrencyExchange()
    {
        return currencyExchange;
    }
}
