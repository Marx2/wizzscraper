package com.marx.wizzscraper.data;

public class Fare
{

	private OutboundFlights[] outboundFlights;

	private String[] returnFlights;

	public OutboundFlights[] getOutboundFlights ()
	{
		return outboundFlights;
	}

	public void setOutboundFlights (OutboundFlights[] outboundFlights)
	{
		this.outboundFlights = outboundFlights;
	}

	public String[] getReturnFlights ()
	{
		return returnFlights;
	}

	public void setReturnFlights (String[] returnFlights)
	{
		this.returnFlights = returnFlights;
	}
}
