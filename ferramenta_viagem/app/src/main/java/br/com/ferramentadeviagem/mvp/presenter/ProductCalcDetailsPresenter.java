package br.com.ferramentadeviagem.mvp.presenter;

import br.com.ferramentadeviagem.mvp.event.OnExchangeResultInfoCalculatedEvent;
import br.com.ferramentadeviagem.mvp.repository.pojo.ExchangeInfo;

/**
 * Created by Develop on 03/11/2017.
 */

public interface ProductCalcDetailsPresenter extends BasePresenter{

    void onLoadProductCalcValue(ExchangeInfo exchangeInfo);
    void onExchangeResultInfoCalculatedEvent(OnExchangeResultInfoCalculatedEvent event);

}
