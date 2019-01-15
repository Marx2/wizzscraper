package com.marx.wizzscraper.controller;

import static java.util.Collections.singletonList;

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

import com.marx.wizzscraper.client.WizzClient;
import com.marx.wizzscraper.data.Fare;
import com.marx.wizzscraper.data.FlightList;
import com.marx.wizzscraper.data.FlightQuery;
import com.marx.wizzscraper.data.Flights;
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

	private RestTemplate restTemplate = new RestTemplate();

	@GetMapping("/dates")
	public ResponseEntity<Flights> dates()
	{
		String url =
				"https://be.wizzair.com/9.3.0/Api/search/flightDates?departureStation=KTW&arrivalStation=DSA&from=2019-01" +
						"-16&to=2019-03-16";
		HttpHeaders headers = getHttpHeaders();
		headers.put("Accept", singletonList("text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8"));

		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url);
//				.queryParam("msisdn", msisdn)
//				.queryParam("email", email)
//				.queryParam("clientVersion", clientVersion)
//				.queryParam("clientType", clientType)
//				.queryParam("issuerName", issuerName)
//				.queryParam("applicationName", applicationName);

		HttpEntity<?> entity = new HttpEntity<>(headers);

		final ResponseEntity<Flights> flights = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, entity,
				Flights.class);
		return flights;
	}

	@GetMapping("/")
	public void test()
	{
		final ResponseEntity<Fare> farechart = farechart("2019-05-01", "KTW", "DSA","10");
		final Fare fare = farechart.getBody();
		final OutboundFlights[] outboundFlights = fare.getOutboundFlights();
		calculateService.calculate(outboundFlights);
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
		String url = "https://be.wizzair.com/9.4.0/Api/asset/farechart";

		HttpHeaders headers = getHttpHeaders();
		headers.put("Content-Length", singletonList("144"));
		headers.put("Content-Type", singletonList("application/json;charset=utf-8"));
		headers.put("Accept", singletonList("application/json, text/plain, */*"));
		headers.put("Referer", singletonList("https://wizzair.com/pl-pl"));
		headers.put("Origin", singletonList("https://wizzair.com"));

		FlightQuery flightQuery = new FlightQuery();
		flightQuery.setAdultCount("1");
		flightQuery.setChildCount("0");
		flightQuery.setDayInterval(interval);
		FlightList[] list = new FlightList[1];
		final FlightList flight = new FlightList();
		list[0] = flight;
		flight.setDepartureStation(source);
		flight.setArrivalStation(target);
		flight.setDate(date);
		flightQuery.setFlightList(list);
		flightQuery.setWdc("false");
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url);

		HttpEntity entity = new HttpEntity(flightQuery, headers);

		final ResponseEntity<Fare> fare = restTemplate.exchange(builder.toUriString(), HttpMethod.POST, entity, Fare.class);
		return fare;
	}

	private HttpHeaders getHttpHeaders()
	{
		HttpHeaders headers = new HttpHeaders();
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
}
