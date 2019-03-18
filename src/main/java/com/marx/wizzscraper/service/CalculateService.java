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


	public String calculate(final List<OutboundFlights> outboundFlights, final List<OutboundFlights> returnFlights)
	{
		final List<DatePrice> outboundConverted = convert(outboundFlights);
		final List<DatePrice> returnConverted = convert(returnFlights);
		return findCheapestRoute(outboundConverted, returnConverted, 5, 7);
	}

	private List<DatePrice> convert(final List<OutboundFlights> outboundFlights)
	{
		final List<DatePrice> list = new ArrayList<>();
		for (final OutboundFlights outboundFlight : outboundFlights)
		{
			//LOG.info(outboundFlight.getDate() + ": " + outboundFlight.getPrice().getAmount() + outboundFlight.getPrice()
			//                                                                                                 .getCurrencyCode());
			Double price = Double.parseDouble(outboundFlight.getPrice().getAmount());
			if (outboundFlight.getPrice().getCurrencyCode().equals("GBP"))
			{
				price = price.doubleValue() * GBP;
			}
			//LOG.info(outboundFlight.getPrice().getAmount() + "->" + price + " " + outboundFlight.getPrice().getCurrencyCode());
			final LocalDate localDate = LocalDate.parse(outboundFlight.getDate().substring(0, 10), formatter);
			final DatePrice datePrice = new DatePrice(localDate, price.intValue());
			if (price > 0)
			{
				list.add(datePrice);
			}
		}
		return list;
	}

	private String findCheapestRoute(final List<DatePrice> outboundFlights, final List<DatePrice> returnConverted, final int min,
	                                 final int max)
	{
		final List<Period> periods = new ArrayList<>();
		LocalDate start = null;
		Integer startPrice = null;
		LocalDate end = null;
		Integer endPrice = null;
		for (final DatePrice datePriceStart : outboundFlights)
		{
			start = datePriceStart.getDate();
			startPrice = datePriceStart.getPrice();

			for (final DatePrice datePrice : returnConverted)
			{
				end = datePrice.getDate();
				endPrice = datePrice.getPrice();
				final long diff = start.until(end, ChronoUnit.DAYS);

				if (diff < min)
				{
					continue;
				}
				if (diff > max)
				{
					continue;
				}
				final Period period = new Period(start, end, startPrice, endPrice);
				periods.add(period);
			}
		}

		Collections.sort(periods);
		final Map<String, List<Period>> group = group(periods);
		return formatOutput(group);
	}

	private Map<String, List<Period>> group(final List<Period> periods)
	{
		final Map<String, List<Period>> groups = new LinkedHashMap<>();
		for (final Period p : periods)
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

	private String formatOutput(final Map<String, List<Period>> groups)
	{
		final StringBuffer r = new StringBuffer();
		final int priceBaggage10kgx2 = 90 * 2;
		final int priceBaggage20kgx1 = 150;
		final int priceBaggage32kgx1 = 232;
		final int priceBaggage2way = priceBaggage20kgx1 * 2;
		final int priceSeats = 2 * (43 + 39 + 39);
		r.append("Bagaz: " + priceBaggage2way).append(app);
		r.append("Siedzenia: " + priceSeats).append(app);
		r.append(eol);
		for (final Map.Entry<String, List<Period>> e : groups.entrySet())
		{
			final Optional<Period> first = e.getValue().stream().findFirst();
			if (first.isPresent())
			{
				final Period c = first.get();
				final long days = c.getStart().until(c.getEnd(), ChronoUnit.DAYS);
				final int price3Persons = c.getFullPrice() * 3;
				final int totalPrice = price3Persons + priceBaggage2way + priceSeats;
				final int wizzClub = -(45 + 45);
				final int totalWizzClubPrice = totalPrice + wizzClub;
				r.append(c.getFullPrice() + " (" + c.getStartPrice() + "+" + c.getEndPrice() + ")").append(eol);
				r.append("Bilety: " + price3Persons).append(app);
				r.append("SUMA: " + totalPrice).append(app);
				r.append("SUMA (Wizz Club): " + totalWizzClubPrice).append(app);
			}
			for (final Period period : e.getValue())
			{
				r.append(period.getStart() + " " + period.getEnd() + " (" + period.getStart()
				                                                                  .until(period.getEnd(),
				                                                                         ChronoUnit.DAYS) + " dni)" + eol);
			}
			r.append(eol);
		}
		return r.toString();
	}
}
