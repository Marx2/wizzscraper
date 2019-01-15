package com.marx.wizzscraper.data;

import com.marx.wizzscraper.data.Price;

public class OutboundFlights
{
	private String hasMacFlight;

	private String arrivalStation;

	private Price price;

	private String departureStation;

	private String priceType;

	private String classOfService;

	private String date;

	public String getHasMacFlight ()
	{
		return hasMacFlight;
	}

	public void setHasMacFlight (String hasMacFlight)
	{
		this.hasMacFlight = hasMacFlight;
	}

	public String getArrivalStation ()
	{
		return arrivalStation;
	}

	public void setArrivalStation (String arrivalStation)
	{
		this.arrivalStation = arrivalStation;
	}

	public Price getPrice ()
	{
		return price;
	}

	public void setPrice (Price price)
	{
		this.price = price;
	}

	public String getDepartureStation ()
	{
		return departureStation;
	}

	public void setDepartureStation (String departureStation)
	{
		this.departureStation = departureStation;
	}

	public String getPriceType ()
	{
		return priceType;
	}

	public void setPriceType (String priceType)
	{
		this.priceType = priceType;
	}

	public String getClassOfService ()
{
	return classOfService;
}

	public void setClassOfService (String classOfService)
	{
		this.classOfService = classOfService;
	}

	public String getDate ()
	{
		return date;
	}

	public void setDate (String date)
	{
		this.date = date;
	}
}
