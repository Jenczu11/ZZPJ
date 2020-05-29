package pl.lodz.p.it.mercedes.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.lodz.p.it.mercedes.exceptions.CarNotFoundException;
import pl.lodz.p.it.mercedes.model.Car;
import pl.lodz.p.it.mercedes.model.Review;
import pl.lodz.p.it.mercedes.repositories.CarRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@AllArgsConstructor
public class CarService {
    private final CarRepository carRepository;
    public void addCar(Car car) {
        carRepository.insert(car);
    }

    public List<Car> getCarWithName(String name) throws CarNotFoundException {
        if (carRepository.findAllByNameContaining(name).isPresent()) {
            List<Car> cars = carRepository.findAllByNameContaining(name).get();
            if (cars.size() == 0) {
                throw new CarNotFoundException("Car not found.");
            }
            return cars;
        } else {
            throw new CarNotFoundException("Car not found.");
        }
    }

    public Car getCarById(String id) throws CarNotFoundException {
        if (carRepository.findById(id).isPresent()) {
            return carRepository.findById(id).get();
        } else {
            throw new CarNotFoundException("Car not found.");
        }
    }

    public List<Car> getAllCars() {
        return carRepository.findAll();
    }

    public Car deleteCar(String id) throws CarNotFoundException {
        if (carRepository.findById(id).isPresent()) {
            Car carToDelete = carRepository.findById(id).get();
            carRepository.delete(carToDelete);
            return carToDelete;
        } else {
            throw new CarNotFoundException("Car not found.");
        }
    }

    public void addReviewToCar(String carId, Review review) throws CarNotFoundException {
        Car car = getCarById(carId);
        car.getReviewList().add(review);
        carRepository.save(car);
    }
    public void updateReviewInCar(String carId, Review reviewToUpdate) throws CarNotFoundException {
        Car car = getCarById(carId);
//        TODO: Logika updatujaca review a nie insertujaca.
        ArrayList<Review> reviewsForCar = car.getReviewList();
        for(Review review : reviewsForCar) {
            if(review.getCarId().equals(reviewToUpdate.getCarId())){
                if(review.getUserId().equals(reviewToUpdate.getUserId())) {
                    reviewsForCar.remove(review);
                    reviewsForCar.add(reviewToUpdate);
                }
            }

        };
        car.setReviewList(reviewsForCar);
        carRepository.save(car);
    }
    public Car calculateAverageRatings(String carId) throws CarNotFoundException {

        Car car = getCarById(carId);
        calculateAverageRatingsHelper(car);
        carRepository.save(car);
        return car;
    }

    public Car calculateAverageRatings(Car car) throws CarNotFoundException {

        calculateAverageRatingsHelper(car);
        carRepository.save(car);
        return car;
    }

    private Car calculateAverageRatingsHelper(Car car) {
        Double rating = 0.0;
        Double valueForMoneyAverage = 0.0;
        Double performanceAverage = 0.0;
        Double visualAspectAverage = 0.0;
        Integer numberOfRatings = 0;
        List<Review> reviewsForCar = car.getReviewList();
        for (Review review : reviewsForCar) {
            performanceAverage += review.getPerformance();
            valueForMoneyAverage += review.getValueForMoney();
            visualAspectAverage += review.getVisualAspect();
            rating += review.getOverallRating();
            numberOfRatings++;
        }
        ;

        performanceAverage /= numberOfRatings;
        valueForMoneyAverage /= numberOfRatings;
        visualAspectAverage /= numberOfRatings;
        rating /= numberOfRatings;

        car.setPerformanceAverage(performanceAverage);
        car.setValueForMoneyAverage(valueForMoneyAverage);
        car.setVisualAspectAverage(visualAspectAverage);
        car.setRating(rating);
        car.setNumberOfRatings(numberOfRatings);
        return car;
    }


    public List<Car> getTop10_OverallRating() {
        List<Car> cars = this.getAllCars();
        List<Car> top10 = new ArrayList<>();
        Collections.sort(cars,(o1, o2) -> {
            if(o1.getRating() < o2.getRating()) return 1;
            if(o1.getRating() > o2.getRating()) return -1;
            return 0;
        });
        top10 = cars.subList(0,10);
        return top10;
    }

    public List<Car> getTop10_VisualAspect() {
        List<Car> cars = this.getAllCars();
        List<Car> top10 = new ArrayList<>();
        Collections.sort(cars,(o1, o2) -> {
            if(o1.getVisualAspectAverage() < o2.getVisualAspectAverage()) return 1;
            if(o1.getVisualAspectAverage() > o2.getVisualAspectAverage()) return -1;
            return 0;
        });
        top10 = cars.subList(0,10);
        return top10;
    }

    public List<Car> getTop10_ValueForMoney() {
        List<Car> cars = this.getAllCars();
        List<Car> top10 = new ArrayList<>();
        Collections.sort(cars,(o1, o2) -> {
            if(o1.getValueForMoneyAverage() < o2.getValueForMoneyAverage()) return 1;
            if(o1.getValueForMoneyAverage() > o2.getValueForMoneyAverage()) return -1;
            return 0;
        });
        top10 = cars.subList(0,10);
        return top10;
    }

    public List<Car> getTop10_Performance() {
        List<Car> cars = this.getAllCars();
        List<Car> top10 = new ArrayList<>();
        Collections.sort(cars,(o1, o2) -> {
            if(o1.getPerformanceAverage() < o2.getPerformanceAverage()) return 1;
            if(o1.getPerformanceAverage() > o2.getPerformanceAverage()) return -1;
            return 0;
        });
        top10 = cars.subList(0,10);
        return top10;
    }
}

