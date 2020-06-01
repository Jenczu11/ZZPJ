package pl.lodz.p.it.mercedes.services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.web.server.ResponseStatusException;
import pl.lodz.p.it.mercedes.model.Car;
import pl.lodz.p.it.mercedes.model.CarTechnicalInformation;
import pl.lodz.p.it.mercedes.model.Engine;
import pl.lodz.p.it.mercedes.model.Transmission;
import pl.lodz.p.it.mercedes.model.engines.Diesel;
import pl.lodz.p.it.mercedes.model.engines.EngineFactory;
import pl.lodz.p.it.mercedes.repositories.CarRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Service
@AllArgsConstructor
public class MercedesAPIService {

    private final CarRepository carRepository;
    private final RestTemplate restTemplate = new RestTemplate();

    private final String baseURL = "https://api.mercedes-benz.com/configurator/v1";
    private final String apiKey = "null";

    public String appendApiKey(String url, String apiKey) {
        if (Objects.equals(apiKey, "null")) {
            return url + "apikey=" + this.apiKey;
        }
        return url + "apikey=" + apiKey;
    }

    public Car getCar(int index, String apiKey) {
        String url = baseURL + "/markets/pl_PL/models?";
        url = appendApiKey(url, apiKey);

        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        JSONArray jsonArray = parseResponseEntityArray(response);

        JSONObject carFromApi = (JSONObject) jsonArray.get(index);

        String modelId = carFromApi.get("modelId").toString();
        String name = carFromApi.get("name").toString();

        var vehicleBodyWrapper = (JSONObject) carFromApi.get("vehicleBody");
        String bodyName = vehicleBodyWrapper.get("bodyName").toString();

        var vehicleClassWrapper = (JSONObject) carFromApi.get("vehicleClass");
        String className = vehicleClassWrapper.get("className").toString();

        var priceInformationWrapper = (JSONObject) carFromApi.get("priceInformation");
        Double price = parseNumber(priceInformationWrapper.get("price"));

        var linksWrapper = (JSONObject) carFromApi.get("_links");
        var configurationsLink = linksWrapper.get("configurations").toString();

        response = restTemplate.getForEntity(configurationsLink, String.class);
        JSONObject carConfiguration = parseResponseEntity(response);
        CarTechnicalInformation technical = parseCarTechnicalInformation(carConfiguration);

        var imagesLinksWrapper = (JSONObject) carConfiguration.get("_links");
        var imagesLinks = imagesLinksWrapper.get("image").toString();

        Map<String, String> imagesUrls = new HashMap<String, String>();
        String linkToImages = imagesLinks.replaceAll("(apikey.*)", "");
//        String linkToImages = imagesLinks;
        imagesUrls.put("base", linkToImages);

        try {
            ResponseEntity<String> imageRes = restTemplate.getForEntity(imagesLinks, String.class);
            JSONObject imagesResponse = parseResponseEntity(imageRes);
            JSONObject imagesJson = (JSONObject) imagesResponse.get("vehicle");
            imagesJson.keySet().forEach(keyStr ->
            {
                Object nestedURL = imagesJson.get(keyStr);

                // For nested objects iteration if required.
                if (nestedURL instanceof JSONObject)
                    ((JSONObject) nestedURL).keySet().forEach(key -> {
                        Object imageURL = ((JSONObject) nestedURL).get(key);
                        if (key.toString().equals("url")) {
                            imagesUrls.put(keyStr.toString(), imageURL.toString());
                        }
                    });
            });
        } catch (HttpServerErrorException e) {
            log.error("Error when getting images from api for car " + name + ". " + e);
        }

        return Car.builder()
                .modelId(modelId)
                .name(name)
                .bodyName(bodyName)
                .className(className)
                .price(price)
                .carTechnicalInformation(technical)
                .imagesUrls(imagesUrls)
                .rating(0.0)
                .numberOfRatings(0)
                .build();
    }

    public Car saveCar(int index, String apiKey) {
        Car car = getCar(index, apiKey);
        carRepository.save(car);
        return car;
    }

    public String populateDB(int start, int end, String apiKey) {
        int i;
        int howMany = 0;
        for (i = start; i < end; i++, howMany++) {
            try {
                saveCar(i, apiKey);
            } catch (HttpClientErrorException.TooManyRequests e) {
                String message = "To many requests. {" + "Added " + howMany + " vehicles from id " + start + " to " + (end - howMany) + " } ";
                log.error(message + e);
                return message;
            }
        }
        String message = "Added " + howMany + " vehicles from id " + start + " to " + end + ".";
        log.info(message);
        return message;
    }

