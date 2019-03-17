package br.com.ferramentadeviagem.mvp.model;

import org.greenrobot.eventbus.EventBus;

import br.com.ferramentadeviagem.mvp.event.OnCurrencyCalculatedEvent;
import br.com.ferramentadeviagem.mvp.event.OnExchangeResultInfoCalculatedEvent;
import br.com.ferramentadeviagem.mvp.repository.pojo.CalculatedCurrency;
import br.com.ferramentadeviagem.mvp.repository.pojo.ExchangeInfo;
import br.com.ferramentadeviagem.mvp.repository.pojo.ExchangeResultInfo;
import br.com.ferramentadeviagem.mvp.repository.pojo.PaymentType;
import br.com.ferramentadeviagem.mvp.repository.pojo.SituationType;

/**
 * Created by Develop on 03/11/17.
 */

public class ProductCalcModelImpl implements ProductCalcModel
{

    private static final double AMOUNT_LIMIT_BEFORE_NEEDS_PAY_TAXES = 500.00D;
    private static final double TAX_IOF_MONEY = 1.32;
    private static final double TAX_IOF_DEBIT_CREDIT_CARD = 6.34;

    @Override
    public void doCalculateExchangeInfos(ExchangeInfo exchangeInfo)
    {
        ExchangeResultInfo exchangeResultInfos = new ExchangeResultInfo();
        exchangeResultInfos.setCalculatedCurrency(handleCurrencyCalculated(exchangeInfo));

        double exchangeRate = exchangeInfo.getExchangeRate();
        double amountFrom = exchangeInfo.getAmountFrom();
        PaymentType paymentType = exchangeInfo.getPaymentType();
        SituationType situationType = exchangeInfo.getSituationType();

        double situationTypeTaxFrom = this.calculateSituationType(amountFrom, situationType);
        double situationTypeTaxTo = situationTypeTaxFrom * exchangeRate;
        double paymentTypeTaxFrom = this.calculatePaymentType(amountFrom, paymentType);
        double paymentTypeTaxTo = paymentTypeTaxFrom * exchangeRate;

        exchangeResultInfos.setPaymentTaxAmountFrom(paymentTypeTaxFrom);
        exchangeResultInfos.setPaymentTaxAmountTo(paymentTypeTaxTo);

        exchangeResultInfos.setSituationTaxAmountFrom(situationTypeTaxFrom);
        exchangeResultInfos.setSituationTaxAmountTo(situationTypeTaxTo);

        exchangeResultInfos.setCurrencyFrom(exchangeInfo.getCurrencyFrom());
        exchangeResultInfos.setCurrencyTo(exchangeInfo.getCurrencyTo());
        exchangeResultInfos.setExchangeRate(exchangeRate);
        exchangeResultInfos.setAmountTo(amountFrom * exchangeRate);
        exchangeResultInfos.setAmountFrom(amountFrom);
        exchangeResultInfos.setPaymentType(paymentType);
        exchangeResultInfos.setSituationType(situationType);

        this.sendOnExchangeResultInfoCalculatedEvent(exchangeResultInfos);
    }

    @Override
    public void doCalculateCurrencyDetails(ExchangeInfo exchangeInfo)
    {
        CalculatedCurrency calculatedCurrency = handleCurrencyCalculated(exchangeInfo);

        this.sendOnCurrencyCalculatedEvent(calculatedCurrency);
    }

    private CalculatedCurrency handleCurrencyCalculated(ExchangeInfo exchangeInfo)
    {

        double exchangeRate = exchangeInfo.getExchangeRate();
        double amountFrom = exchangeInfo.getAmountFrom();
        PaymentType paymentType = exchangeInfo.getPaymentType();
        SituationType situationType = exchangeInfo.getSituationType();

        amountFrom += (this.calculateSituationType(amountFrom, situationType) + this.calculatePaymentType(amountFrom, paymentType));

        double amountTo = amountFrom * exchangeRate;

        CalculatedCurrency calculatedCurrency = new CalculatedCurrency();
        calculatedCurrency.setAmountFrom(amountFrom);
        calculatedCurrency.setAmountTo(amountTo);

        return calculatedCurrency;

    }

    private double calculateSituationType(final double amount, SituationType situationType)
    {
        double situationTaxResult = 0.00d;
        if (amount > AMOUNT_LIMIT_BEFORE_NEEDS_PAY_TAXES)
        {
            switch (situationType)
            {
                case DECLARED:
                {
                    situationTaxResult = this.calculatePlusOnDeclaredSituation(amount);
                    break;
                }
                case FINED:
                {
                    situationTaxResult = this.calculatePlusOnFinedSituation(amount);
                    break;
                }
                case NOT_DECLARED:
                {
                }
                case NONE:
                {
                }
                default:
                {
                }
            }
        }
        return situationTaxResult;
    }

    private double calculatePaymentType(final double amount, PaymentType paymentType)
    {
        double paymentTaxResult = 0.00d;
        switch (paymentType)
        {
            case MONEY:
            {
                paymentTaxResult += this.calculatePlusMoneyIof(amount);
                break;
            }
            case DEBIT_CREDIT_CARD:
            {
                paymentTaxResult += this.calculatePlusDebitCreditCardIof(amount);
                break;
            }
            case NONE:
            {
                break;
            }
        }
        return paymentTaxResult;
    }

    private double calculatePlusDebitCreditCardIof(double amountFrom)
    {
        return (amountFrom * TAX_IOF_DEBIT_CREDIT_CARD) / 100;
    }

    private double calculatePlusMoneyIof(double amountFrom)
    {
        return (amountFrom * TAX_IOF_MONEY) / 100;
    }

    private double calculatePlusOnFinedSituation(double amountFrom)
    {
        return ((amountFrom - AMOUNT_LIMIT_BEFORE_NEEDS_PAY_TAXES) * 100) / 100;
    }

    private double calculatePlusOnDeclaredSituation(double amountFrom)
    {
        return (amountFrom - AMOUNT_LIMIT_BEFORE_NEEDS_PAY_TAXES) * 50 / 100;
    }

    private void sendOnCurrencyCalculatedEvent(CalculatedCurrency calculatedCurrency)
    {
        EventBus.getDefault().post(new OnCurrencyCalculatedEvent(calculatedCurrency));
    }

    private void sendOnExchangeResultInfoCalculatedEvent(ExchangeResultInfo exchangeResultInfos)
    {
        EventBus.getDefault().post(new OnExchangeResultInfoCalculatedEvent(exchangeResultInfos));
    }
}
