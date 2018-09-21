import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Formatter;
import java.util.Scanner;
public class MyFavoritePlaces {
	private static Scanner cmdScanner = new Scanner(System.in);
	private static ListOfPlaces listOfPlaces = new ListOfPlaces();

	public static void main(String[] args) {
		while (true) {
			System.out.println("");
			System.out.println("My Favorite Places 2016");
			System.out.println(listOfPlaces.toString());
			if (listOfPlaces.getCount() == 0) {
				System.out.print("A)dd R)ead Q)uit : ");
				String input = cmdScanner.nextLine().toUpperCase();
				switch (input) {
				case "A":
					addPlace();
					break;
				case "R":
					readPlaces();
					break;
				case "Q":
					System.out.println("Thank you for using My Favorite Places"
							+ " 2016!");
					System.exit(0);
				}
			} else {
				System.out.print("A)dd S)how E)dit D)elete C)urrent R)ead"
						+ " W)rite Q)uit : ");
				String input = cmdScanner.nextLine().toUpperCase();
				switch (input) {
				case "A":
					addPlace();
					break;
				case "S":
					showPlace();
					break;
				case "E":
					editPlace();
					break;
				case "D":
					deletePlace();
					break;
				case "C":
					setCurrentPlace();
					break;
				case "R":
					readPlaces();
					break;
				case "W":
					writePlaces();
					break;
				case "Q":
					System.out.println("Thank you for using My Favorite"
							+ " Places 2016!");
					System.exit(0);
				}
			}
		}
	}

	/*
	 * Call when user selects Add option (a)
	 * This method get name and address from user and find latitude and
	 * longitude via google api
	 */
	public static void addPlace() {
		System.out.print("Enter the name: ");
		String name = cmdScanner.nextLine();
		System.out.print("Enter the address: ");
		String address = cmdScanner.nextLine();
		GResponse gresponse = null;
		try {
			String response = Geocoding.find(address);
			gresponse = GeocodeResponse.parse(response);
			Place newPlace = new Place(name, gresponse.getFormattedAddress(),
					gresponse.getLatitude(),
					gresponse.getLongitude());
			if (listOfPlaces.alreadyExists(newPlace))
				System.out.println("Place " + name + " already in list.");
			else {
				listOfPlaces.add(newPlace);
				listOfPlaces.sort();
			}

		} catch (Exception e) {
			System.out.println("Place not found using address: " + name);
			pressAnyKeyToContinue();
		}
	}

	/*
	 * Call during the file reading
	 */
	public static void addPlace(Place newPlace) {
		if (listOfPlaces.alreadyExists(newPlace))
			System.out.println("Place " + newPlace.getName()
			+ " already in list.");
		else
			listOfPlaces.add(newPlace);
	}

	/*
	 * Call when user selects Show option (s)
	 * This method gets place id from user call toString method of listOfPlaces
	 *  object as well as open the browser to show the location
	 */
	public static void showPlace() {
		System.out.print("Enter number of place to Show: ");
		String input = cmdScanner.nextLine();
		try {
			int number = Integer.parseInt(input);
			if (number > 0 && number <= listOfPlaces.getCount()) {
				System.out.println(listOfPlaces.retrieve(number - 1)
						.toString());
				try {
					Geocoding.openBrowser(listOfPlaces.retrieve(number - 1)
							.getURL());
				} catch (IOException e) {
					e.printStackTrace();
				} catch (URISyntaxException e) {
					e.printStackTrace();
				}
				pressAnyKeyToContinue();
			} else
				System.out.println("Invalid value: " + number);
		} catch (Exception e) {
			System.out.println("Invalid value: " + input);
		}

	}

	/*
	 * Call when user selects Edit option (e)
	 * This method gets name and address again from user and updates the Place 
	 * if new address is not available in google api, ignores the update
	 */
	public static void editPlace() {
		System.out.print("Enter number of place to Edit: ");
		String input = cmdScanner.nextLine();
		try {
			int number = Integer.parseInt(input);
			if (number > 0 && number <= listOfPlaces.getCount()) {
				Place place = listOfPlaces.retrieve(number - 1);
				System.out.println("Current name: " + place.getName());
				System.out.print("Enter a new name: ");
				String name = cmdScanner.nextLine();
				place.setName(name);
				listOfPlaces.sort();
				System.out.println("Current address: " + place.getAddress());
				System.out.print("Enter a new address: ");
				String address = cmdScanner.nextLine();				
				String response = Geocoding.find(address);
				GResponse gresponse = GeocodeResponse.parse(response);								
				place.setAddress(gresponse.getFormattedAddress());
				place.setLatitude(gresponse.getLatitude());
				place.setLongitude(gresponse.getLongitude());
				listOfPlaces.sort();
			} else
				System.out.println("Invalid value: " + number);
		} catch (Exception e) {
			System.out.println("Invalid value: " + input);
		}

	}

