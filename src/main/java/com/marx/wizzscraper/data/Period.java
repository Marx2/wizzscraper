package com.marx.wizzscraper.data;

import java.time.LocalDate;

public class Period implements Comparable<Period>
{
	private LocalDate start;
	private LocalDate end;
	private int startPrice;
	private int endPrice;

	public Period(LocalDate start, LocalDate end, int startPrice, int endPrice)
	{
		this.start = start;
		this.end = end;
		this.startPrice = startPrice;
		this.endPrice = endPrice;
	}

	public LocalDate getStart()
	{
		return start;
	}

	public void setStart(LocalDate start)
	{
		this.start = start;
	}

	public LocalDate getEnd()
	{
		return end;
	}

	public void setEnd(LocalDate end)
	{
		this.end = end;
	}

	public int getFullPrice()
	{
		return startPrice + endPrice;
	}

	@Override
	public int compareTo(Period o)
	{
		return (startPrice + endPrice) - (o.getStartPrice() + o.getEndPrice());
	}

	public int getStartPrice()
	{
		return startPrice;
	}

	public void setStartPrice(int startPrice)
	{
		this.startPrice = startPrice;
	}

	public int getEndPrice()
	{
		return endPrice;
	}

	public void setEndPrice(int endPrice)
	{
		this.endPrice = endPrice;
	}
}
