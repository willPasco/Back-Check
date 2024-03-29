package br.com.ferramentadeviagem;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.text.DecimalFormat;

import br.com.concretesolutions.canarinho.watcher.ValorMonetarioWatcher;
import br.com.ferramentadeviagem.models.CurrencyExchange;
import br.com.ferramentadeviagem.mvp.event.OnFireCurrencyDetailsUpdateEvent;
import br.com.ferramentadeviagem.mvp.event.OnFireLoadExchangeRatesEvent;
import br.com.ferramentadeviagem.mvp.presenter.ProductCalcPresenter;
import br.com.ferramentadeviagem.mvp.presenter.ProductCalcPresenterImpl;
import br.com.ferramentadeviagem.mvp.repository.pojo.CalculatedCurrency;
import br.com.ferramentadeviagem.mvp.repository.pojo.ExchangeInfo;
import br.com.ferramentadeviagem.mvp.repository.pojo.PaymentType;
import br.com.ferramentadeviagem.mvp.repository.pojo.SituationType;
import br.com.ferramentadeviagem.mvp.view.ProductCalcView;
import studio.carbonylgroup.textfieldboxes.ExtendedEditText;

@SuppressLint({"Registered", "SetTextI18n"})
@EActivity(R.layout.activity_main)
public class ProductCalcActivity extends AppCompatActivity implements ProductCalcView
{
    private static final String TAG = "ProductCalcActivity";

    private RadioGroup.OnCheckedChangeListener onCheckedChangeListener;
    private DecimalFormat decimalFormat = new DecimalFormat("0.00");
    private PaymentType paymentType = PaymentType.NONE;
    private SituationType situationType = SituationType.NONE;

    private Typeface face;


    @ViewById(R.id.editAmount)
    ExtendedEditText editAmount;

    @ViewById(R.id.editCurrencyExchange)
    ExtendedEditText editCurrencyExchange;

    @ViewById
    TextView textCurrencySymbolFrom;

    @ViewById
    TextView textCurrencySymbolTo;

    @ViewById
    TextView textAmountFromLabel;

    @ViewById
    TextView textAmountFromValue;

    @ViewById
    TextView textAmountToValue;

    @ViewById
    Button buttonGoToDetails;

    @ViewById
    Button buttonCleanForm;

    @ViewById
    Button buttonInfoPayment;

    @ViewById
    RadioGroup radioPaymentType;

    @ViewById
    RadioGroup radioSituationType;

    private ProductCalcPresenter presenter;

