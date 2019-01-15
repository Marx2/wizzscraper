package com.marx.wizzscraper.data;

public class FlightList
{
	private String arrivalStation;

	private String departureStation;

	private String date;

	public String getArrivalStation ()
	{
		return arrivalStation;
	}

	public void setArrivalStation (String arrivalStation)
	{
		this.arrivalStation = arrivalStation;
	}

	public String getDepartureStation ()
	{
		return departureStation;
	}

	public void setDepartureStation (String departureStation)
	{
		this.departureStation = departureStation;
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
