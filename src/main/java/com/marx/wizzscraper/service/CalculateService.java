package com.marx.wizzscraper.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.marx.wizzscraper.data.DatePrice;
import com.marx.wizzscraper.data.OutboundFlights;
import com.marx.wizzscraper.data.Period;

@Service
public class CalculateService
{
	private final static Logger LOG = LoggerFactory.getLogger(CalculateService.class);
	private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	private final double GBP = 4.8385;
	private final String eol = System.lineSeparator();
	private final String pln = " PLN";
	private final String app = pln + eol;


	public String calculate(List<OutboundFlights> outboundFlights, List<OutboundFlights> returnFlights)
	{
		List<DatePrice> outboundConverted = convert(outboundFlights);
		List<DatePrice> returnConverted = convert(returnFlights);
		return findCheapestRoute(outboundConverted, returnConverted, 5, 7);
	}

	private List<DatePrice> convert(List<OutboundFlights> outboundFlights)
	{
		List<DatePrice> list = new ArrayList<>();
		for (OutboundFlights outboundFlight : outboundFlights)
		{
//			LOG.info(outboundFlight.getDate() + ": " + outboundFlight.getPrice().getAmount() + outboundFlight.getPrice()
//					.getCurrencyCode());
			Double price = Double.parseDouble(outboundFlight.getPrice().getAmount());
			if (outboundFlight.getPrice().getCurrencyCode().equals("GBP"))
			{
				price = price.doubleValue() * GBP;
			}
//			LOG.info(outboundFlight.getPrice().getAmount() + "->" + price + " " + outboundFlight.getPrice().getCurrencyCode());
			LocalDate localDate = LocalDate.parse(outboundFlight.getDate().substring(0, 10), formatter);
			DatePrice datePrice = new DatePrice(localDate, price.intValue());
			if (price > 0)
			{
				list.add(datePrice);
			}
		}
		return list;
	}

	private String findCheapestRoute(List<DatePrice> outboundFlights, List<DatePrice> returnConverted, int min, int max)
	{
		List<Period> periods = new ArrayList<>();
		LocalDate start = null;
		Integer startPrice = null;
		LocalDate end = null;
		Integer endPrice = null;
		for (DatePrice datePriceStart : outboundFlights)
		{
			start = datePriceStart.getDate();
			startPrice = datePriceStart.getPrice();

			for (DatePrice datePrice : returnConverted)
			{
				end = datePrice.getDate();
				endPrice = datePrice.getPrice();
				long diff = start.until(end, ChronoUnit.DAYS);

				if (diff < min)
				{
					continue;
				}
				if (diff > max)
				{
					continue;
				}
				Period period = new Period(start, end, startPrice, endPrice);
				periods.add(period);
			}
		}

		Collections.sort(periods);
		Map<String, List<Period>> group = group(periods);
		return formatOutput(group);
	}

	private Map<String, List<Period>> group(List<Period> periods)
	{
		Map<String, List<Period>> groups = new LinkedHashMap<>();
		for (Period p : periods)
		{
			List<Period> periodsForPrice = groups.get(String.valueOf(p.getFullPrice()));
			if (periodsForPrice == null)
			{
				periodsForPrice = new ArrayList<>();
				groups.put(String.valueOf(p.getFullPrice()), periodsForPrice);
			}
			periodsForPrice.add(p);
		}
		return groups;
	}

	private String formatOutput(Map<String, List<Period>> groups)
	{
		StringBuffer r = new StringBuffer();
		int priceBaggage10kgx2 = 84 * 2;
		int priceBaggage20kgx1 = 155;
		int priceBaggage2way = priceBaggage10kgx2 * 2;
		int priceSeats = 2 * (60 + 55 + 55);
		r.append("Bagaz: " + priceBaggage2way).append(app);
		r.append("Siedzenia: " + priceSeats).append(app);
		r.append(eol);
		for (Map.Entry<String, List<Period>> e : groups.entrySet())
		{
			Optional<Period> first = e.getValue().stream().findFirst();
			if (first.isPresent())
			{
				Period c = first.get();
				long days = c.getStart().until(c.getEnd(), ChronoUnit.DAYS);
				int price3Persons = c.getFullPrice() * 3;
				int totalPrice = price3Persons + priceBaggage2way + priceSeats;
				int wizzClub = -270;
				int totalWizzClubPrice = totalPrice + wizzClub;
				r.append(c.getFullPrice() + " (" + c.getStartPrice() + "+" + c.getEndPrice() + ")").append(eol);
				r.append("Bilety: " + price3Persons).append(app);
				r.append("SUMA: " + totalPrice).append(app);
				r.append("SUMA (Wizz Club): " + totalWizzClubPrice).append(app);
			}
			for (Period period : e.getValue())
			{
				r.append(period.getStart() + " " + period.getEnd() + " (" + period.getStart()
						.until(period.getEnd(), ChronoUnit.DAYS) + " dni)" + eol);
			}
			r.append(eol);
		}
		return r.toString();
	}
}
