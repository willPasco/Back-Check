package br.com.ferramentadeviagem.mvp.repository;

import android.support.annotation.NonNull;
import android.util.Log;

import org.greenrobot.eventbus.EventBus;

import br.com.ferramentadeviagem.models.CurrencyExchange;
import br.com.ferramentadeviagem.mvp.event.OnLoadExchangeRatesSuccessEvent;
import br.com.ferramentadeviagem.service.RetrofitService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Develop on 02/11/17.
 */

public class ExchangeRatesRepositoryImpl implements ExchangeRatesRepository
{

    private final RetrofitService retrofitService;

    public ExchangeRatesRepositoryImpl()
    {
        this.retrofitService = new RetrofitService("https://api.exchangeratesapi.io/");
    }

    @Override
    public void loadExchangeRates(String currencyFrom, String[] currencyTo)
    {
        String currenciesString = this.getCurrenciesString(currencyTo);
        Callback<CurrencyExchange> callback = this.getRetrofitCallback();
        this.retrofitService.getCurrency(currencyFrom, currenciesString, callback);
    }


    @NonNull
    private Callback<CurrencyExchange> getRetrofitCallback()
    {
        return new Callback<CurrencyExchange>()
        {
            @Override
            public void onResponse(@NonNull Call<CurrencyExchange> call, @NonNull Response<CurrencyExchange> response)
            {
                this.handleResponse(response);
            }

            @Override
            public void onFailure(@NonNull Call<CurrencyExchange> call,@NonNull Throwable t)
            {
                Log.e("TAG", "Error loading exchange rates.", t);
            }

            private void handleResponse(Response<CurrencyExchange> response)
            {
                if (response.isSuccessful())
                {
                    this.handleSuccessResponse(response);
                } else
                {
                    Log.e("TAG", "Error loading exchange rates. " + response.message());
                }
            }

            private void handleSuccessResponse(Response<CurrencyExchange> response)
            {
                CurrencyExchange currencyExchange = response.body();
                sendOnLoadExchangeRatesSuccessEvent(currencyExchange);
            }
        };
    }

    private void sendOnLoadExchangeRatesSuccessEvent(CurrencyExchange currencyExchange)
    {
        EventBus.getDefault().post(new OnLoadExchangeRatesSuccessEvent(currencyExchange));
    }

    private String getCurrenciesString(String[] currencyTo)
    {
        StringBuilder result = new StringBuilder();

        for (int index = 0; index < currencyTo.length; index++)
        {
            String currency = currencyTo[index];
            result.append(currency);

            if (this.isNotTheLastIndex(currencyTo, index))
            {
                result.append(",");
            }
        }

        return result.toString();
    }

    private boolean isNotTheLastIndex(String[] currencyTo, int index)
    {
        return index < currencyTo.length - 1;
    }
}
