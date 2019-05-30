package com.example.searcher.query.Processor;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.searcher.Constants.Constants;
import com.example.searcher.Model.Place;

public class QueryProcessor {

	public static Place details(String reference) {
		HttpURLConnection conn = null;
		StringBuilder jsonResults = new StringBuilder();
		try {
			StringBuilder sb = new StringBuilder(Constants.PLACES_API_BASE);
			sb.append(Constants.TYPE_DETAILS);
			sb.append(Constants.OUT_JSON);
			sb.append("?sensor=false");
			sb.append("&key=" + Constants.API_KEY);
			sb.append("&reference=" + URLEncoder.encode(reference, "utf8"));
			System.out.println(sb.toString());
			URL url = new URL(sb.toString());
			conn = (HttpURLConnection) url.openConnection();
			InputStreamReader in = new InputStreamReader(conn.getInputStream());

			int read;
			char[] buff = new char[1024];
			while ((read = in.read(buff)) != -1) {
				jsonResults.append(buff, 0, read);
			}
		} catch (MalformedURLException e) {
			System.out.println("Error processing Places API URL" + e);
			return null;
		} catch (IOException e) {
			System.out.println("Error processing Places API URL" + e);
			return null;
		} finally {
			if (conn != null) {
				conn.disconnect();
			}
		}

		Place place = null;
		try {
			JSONObject jsonObj = new JSONObject(jsonResults.toString()).getJSONObject("result");
			place = new Place();
			place.setIcon(jsonObj.getString("icon"));
			place.setName(jsonObj.getString("name"));
			place.setFormatted_address(jsonObj.getString("formatted_address"));
			if (jsonObj.has("formatted_phone_number")) {
				place.setFormatted_phone_number(jsonObj.getString("formatted_phone_number"));
			}
		} catch (JSONException e) {
			System.out.println("Error processing JSON " + e);
		}

		return place;
	}

	public static String typeSearch(String input, String types) {
		HttpURLConnection conn = null;
		StringBuilder jsonResults = new StringBuilder();
		try {
			StringBuilder sb = new StringBuilder(Constants.PLACES_API_BASE);
			sb.append(Constants.TYPE_AUTOCOMPLETE);
			sb.append(Constants.OUT_JSON);
			sb.append("?input=" + input);
			sb.append("?types=" + types);
			sb.append("&key=" + Constants.API_KEY);

			URL url = new URL(sb.toString());
			conn = (HttpURLConnection) url.openConnection();
			InputStreamReader in = new InputStreamReader(conn.getInputStream());

			int read;
			char[] buff = new char[1024];
			while ((read = in.read(buff)) != -1) {
				jsonResults.append(buff, 0, read);
			}
		} catch (MalformedURLException e) {
			System.out.println("Error processing Places API URL" + e);
			return null;
		} catch (IOException e) {
			System.out.println("Error processing Places API URL" + e);
			return null;
		} finally {
			if (conn != null) {
				conn.disconnect();
			}
		}

		return jsonResults.toString();
	}

	public static ArrayList<Place> advanceSearch(String keyword, double lat, double lng, int radius) {
		ArrayList<Place> resultList = null;
		HttpURLConnection conn = null;
		StringBuilder jsonResults = new StringBuilder();
		try {
			StringBuilder sb = new StringBuilder(Constants.PLACES_API_BASE);
			sb.append(Constants.TYPE_SEARCH);
			sb.append(Constants.OUT_JSON);
			sb.append("?sensor=false");
			sb.append("&key=" + Constants.API_KEY);
			sb.append("&keyword=" + URLEncoder.encode(keyword, "utf8"));
			sb.append("&location=" + String.valueOf(lat) + "," + String.valueOf(lng));
			sb.append("&radius=" + String.valueOf(radius));

			URL url = new URL(sb.toString());
			conn = (HttpURLConnection) url.openConnection();
			InputStreamReader in = new InputStreamReader(conn.getInputStream());

			int read;
			char[] buff = new char[1024];
			while ((read = in.read(buff)) != -1) {
				jsonResults.append(buff, 0, read);
			}
		} catch (MalformedURLException e) {
			System.out.println("Error processing Places API URL" + e);
			return null;
		} catch (IOException e) {
			System.out.println("Error processing Places API URL" + e);
			return null;
		} finally {
			if (conn != null) {
				conn.disconnect();
			}
		}

		try {
			JSONObject jsonObj = new JSONObject(jsonResults.toString());
			JSONArray predsJsonArray = jsonObj.getJSONArray("results");
			resultList = new ArrayList<Place>(predsJsonArray.length());
			for (int i = 0; i < predsJsonArray.length(); i++) {
				Place place = new Place();
				place.setReference(predsJsonArray.getJSONObject(i).getString("reference"));
				place.setName(predsJsonArray.getJSONObject(i).getString("name"));
				resultList.add(place);
			}
		} catch (JSONException e) {
			System.out.println("Error processing JSON " + e);
		}

		return resultList;
	}

}
