package com.thoughtworks.services.railroad;


public class City {
	private String cityName;
	private double distance;

	
	
	@Override
	public String toString() {
		return "City [cityName=" + cityName + ", at distance=" + distance + "]";
	}

	public City (String cityName, double distance) {
		this.cityName = cityName;
		this.distance = distance;
	}
	
	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public double getDistance() {
		return distance;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}
}
