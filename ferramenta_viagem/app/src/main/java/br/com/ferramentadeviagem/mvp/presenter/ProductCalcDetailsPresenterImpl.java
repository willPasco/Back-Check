package br.com.ferramentadeviagem.mvp.presenter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import br.com.ferramentadeviagem.mvp.event.OnExchangeResultInfoCalculatedEvent;
import br.com.ferramentadeviagem.mvp.model.ProductCalcModel;
import br.com.ferramentadeviagem.mvp.model.ProductCalcModelImpl;
import br.com.ferramentadeviagem.mvp.repository.pojo.ExchangeInfo;
import br.com.ferramentadeviagem.mvp.view.ProductCalcDetailsView;

/**
 * Created by Develop on 03/11/2017.
 */

public class ProductCalcDetailsPresenterImpl implements ProductCalcDetailsPresenter
{

    private final ProductCalcDetailsView view;
    private final ProductCalcModel model;

    public ProductCalcDetailsPresenterImpl(ProductCalcDetailsView view)
    {
        this.view = view;
        model = new ProductCalcModelImpl();

    }


    @Override
    public void onLoadProductCalcValue(ExchangeInfo exchangeInfo)
    {
        this.model.doCalculateExchangeInfos(exchangeInfo);
    }

    @Override
    @Subscribe
    public void onExchangeResultInfoCalculatedEvent(OnExchangeResultInfoCalculatedEvent event)
    {
        this.view.onExchangeResultLoaded(event.getExchangeResultInfos());
    }

    @Override
    public void onStart()
    {
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop()
    {
        EventBus.getDefault().unregister(this);
    }
}
