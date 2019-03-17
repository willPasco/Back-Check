package br.com.ferramentadeviagem.service;

import br.com.ferramentadeviagem.models.CurrencyExchange;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitService
{
    private final Retrofit retrofit;

    public RetrofitService(String baseURL)
    {
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .addInterceptor(getLoggingInterceptor()).build();

        Retrofit.Builder retroBuilder = new Retrofit.Builder()
                .baseUrl(baseURL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient);

        this.retrofit = retroBuilder.build();
    }

    public void getCurrency(String currencyLocal, String currencyForeign, Callback<CurrencyExchange> callback)
    {
        FreeCurrencyService currency = retrofit.create(FreeCurrencyService.class);
        Call<CurrencyExchange> call = currency.getCurrency(currencyLocal, currencyForeign);

        call.enqueue(callback);
    }

    private Interceptor getLoggingInterceptor()
    {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return interceptor;
    }
}
