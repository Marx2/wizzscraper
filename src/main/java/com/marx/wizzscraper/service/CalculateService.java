package com.marx.wizzscraper.service;

import org.springframework.stereotype.Service;

import com.marx.wizzscraper.data.OutboundFlights;

@Service
public class CalculateService
{
	public void calculate(final OutboundFlights[] outboundFlights)
	{
		for (OutboundFlights outboundFlight : outboundFlights)
		{
			System.out.println(outboundFlight.getDate() + ": " + outboundFlight.getPrice().getAmount() + outboundFlight.getPrice()
					.getCurrencyCode());
		}

	}
}
