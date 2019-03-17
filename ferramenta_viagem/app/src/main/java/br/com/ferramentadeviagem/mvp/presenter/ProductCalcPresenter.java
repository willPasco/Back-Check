package br.com.ferramentadeviagem.mvp.presenter;

import br.com.ferramentadeviagem.mvp.event.OnCurrencyCalculatedEvent;
import br.com.ferramentadeviagem.mvp.event.OnFireCurrencyDetailsUpdateEvent;
import br.com.ferramentadeviagem.mvp.event.OnFireLoadExchangeRatesEvent;
import br.com.ferramentadeviagem.mvp.event.OnLoadExchangeRatesSuccessEvent;

/**
 * Created by Develop on 03/11/17.
 */

public interface ProductCalcPresenter extends BasePresenter
{
    void onFireLoadExchangeRatesEvent(OnFireLoadExchangeRatesEvent event);
    void onLoadExchangeRatesSuccessEvent(OnLoadExchangeRatesSuccessEvent event);
    void onFireCurrencyDetailsUpdate(OnFireCurrencyDetailsUpdateEvent event);
    void onCurrencyCalculated(OnCurrencyCalculatedEvent event);
}
