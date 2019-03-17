package br.com.ferramentadeviagem.service;

import br.com.ferramentadeviagem.models.CurrencyExchange;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Develop on 09/09/2017.
 */

interface FreeCurrencyService
{
    @GET("/latest")
    Call<CurrencyExchange> getCurrency(@Query("base") String valor, @Query("symbols") String symbols);
}
