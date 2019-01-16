package com.marx.wizzscraper.data;

import java.time.LocalDate;

public class DatePrice
{
	private LocalDate date;
	private int price;

	public DatePrice()
	{

	}

	public DatePrice(final LocalDate date, final int price)
	{
		this.date = date;
		this.price = price;
	}

	public LocalDate getDate()
	{
		return date;
	}

	public void setDate(final LocalDate date)
	{
		this.date = date;
	}

	public int getPrice()
	{
		return price;
	}

	public void setPrice(final int price)
	{
		this.price = price;
	}
}
