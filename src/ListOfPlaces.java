
import java.util.ArrayList;
import java.util.Collections;
public class ListOfPlaces {
	ArrayList<Place> places;

	public ListOfPlaces() {
		places = new ArrayList<Place>();
	}

	public void add(Place place) {
		places.add(place);
	}

	public Place retrieve(int index) {
		return places.get(index);
	}

	public String delete(int index) {
		String name = places.get(index).getName();
		places.remove(index);
		return name;
	}

	public boolean alreadyExists(Place newPlace) {
		for (Place place : places) {
			if (place.equals(newPlace))
				return true;
		}
		return false;
	}

	public int getCount() {
		return places.size();
	}

	public void sort() {
		Collections.sort(places);
	}

	/*
	 * This method return a string of places list when user select show option
	 *  (S)
	 * Return string can be different
	 * 01. If there are no places in the list
	 * 
	 	--------------------------
		No places loaded.
		--------------------------
	 *	
	 * 02. If the places are available but the current place is not set
	 * 
	 	--------------------------
		1) Computer Sciences
		2) Health Sciences Learning Center
		3) Kohl Center
		4) Memorial Union
		5) Union South
		6) Waisman Center
		7) Washburn Observatory
		8) Wisconsin State Capital
		--------------------------
	 * 
	 */
	
	@Override
	public String toString() {
		String rtn = "--------------------------\n";
		if (getCount() == 0)
			rtn = rtn + "No places loaded.\n";
		else {
			int i;
			String dis = " (%.2f miles)";
			if (Place.currentPlace == null) {
				for (i = 0; i < places.size(); i++) {
					rtn = rtn + (i + 1) + ") " + places.get(i).getName()
							+ "\n";
				}
			} else {
				rtn = rtn + "distance from " + Place.currentPlace.getName()
				+ "\n";
				for (i = 0; i < places.size(); i++) {
					rtn = rtn + (i + 1) + ") " + places.get(i).getName()
							+ String.format(dis, places.get(i).getDistance())
							+ "\n";
				}
			}
		}
		rtn = rtn + "--------------------------";
		return rtn;
	}
}