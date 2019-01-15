package com.marx.wizzscraper.data;

public class Price
{
	private String amount;

	private String currencyCode;

	public String getAmount ()
	{
		return amount;
	}

	public void setAmount (String amount)
	{
		this.amount = amount;
	}

	public String getCurrencyCode ()
	{
		return currencyCode;
	}

	public void setCurrencyCode (String currencyCode)
	{
		this.currencyCode = currencyCode;
	}
}
