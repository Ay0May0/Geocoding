import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class Place implements Comparable {
	private String name;
	private String address;
	private double latitude;
	private double longitude;
	public static Place currentPlace;

	public Place(String name, String address) {
		this.name = name;
		this.address = address;
	}

	public Place(String name, String address, double latitude,
			double longitude) {
		this.name = name;
		this.address = address;
		this.latitude = latitude;
		this.longitude = longitude;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public Place getCurrentPlace() {
		return currentPlace;
	}

	public void setCurrentPlace(Place currentPlace) {
		this.currentPlace = currentPlace;
	}

	@Override
	public boolean equals(Object arg0) {
		Place place = (Place) arg0;
		if (place.getName().equalsIgnoreCase(name) &&
				place.getAddress().equalsIgnoreCase(address))
			return true;
		else
			return false;
	}

	public String getURL() {
		String url = "https://www.google.com/maps/place/%s/@%s,%s,17z/";
		String encodedAddress = "";
		try {
			encodedAddress = URLEncoder.encode(address, "UTF-8");

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return String.format(url, encodedAddress, latitude + "",
				longitude + "");
	}

	@Override
	public String toString() {
		return name + "\n" + address + "\n" + latitude + "," +
	longitude + "\n" + getURL();
	}

	public String writeString() {
		return name + ";" + address + ";" + latitude + ";" + longitude;
	}

	public double getDistance() {
		return Geocoding.distance(latitude, longitude,
				currentPlace.getLatitude(), currentPlace.getLongitude());
	}

	@Override
	public int compareTo(Object arg0) {
		Place place = (Place) arg0;
		if (currentPlace == null)
			return name.compareTo(place.getName());
		else {
			if (getDistance() > place.getDistance())
				return 1;
			else if (getDistance() < place.getDistance())
				return -1;
			else
				return 0;
		}
	}
}