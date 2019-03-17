package br.com.ferramentadeviagem.mvp.event;

import br.com.ferramentadeviagem.mvp.repository.pojo.ExchangeInfo;

/**
 * Created by Develop on 03/11/17.
 */

public class OnFireCurrencyDetailsUpdateEvent
{
    private final ExchangeInfo exchangeInfo;

    public OnFireCurrencyDetailsUpdateEvent(ExchangeInfo exchangeInfo)
    {
        this.exchangeInfo = exchangeInfo;
    }

    public ExchangeInfo getExchangeInfo()
    {
        return exchangeInfo;
    }
}