    private JSONArray parseResponseEntityArray(ResponseEntity<String> response) {
        JSONParser jp = new JSONParser();
        JSONArray jsonArray = null;
        try {
            jsonArray = (JSONArray) jp.parse(response.getBody());
        } catch (ParseException e) {
            log.error("Error when parsing response entity. " + e);
        }
        return jsonArray;
    }

    private JSONObject parseResponseEntity(ResponseEntity<String> response) {
        JSONParser jp = new JSONParser();
        JSONObject jsonObject = null;
        try {
            jsonObject = (JSONObject) jp.parse(response.getBody());
        } catch (ParseException e) {
            log.error("Error when parsing response entity. " + e);
        }
        return jsonObject;
    }

    public CarTechnicalInformation parseCarTechnicalInformation(JSONObject carConfiguration) {

        JSONObject technicalInformation = (JSONObject) carConfiguration.get("technicalInformation");

        // Technical information.
        var accelerationWrapper = (JSONObject) technicalInformation.get("acceleration");
        var acceleration = parseNumber(accelerationWrapper.get("value"));

        var topSpeedWrapper = (JSONObject) technicalInformation.get("topSpeed");
        var topSpeed = parseNumber(topSpeedWrapper.get("value"));

        var doors = parseNumber(technicalInformation.get("doors"));
        var seats = parseNumber(technicalInformation.get("seats"));

        var actualMassWrapper = (JSONObject) technicalInformation.get("actualMass");
        var mass = parseNumber(actualMassWrapper.get("value"));

        Transmission transmission = parseTransmission(technicalInformation);

        Engine engine = parseEngine(technicalInformation);

        return CarTechnicalInformation.builder()
                .acceleration(acceleration)
                .topSpeed(topSpeed)
                .doors(doors)
                .seats(seats)
                .mass(mass)
                .engine(engine)
                .transmission(transmission).build();
    }

    public Transmission parseTransmission(JSONObject technicalInformation) {
        var transmissionWrapper = (JSONObject) technicalInformation.get("transmission");
        var transmissionName = transmissionWrapper.get("name");
        var transmissionType = transmissionWrapper.get("transmissionType");
        return Transmission.builder()
                .name(transmissionName.toString())
                .type(transmissionType.toString())
                .build();
    }

    public Engine parseEngine(JSONObject technicalInformation) {
        var engineWrapper = (JSONObject) technicalInformation.get("engine");

        String fuelType = (String) engineWrapper.get("fuelType");
        String emissionStandard = (String) engineWrapper.get("emissionStandard");

        JSONObject powerHpWrapper = (JSONObject) engineWrapper.get("powerHp");
        Double powerHp = parseNumber(powerHpWrapper.get("value"));

        JSONObject powerKwWrapper = (JSONObject) engineWrapper.get("powerKw");
        Double powerKw = parseNumber(powerKwWrapper.get("value"));

        String cylinder = "---";
        try {
            cylinder = engineWrapper.get("cylinder").toString();
        } catch (Exception e) {
            log.error("Error when parsing cylinder from api. " + e);
        }

        JSONObject cylinderValvesWrapper = (JSONObject) engineWrapper.get("cylinderValves");
        Double cylinderValves = 0.0;
        try {
            cylinderValves = parseNumber(cylinderValvesWrapper.get("value"));
        } catch (Exception e) {
            log.error("Error when parsing cylinder values from api. " + e);
        }
        JSONObject capacityWrapper = (JSONObject) engineWrapper.get("capacity");
        Double capacity = parseNumber(capacityWrapper.get("value"));

        String driveType = (String) engineWrapper.get("typeOfPropulsion");

        JSONObject fuelEconomy = (JSONObject) engineWrapper.get("fuelEconomy");
        JSONObject fuelConsumptionMinWrapper = (JSONObject) fuelEconomy.get("fuelConsumptionCombinedMin");
        Double fuelConsumptionMin = parseNumber(fuelConsumptionMinWrapper.get("value"));
        String fuelConsumption = fuelConsumptionMin.toString();

        return EngineFactory.getEngine(
                fuelType,
                emissionStandard,
                driveType,
                fuelConsumption,
                powerHp,
                powerKw,
                cylinder,
                cylinderValves,
                capacity);
    }

    public Double parseNumber(Object object) {
        return Double.parseDouble(object.toString());
    }
}
