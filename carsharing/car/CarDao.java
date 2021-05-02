package carsharing.car;

import java.util.List;

public interface CarDao {
    // returns list of company cars
    List<Car> getCars(int companyId);

    void createCar(int companyId, String name);
}
