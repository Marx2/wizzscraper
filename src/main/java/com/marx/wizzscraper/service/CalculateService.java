package com.marx.wizzscraper.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.marx.wizzscraper.data.OutboundFlights;

@Service
public class CalculateService
{
	private final static Logger LOG = LoggerFactory.getLogger(CalculateService.class);

	public void calculate(final OutboundFlights[] outboundFlights)
	{
		for (OutboundFlights outboundFlight : outboundFlights)
		{
			LOG.info(outboundFlight.getDate() + ": " + outboundFlight.getPrice().getAmount() + outboundFlight.getPrice()
					.getCurrencyCode());
		}
	}
}