    @AfterViews
    public void afterViews()
    {
        this.configTypefaces();

        String array_rate_Symbol[] = new String[2];
        array_rate_Symbol[0] = "USD $";
        array_rate_Symbol[1] = "EUR €";

        AlertDialog.Builder dialogBuilderMoeda = new AlertDialog.Builder(ProductCalcActivity.this);
        dialogBuilderMoeda.setTitle(R.string.text_local_currency)
                .setItems(array_rate_Symbol, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i)
                    {
                        if (i == 0)
                        {
                            String currencyFrom = "USD";
                            String[] currencyTo = new String[]{"BRL","EUR"};

                            ProductCalcActivity.this.setSymbols("$");
                            ProductCalcActivity.this.fireLoadExchangeRatesEvent(currencyFrom, currencyTo);
                        } else if (i == 1)
                        {
                            String currencyFrom = "EUR";
                            String[] currencyTo = new String[]{"BRL","USD"};

                            ProductCalcActivity.this.setSymbols("€");
                            ProductCalcActivity.this.fireLoadExchangeRatesEvent(currencyFrom, currencyTo);
                        }
                    }
                });


        textAmountToValue.addTextChangedListener(new ValorMonetarioWatcher());
        textAmountFromValue.addTextChangedListener(new ValorMonetarioWatcher());

        editCurrencyExchange.addTextChangedListener((new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                fireCurrencyDetailsUpdateEvent();
            }

            @Override
            public void afterTextChanged(Editable s)
            {

            }
        }));

        editAmount.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {
                fireCurrencyDetailsUpdateEvent();
            }

            @Override
            public void afterTextChanged(Editable editable)
            {

            }
        });

        radioPaymentType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {

                RadioButton button = group.findViewById(checkedId);
                if (button != null)
                {
                    handlePaymentType(button.getId());
                    fireCurrencyDetailsUpdateEvent();
                }
            }
        });

        onCheckedChangeListener = new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {
                RadioButton button = group.findViewById(checkedId);

                if (button != null)
                {
                    if (getDoubleValueFrom(editAmount) < 500.00D)
                    {
                        String dialogTitle = getString(R.string.text_warn);
                        String message = getString(R.string.text_min_value_required);
                        showDialog(dialogTitle, message);

                    }
                    handleSituationType(button.getId());
                    fireCurrencyDetailsUpdateEvent();
                }
            }
        };

        radioSituationType.setOnCheckedChangeListener(onCheckedChangeListener);


        buttonInfoPayment.setOnClickListener(getOnClickListener(createDialog(getString(R.string.text_information), getString(R.string.card_payment_info))));

        buttonGoToDetails.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String currencyLocalSymbol = textCurrencySymbolFrom.getText().toString();
                String currencyForeignSymbol = textCurrencySymbolTo.getText().toString();

                ProductCalcDetailsActivity.start(ProductCalcActivity.this, currencyLocalSymbol, currencyForeignSymbol, getExchangeInfos());
            }
        });

        buttonCleanForm.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                editAmount.setText("0,00");
                textAmountToValue.setText("0,00");
                textAmountFromValue.setText("0,00");
                paymentType = PaymentType.NONE;
                situationType = SituationType.NONE;

                radioPaymentType.clearCheck();
                radioSituationType.setOnCheckedChangeListener(null);
                radioSituationType.clearCheck();
                radioSituationType.setOnCheckedChangeListener(onCheckedChangeListener);

                fireCurrencyDetailsUpdateEvent();
            }
        });

        AlertDialog alertDialog = dialogBuilderMoeda.create();
        View.OnClickListener onClickListener = this.getOnClickListener(alertDialog);
        textCurrencySymbolFrom.setOnClickListener(onClickListener);

        this.presenter = new ProductCalcPresenterImpl(this);
        this.fireLoadExchangeRatesEvent("USD", new String[]{"BRL,EUR"});
    }

    private void handleSituationType(int id)
    {
        if (id == R.id.radioDeclared)
        {
            situationType = SituationType.DECLARED;
        } else if (id == R.id.radio_nDeclared)
        {
            situationType = SituationType.NOT_DECLARED;
        } else if (id == R.id.radio_fined)
        {
            situationType = SituationType.FINED;
        }

    }

    private void fireLoadExchangeRatesEvent(String currencyFrom, String[] currencyTo)
    {
        this.presenter.onFireLoadExchangeRatesEvent(new OnFireLoadExchangeRatesEvent(currencyFrom, currencyTo));
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        this.presenter.onStart();
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        this.presenter.onStop();
    }

    @Override
    public void updateAmounts(CalculatedCurrency calculatedCurrency)
    {
        String amountTo = decimalFormat.format(calculatedCurrency.getAmountTo());
        String amountFrom = decimalFormat.format(calculatedCurrency.getAmountFrom());

        textAmountToValue.setText(amountTo);
        textAmountFromValue.setText(amountFrom);

        Log.d(TAG, String.format("USD: %f, BRL: %f", calculatedCurrency.getAmountFrom(), calculatedCurrency.getAmountTo()));
    }

    @Override
    public void onCurrencyExchangeLoaded(CurrencyExchange currencyExchange)
    {
        editCurrencyExchange.setText(decimalFormat.format(currencyExchange.getRates().getBRL()));
    }

    private ExchangeInfo getExchangeInfos()
    {

        ExchangeInfo exchangeInfo = new ExchangeInfo();

        exchangeInfo.setExchangeRate(this.getDoubleValueFrom(editCurrencyExchange));
        exchangeInfo.setAmountFrom(this.getDoubleValueFrom(editAmount));
        exchangeInfo.setCurrencyFrom(this.textCurrencySymbolFrom.getText().toString());
        exchangeInfo.setCurrencyTo(this.textCurrencySymbolTo.getText().toString());
        exchangeInfo.setPaymentType(paymentType);
        exchangeInfo.setSituationType(situationType);

        return exchangeInfo;

    }

    private void fireCurrencyDetailsUpdateEvent()
    {
        ExchangeInfo exchangeInfo = getExchangeInfos();

        OnFireCurrencyDetailsUpdateEvent event = new OnFireCurrencyDetailsUpdateEvent(exchangeInfo);
        this.presenter.onFireCurrencyDetailsUpdate(event);
    }

    private void configTypefaces()
    {
        setTypeface(R.id.money);
        setTypeface(R.id.card);
        setTypeface(R.id.radioDeclared);
        setTypeface(R.id.radio_nDeclared);
        setTypeface(R.id.radio_fined);
        setTypeface(R.id.editCurrencyExchange);
        setTypeface(R.id.editAmount);
    }

    private View.OnClickListener getOnClickListener(final Dialog dialog)
    {
        return new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                dialog.show();
            }
        };
    }

    private void setTypeface(@IdRes int resid)
    {
        TextView textView = findViewById(resid);

        if (face == null)
        {
            face = Typeface.createFromAsset(getAssets(), "fonts/roboto_thin.ttf");
        }
        textView.setTypeface(face);
    }

    private double getDoubleValueFrom(@NonNull final EditText editText)
    {
        String editTextValue = editText.getText().toString();
        return this.getDoubleValueFrom(editTextValue);
    }

    private double getDoubleValueFrom(@NonNull final String textValue)
    {
        double result = 0.0d;

        if (!TextUtils.isEmpty(textValue.trim()))
        {
            result = Double.parseDouble(textValue.replace(",", "."));

        }

        return result;
    }

    private Dialog createDialog(String title, String message)
    {
        final AlertDialog.Builder builder = new AlertDialog.Builder(ProductCalcActivity.this);
        builder.setMessage(message);
        builder.setTitle(title);

        return builder.create();
    }

    private void showDialog(String dialogTitle, String message)
    {
        createDialog(dialogTitle, message).show();
    }

    private void setSymbols(String symbol)
    {
        textCurrencySymbolFrom.setText(symbol);
        editAmount.setPrefix(symbol + " ");
        textAmountFromLabel.setText("Total em " + symbol);
    }

    private void handlePaymentType(int id)
    {

        if (id == R.id.money)
        {
            paymentType = PaymentType.MONEY;
        } else
        {
            paymentType = PaymentType.DEBIT_CREDIT_CARD;
        }

    }

    public void hideKeyboard(View view){
        InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null)
        {
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(),0);
        }
    }
}
