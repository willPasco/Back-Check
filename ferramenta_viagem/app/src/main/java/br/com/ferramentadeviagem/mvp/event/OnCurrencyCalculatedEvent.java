package br.com.ferramentadeviagem.mvp.event;

import br.com.ferramentadeviagem.mvp.repository.pojo.CalculatedCurrency;

/**
 * Created by Develop on 03/11/17.
 */

public class OnCurrencyCalculatedEvent
{
    private final CalculatedCurrency calculatedCurrency;

    public OnCurrencyCalculatedEvent(CalculatedCurrency calculatedCurrency)
    {
        this.calculatedCurrency = calculatedCurrency;
    }

    public CalculatedCurrency getCalculatedCurrency()
    {
        return calculatedCurrency;
    }
}
