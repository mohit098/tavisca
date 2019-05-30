package com.example.searcher.Controller;

import java.util.ArrayList;

import javax.validation.constraints.NotNull;

import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.searcher.Model.Place;
import com.example.searcher.query.Processor.QueryProcessor;

@RestController
public class SearchController {

	@GetMapping(value = "/healthcheck")
	public ResponseEntity<String> healthcheck() {
		return new ResponseEntity<String>("Service is Up", HttpStatus.OK);
	}
	@GetMapping(value = "/searcher/searchAnyLocation")
	public ResponseEntity<?> retrievePlace(@NotNull @RequestParam("input") String input) {
		try {
			Place place = QueryProcessor.details(input);
			if (place != null) {
				return new ResponseEntity<>(place, HttpStatus.OK);
			} else {
				return new ResponseEntity<>("No Result Found.", HttpStatus.OK);
			}
		} catch (Exception e) {
			return new ResponseEntity<>(new Error(), HttpStatus.BAD_REQUEST);
		}

	}

	@GetMapping(value = "/searcher/typeSearch")
	public ResponseEntity<?> typeSearch(@NotNull @RequestParam("input") String input,
			@NotNull @RequestParam("types") String types) {
		try {
			String results = QueryProcessor.typeSearch(input, types);
			if (results != null) {
				return new ResponseEntity<>(results, HttpStatus.OK);
			} else {
				return new ResponseEntity<>("No Result Found.", HttpStatus.OK);
			}
		} catch (Exception e) {
			return new ResponseEntity<>(new Error(), HttpStatus.BAD_REQUEST);
		}

	}

	@RequestMapping(value = "/searcher/advanceSearch", method = RequestMethod.POST, headers = "Accept=application/json")
	public ResponseEntity<?> retrieveByOption(@RequestBody String request) {
		try {
			JSONObject jsonObj = new JSONObject(request).getJSONObject("query");
			String keyword = jsonObj.getString("type");
			Double lat = Double.valueOf(jsonObj.getString("lat"));
			Double lng = Double.valueOf(jsonObj.getString("lng"));
			Integer radius = Integer.valueOf(jsonObj.getString("radius"));
			ArrayList<Place> place = QueryProcessor.advanceSearch(keyword, lat, lng, radius);
			if (place != null) {
				return new ResponseEntity<>(place, HttpStatus.OK);
			} else {
				return new ResponseEntity<>("No Result Found.", HttpStatus.OK);
			}
		} catch (Exception e) {
			return new ResponseEntity<>(new Error(), HttpStatus.BAD_REQUEST);
		}
	}

}
