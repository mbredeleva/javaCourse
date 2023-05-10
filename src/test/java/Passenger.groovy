

class Passenger {
    String name;
    int trips;
    int airline;

    public Passenger(String name, int airline, int trips) {
        this.name = name;
        this.trips = trips;
        this.airline = airline;
    }

    public Passenger(String name, int airline) {
        Passenger(name, airline, 0)
    }
}
