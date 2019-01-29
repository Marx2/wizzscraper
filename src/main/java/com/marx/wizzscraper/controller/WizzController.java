package com.marx.wizzscraper.controller;

import static java.util.Collections.singletonList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.marx.wizzscraper.data.Api;
import com.marx.wizzscraper.data.Fare;
import com.marx.wizzscraper.data.FlightList;
import com.marx.wizzscraper.data.FlightQuery;
import com.marx.wizzscraper.data.OutboundFlights;
import com.marx.wizzscraper.service.CalculateService;

@RestController
public class WizzController
{
	private final static Logger LOG = LoggerFactory.getLogger(WizzController.class);
	//	@Autowired
//	private WizzClient wizzClient;
	@Autowired
	private CalculateService calculateService;

	private final RestTemplate restTemplate = new RestTemplate();

	@GetMapping("/test")
	public String test()
	{
		return "Everything is ok!";
	}

	@GetMapping("/")
	public String farechart()
	{
		final List<OutboundFlights> outboundFlights = new ArrayList<>();
		final List<OutboundFlights> returnFlights = new ArrayList<>();
		queryFlights("2019-07-01", outboundFlights, returnFlights);
		queryFlights("2019-07-22", outboundFlights, returnFlights);
		queryFlights("2019-08-11", outboundFlights, returnFlights);
		queryFlights("2019-09-01", outboundFlights, returnFlights);
		return calculateService.calculate(outboundFlights, returnFlights);
	}

	private void queryFlights(final String date, final List<OutboundFlights> outboundFlights,
			final List<OutboundFlights> returnFlights)
	{
		final Fare body = farechart(date, "KTW", "DSA", "10").getBody();

		addNew(outboundFlights, Arrays.asList(body.getOutboundFlights()));
		addNew(returnFlights, Arrays.asList(body.getReturnFlights()));
	}

	private void addNew(final List<OutboundFlights> allFlights, final List<OutboundFlights> flights)
	{
		final Predicate<OutboundFlights> notInAll = s -> !allFlights.stream().anyMatch(d -> s.equals(d.getDate()));
		final List<OutboundFlights> newFlights = flights.stream().filter(notInAll).collect(Collectors.toList());
		if (newFlights != null)
		{
			allFlights.addAll(newFlights);
		}
	}


	@GetMapping("/farechart")
	public ResponseEntity<Fare> farechart(@RequestParam(required = false) String date,
			@RequestParam(required = false) String source, @RequestParam(required = false) String target,
			@RequestParam(required = false) String interval)
	{
		if (date == null || date.isEmpty())
		{
			date = "2019-02-20";
		}
		if (source == null || source.isEmpty())
		{
			source = "KTW";
		}
		if (target == null || target.isEmpty())
		{
			target = "DSA";
		}
		if (interval == null || interval.isEmpty())
		{
			interval = "3";
		}
		final String url = getApi() + "/asset/farechart";

		final HttpHeaders headers = getHttpHeaders();
		headers.put("Content-Length", singletonList("144"));
		headers.put("Content-Type", singletonList("application/json;charset=utf-8"));
		headers.put("Accept", singletonList("application/json, text/plain, */*"));
		headers.put("Referer", singletonList("https://wizzair.com/pl-pl"));
		headers.put("Origin", singletonList("https://wizzair.com"));

		final FlightQuery flightQuery = new FlightQuery();
		flightQuery.setAdultCount("1");
		flightQuery.setChildCount("0");
		flightQuery.setDayInterval(interval);
		flightQuery.setWdc("false");
		final FlightList[] list = new FlightList[2];
		final FlightList flightFrom = new FlightList();
		flightFrom.setDepartureStation(source);
		flightFrom.setArrivalStation(target);
		flightFrom.setDate(date);
		list[0] = flightFrom;
		final FlightList flightTo = new FlightList();
		flightTo.setDepartureStation(target);
		flightTo.setArrivalStation(source);
		flightTo.setDate(date);
		list[1] = flightTo;
		flightQuery.setFlightList(list);
		final UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url);

		final HttpEntity entity = new HttpEntity(flightQuery, headers);

		final ResponseEntity<Fare> fare = restTemplate.exchange(builder.toUriString(), HttpMethod.POST, entity, Fare.class);
		return fare;
	}

	private HttpHeaders getHttpHeaders()
	{
		final HttpHeaders headers = new HttpHeaders();
		headers.put("User-Agent",
				singletonList("Mozilla/5.0 (Macintosh; Intel Mac OS X 10.14; rv:64.0) Gecko/20100101 Firefox/64.0"));
		headers.put("Accept-Language", singletonList("en-US,en;q=0.5"));
		headers.put("Cache-Control", singletonList("max-age=0"));
		headers.put("Connection", singletonList("keep-alive"));
		headers.put("DNT", singletonList("1"));
		headers.put("Host", singletonList("be.wizzair.com"));
		headers.put("Upgrade-Insecure-Requests", singletonList("1"));
//		headers.put("Accept-Encoding", singletonList("gzip, deflate, br"));
		return headers;
	}

	private String getApi()
	{
		final ResponseEntity<Api> api = restTemplate.getForEntity("https://wizzair.com/static/metadata.json", Api.class);
		return api.getBody().getApiUrl();
	}
}
