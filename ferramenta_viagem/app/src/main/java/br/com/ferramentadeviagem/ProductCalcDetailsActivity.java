package br.com.ferramentadeviagem;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import java.util.Locale;

import br.com.ferramentadeviagem.mvp.presenter.ProductCalcDetailsPresenter;
import br.com.ferramentadeviagem.mvp.presenter.ProductCalcDetailsPresenterImpl;
import br.com.ferramentadeviagem.mvp.repository.pojo.ExchangeInfo;
import br.com.ferramentadeviagem.mvp.repository.pojo.ExchangeResultInfo;
import br.com.ferramentadeviagem.mvp.repository.pojo.PaymentType;
import br.com.ferramentadeviagem.mvp.repository.pojo.SituationType;
import br.com.ferramentadeviagem.mvp.view.ProductCalcDetailsView;
@SuppressLint({"Registered", "SetTextI18n"})
@EActivity(R.layout.activity_product_calc_details)
public class ProductCalcDetailsActivity extends AppCompatActivity implements ProductCalcDetailsView
{

    @Extra
    String currencyValue;

    @Extra
    String currencyConverted;

    @Extra
    ExchangeInfo exchangeInfo;

    @ViewById
    TextView labelExchangeRate;

    @ViewById
    TextView textExchangeRate;

    @ViewById
    TextView labelTitleDetailsFrom;

    @ViewById
    TextView textAmountFrom;

    @ViewById
    TextView labelPaymentTypeFrom;

    @ViewById
    TextView textPaymentTypeFrom;

    @ViewById
    TextView labelSituationTypeFrom;

    @ViewById
    TextView textSituationTypeFrom;

    @ViewById
    TextView textTotalAmountFrom;

    @ViewById
    TextView labelTitleDetailsTo;

    @ViewById
    TextView textAmountTo;

    @ViewById
    TextView labelPaymentTypeTo;

    @ViewById
    TextView textPaymentTypeTo;

    @ViewById
    TextView labelSituationTypeTo;

    @ViewById
    TextView textSituationTypeTo;

    @ViewById
    TextView textTotalAmountTo;


    private ProductCalcDetailsPresenter presenter;

    public static void start(Context context, String currencyValue, String currencyConverted, ExchangeInfo exchangeInfo)
    {
        ProductCalcDetailsActivity_.intent(context)
                .currencyConverted(currencyConverted)
                .currencyValue(currencyValue)
                .exchangeInfo(exchangeInfo)
                .start();
    }

    @AfterViews
    public void afterViews()
    {

        this.presenter = new ProductCalcDetailsPresenterImpl(this);
    }


    @Override
    public void onExchangeResultLoaded(ExchangeResultInfo exchangeResultInfos)
    {
        String currencySymbolFrom = exchangeResultInfos.getCurrencyFrom();
        String currencySymbolTo = exchangeResultInfos.getCurrencyTo();

        setLabels(currencySymbolFrom, currencySymbolTo);
        handlePaymentType(exchangeResultInfos.getPaymentType());
        handleSituationType(exchangeResultInfos.getSituationType());


        String amountFrom = format(exchangeResultInfos.getAmountFrom());
        String exchangeRate = format(exchangeResultInfos.getExchangeRate());
        String paymentTaxAmountFrom = format(exchangeResultInfos.getPaymentTaxAmountFrom());
        String situationTaxAmountFrom = format(exchangeResultInfos.getSituationTaxAmountFrom());
        String totalAmountFrom = format(exchangeResultInfos.getCalculatedCurrency().getAmountFrom());

        String amountTo = format(exchangeResultInfos.getAmountTo());
        String paymentTaxAmountTo = format(exchangeResultInfos.getPaymentTaxAmountTo());
        String situationTaxAmountTo = format(exchangeResultInfos.getSituationTaxAmountTo());
        String totalAmountTo = format(exchangeResultInfos.getCalculatedCurrency().getAmountTo());

        this.textExchangeRate.setText(currencySymbolTo + " " + exchangeRate);
        this.textAmountFrom.setText(currencySymbolFrom + " " + amountFrom);
        this.textPaymentTypeFrom.setText(currencySymbolFrom + " " + paymentTaxAmountFrom);
        this.textSituationTypeFrom.setText(currencySymbolFrom + " " + situationTaxAmountFrom);
        this.textTotalAmountFrom.setText(currencySymbolFrom + " " + totalAmountFrom);
        this.textAmountTo.setText(currencySymbolTo + " " + amountTo);
        this.textPaymentTypeTo.setText(currencySymbolTo + " " + paymentTaxAmountTo);
        this.textSituationTypeTo.setText(currencySymbolTo + " " + situationTaxAmountTo);
        this.textTotalAmountTo.setText(currencySymbolTo + " " + totalAmountTo);

    }

    private String format(Double value)
    {
        if (value != null)
        {
            String aux = String.format(Locale.US, "%.2f", value);

            aux = aux.replace(".", ",");
            StringBuilder stringBuilder = new StringBuilder(aux);
            if (aux.length() <= 9 && aux.length() > 6)
                stringBuilder.insert(aux.length() - 6, ".");
            else if (aux.length() <= 12 && aux.length() > 9)
            {
                stringBuilder.insert(aux.length() - 6, ".");
                stringBuilder.insert(aux.length() - 9, ".");
            } else if (aux.length() <= 15 && aux.length() > 12)
            {
                stringBuilder.insert(aux.length() - 6, ".");
                stringBuilder.insert(aux.length() - 9, ".");
                stringBuilder.insert(aux.length() - 12, ".");

            }

            return stringBuilder.toString();
        } else
            return "0,00";
    }

    private void handleSituationType(SituationType situationType)
    {
        if (situationType == SituationType.DECLARED)
        {
            this.labelSituationTypeFrom.setText(R.string.text_situation_declared);
            this.labelSituationTypeTo.setText(R.string.text_situation_declared);
        } else if (situationType == SituationType.NOT_DECLARED)
        {
            this.labelSituationTypeFrom.setText(R.string.text_situation_not_declared);
            this.labelSituationTypeTo.setText(R.string.text_situation_not_declared);
        } else if (situationType == SituationType.FINED)
        {
            this.labelSituationTypeFrom.setText(R.string.text_situation_fined);
            this.labelSituationTypeTo.setText(R.string.text_situation_fined);
        }

    }

    private void handlePaymentType(PaymentType paymentType)
    {
        if (paymentType == PaymentType.MONEY)
        {
            this.labelPaymentTypeFrom.setText(R.string.text_money_payment_type);
            this.labelPaymentTypeTo.setText(R.string.text_money_payment_type);
        } else if (paymentType == PaymentType.DEBIT_CREDIT_CARD)
        {
            this.labelPaymentTypeFrom.setText(R.string.text_card_payment_type);
            this.labelPaymentTypeTo.setText(R.string.text_card_payment_type);
        }
    }

    private void setLabels(String currencySymbolFrom, String currencySymbolTo)
    {
        this.labelExchangeRate.setText("De (" + currencySymbolFrom + ") Para (" + currencySymbolTo + ")");
        this.labelTitleDetailsFrom.setText("Resumo do custo em " + currencySymbolFrom);
        this.labelTitleDetailsTo.setText("Resumo do Custo em " + currencySymbolTo);
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        this.presenter.onStart();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        this.presenter.onLoadProductCalcValue(exchangeInfo);
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        this.presenter.onStop();
    }

}
