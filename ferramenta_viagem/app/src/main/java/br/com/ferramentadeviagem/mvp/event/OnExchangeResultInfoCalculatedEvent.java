package br.com.ferramentadeviagem.mvp.event;

import br.com.ferramentadeviagem.mvp.repository.pojo.ExchangeResultInfo;

/**
 * Created by Develop on 03/11/2017.
 */

public class OnExchangeResultInfoCalculatedEvent
{
    private ExchangeResultInfo exchangeResultInfos;

    public OnExchangeResultInfoCalculatedEvent(ExchangeResultInfo exchangeResultInfos)
    {
        this.exchangeResultInfos = exchangeResultInfos;
    }

    public ExchangeResultInfo getExchangeResultInfos()
    {
        return exchangeResultInfos;
    }
}
