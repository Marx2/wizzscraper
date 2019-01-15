package com.marx.wizzscraper.data;

public class FlightQuery
{
	private String dayInterval;

	private String childCount;

	private String adultCount;

	private String wdc;

	private FlightList[] flightList;

	public String getDayInterval()
	{
		return dayInterval;
	}

	public void setDayInterval(String dayInterval)
	{
		this.dayInterval = dayInterval;
	}

	public String getChildCount()
	{
		return childCount;
	}

	public void setChildCount(String childCount)
	{
		this.childCount = childCount;
	}

	public String getAdultCount()
	{
		return adultCount;
	}

	public void setAdultCount(String adultCount)
	{
		this.adultCount = adultCount;
	}

	public String getWdc()
	{
		return wdc;
	}

	public void setWdc(String wdc)
	{
		this.wdc = wdc;
	}

	public FlightList[] getFlightList()
	{
		return flightList;
	}

	public void setFlightList(FlightList[] flightList)
	{
		this.flightList = flightList;
	}
}