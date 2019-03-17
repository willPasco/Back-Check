package br.com.ferramentadeviagem.mvp.view;

import br.com.ferramentadeviagem.models.CurrencyExchange;
import br.com.ferramentadeviagem.mvp.repository.pojo.CalculatedCurrency;

/**
 * Created by Develop on 03/11/17.
 */

public interface ProductCalcView
{
    void updateAmounts(CalculatedCurrency calculatedCurrency);

    void onCurrencyExchangeLoaded(CurrencyExchange currencyExchange);
}