	/*
	 * Call when user selects Current option (c)
	 * This method gets place id from user and set current place
	 */
	public static void setCurrentPlace() {
		System.out.print("Enter number of place to be Current place: ");
		String input = cmdScanner.nextLine();
		try {
			int number = Integer.parseInt(input);
			if (number > 0 && number <= listOfPlaces.getCount()) {
				Place.currentPlace = listOfPlaces.retrieve(number - 1);
				listOfPlaces.sort();
				System.out.println(Place.currentPlace
						.getName() + " set as Current place.");
				pressAnyKeyToContinue();
			} else
				System.out.println("Invalid value: " + number);
		} catch (Exception e) {
			System.out.println("Invalid value: " + input);
		}

	}

	/*
	 * Call when user selects Read option (r)
	 * This method shows available files in the root directory and reads the
	 *  file according to user selection
	 */
	public static void readPlaces() {
		System.out.println("Available Files:");
		File folder = new File(".");
		for (File file : folder.listFiles()) {
			if (file.getName().endsWith("mfp"))
				System.out.println("\t" + file.getName());
		}
		System.out.println("");
		System.out.print("Enter filename: ");
		String fileName = cmdScanner.nextLine();
		File file = new File(fileName);
		try {
			Scanner fileScanner = new Scanner(file);
			while (fileScanner.hasNextLine()) {
				String line = fileScanner.nextLine();
				String[] split = line.split(";");
				Place newPlace = new Place(split[0], split[1],
						Double.parseDouble(split[2]),
						Double.parseDouble(split[3]));
				addPlace(newPlace);
			}
			listOfPlaces.sort();
			fileScanner.close();

		} catch (FileNotFoundException e) {

			System.out.println("Unable to read from file: " + fileName);
		}
	}

	/*
	 * Call when user selects Write option (w)
	 * This method shows available files in the root directory and writes 
	 * the file according to user selection
	 */
	public static void writePlaces() {
		System.out.println("Current Files:");
		File folder = new File(".");
		for (File file : folder.listFiles()) {
			if (file.getName().endsWith("mfp"))
				System.out.println("\t" + file.getName());
		}
		System.out.println("");
		System.out.print("Enter filename: ");
		String fileName = cmdScanner.nextLine();
		try {
			Formatter formatter = new Formatter(fileName);
			for (int i = 0; i < listOfPlaces.getCount(); i++) {
				formatter.format("%s\n", listOfPlaces.retrieve(i)
						.writeString());
			}
			formatter.close();
		} catch (FileNotFoundException e) {
			System.out.println("Unable to write to file: ");
		}
	}

	/*
	 * Call when user selects Delete option (d)
	 * This method gets place id from user and delete it from the list
	 * If deleted item is current place then makes currentPlace = null
	 */
	public static void deletePlace() {
		System.out.print("Enter number of place to Delete: ");
		String input = cmdScanner.nextLine();
		try {
			int number = Integer.parseInt(input);
			if (number > 0 && number <= listOfPlaces.getCount()) {
				if (Place.currentPlace != null && listOfPlaces
						.retrieve(number - 1).equals(Place.currentPlace))
					Place.currentPlace = null;
				String name = listOfPlaces.delete(number - 1);
				System.out.println(name + " deleted.");
				listOfPlaces.sort();
				pressAnyKeyToContinue();
			} else
				System.out.println("Invalid value: " + number);
		} catch (Exception e) {
			System.out.println("Invalid value: " + input);
		}
	}

	/*
	 * Call when need to show "Press Enter to continue."
	 * This method waits for the enter key
	 */
	private static void pressAnyKeyToContinue() {
		System.out.print("Press Enter to continue.");
		cmdScanner.nextLine();
	}

}