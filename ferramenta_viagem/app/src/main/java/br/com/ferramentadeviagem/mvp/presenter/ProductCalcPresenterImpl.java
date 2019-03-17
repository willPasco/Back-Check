package br.com.ferramentadeviagem.mvp.presenter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import br.com.ferramentadeviagem.mvp.event.OnCurrencyCalculatedEvent;
import br.com.ferramentadeviagem.mvp.event.OnFireCurrencyDetailsUpdateEvent;
import br.com.ferramentadeviagem.mvp.event.OnFireLoadExchangeRatesEvent;
import br.com.ferramentadeviagem.mvp.event.OnLoadExchangeRatesSuccessEvent;
import br.com.ferramentadeviagem.mvp.model.ProductCalcModel;
import br.com.ferramentadeviagem.mvp.model.ProductCalcModelImpl;
import br.com.ferramentadeviagem.mvp.repository.ExchangeRatesRepository;
import br.com.ferramentadeviagem.mvp.repository.ExchangeRatesRepositoryImpl;
import br.com.ferramentadeviagem.mvp.view.ProductCalcView;

/**
 * Created by Develop on 03/11/17.
 */

public class ProductCalcPresenterImpl implements ProductCalcPresenter
{
    private final ExchangeRatesRepository repository;
    private final ProductCalcModel model;
    private final ProductCalcView view;

    public ProductCalcPresenterImpl(ProductCalcView productCalcView)
    {
        this.model = new ProductCalcModelImpl();
        this.repository = new ExchangeRatesRepositoryImpl();
        this.view = productCalcView;
    }

    @Override
    public void onFireLoadExchangeRatesEvent(OnFireLoadExchangeRatesEvent event)
    {
        repository.loadExchangeRates(event.getCurrencyFrom(), event.getCurrenciesTo());
    }

    @Override
    @Subscribe
    public void onLoadExchangeRatesSuccessEvent(OnLoadExchangeRatesSuccessEvent event)
    {
        this.view.onCurrencyExchangeLoaded(event.getCurrencyExchange());
    }

    @Override
    @Subscribe
    public void onFireCurrencyDetailsUpdate(OnFireCurrencyDetailsUpdateEvent event)
    {
        this.model.doCalculateCurrencyDetails(event.getExchangeInfo());
    }

    @Override
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCurrencyCalculated(OnCurrencyCalculatedEvent event)
    {
        this.view.updateAmounts(event.getCalculatedCurrency());
    }

    @Override
    public void onStart() {
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
    }
}
