package com.marx.wizzscraper.data;

public class Fare
{

	private OutboundFlights[] outboundFlights;

	private OutboundFlights[] returnFlights;

	public OutboundFlights[] getOutboundFlights()
	{
		return outboundFlights;
	}

	public void setOutboundFlights(OutboundFlights[] outboundFlights)
	{
		this.outboundFlights = outboundFlights;
	}

	public OutboundFlights[] getReturnFlights()
	{
		return returnFlights;
	}

	public void setReturnFlights(OutboundFlights[] returnFlights)
	{
		this.returnFlights = returnFlights;
	}
}
