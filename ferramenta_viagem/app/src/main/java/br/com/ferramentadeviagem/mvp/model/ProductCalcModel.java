package br.com.ferramentadeviagem.mvp.model;

import br.com.ferramentadeviagem.mvp.repository.pojo.ExchangeInfo;

/**
 * Created by Develop on 03/11/17.
 */

public interface ProductCalcModel
{
    void doCalculateExchangeInfos(ExchangeInfo exchangeInfo);
    void doCalculateCurrencyDetails(ExchangeInfo exchangeInfo);
}
