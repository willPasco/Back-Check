package br.com.ferramentadeviagem.mvp.repository;

/**
 * Created by Develop on 02/11/17.
 */

public interface ExchangeRatesRepository
{
    void loadExchangeRates(String currencyFrom, String[] currencyTo);
}
