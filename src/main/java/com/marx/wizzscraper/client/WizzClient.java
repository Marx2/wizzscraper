package com.marx.wizzscraper.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.marx.wizzscraper.data.Flights;

import feign.HeaderMap;

//"https://be.wizzair.com/9.3.0/Api/search/flightDates?departureStation=KTW&arrivalStation=DSA&from=2019-01-16&to=2019-03-16"
@FeignClient(name = "wizz", url = "https://be.wizzair.com")
public interface WizzClient
{
	@RequestMapping(method = RequestMethod.GET, value =
			"/9.3.0/Api/search/flightDates?departureStation=KTW&arrivalStation=DSA" + "&from=2019-01-16&to=2019-03-16",
			produces = "application/json")
	List<Flights> getFlights(@HeaderMap MultiValueMap<String, String> headers);
}


