package pl.lodz.p.it.mercedes.services;

import lombok.AllArgsConstructor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import pl.lodz.p.it.mercedes.model.Car;
import pl.lodz.p.it.mercedes.model.CarTechnicalInformation;
import pl.lodz.p.it.mercedes.model.Engine;
import pl.lodz.p.it.mercedes.model.Transmission;
import pl.lodz.p.it.mercedes.repositories.CarRepository;

import java.util.HashMap;
import java.util.Map;


@Service
@AllArgsConstructor
public class MercedesAPIService {
    private final CarRepository carRepository;
    private final RestTemplate restTemplate = new RestTemplate();
    private final String baseURL = "https://api.mercedes-benz.com/configurator/v1";
//    private final String baseURL = "https://api.mercedes-benz.com/configurator_tryout/v1";
    private final String apiKey = "ba307f29-7f19-4b33-b2ad-940e122fc7f6";
//    private final String apiKey = "Tyt82ndiKG0AdH8TCqe001ROh7RsGOKB";

    public String appendApiKey(String url) {
        return url + "apikey="+apiKey;
    }

    public Car getCar(int index) {
        String url = baseURL + "/markets/pl_PL/models?";
        url = appendApiKey(url);

        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        JSONArray jsonArray = parseResponseEntityArray(response);

        JSONObject carFromApi = (JSONObject) jsonArray.get(index);

        String modelId = carFromApi.get("modelId").toString();
        String name = carFromApi.get("name").toString();

        var vehicleBodyWrapper = (JSONObject) carFromApi.get("vehicleBody");
        String bodyName = vehicleBodyWrapper.get("bodyName").toString();;

        var vehicleClassWrapper = (JSONObject) carFromApi.get("vehicleClass");
        String className = vehicleClassWrapper.get("className").toString();;

        var priceInformationWrapper = (JSONObject) carFromApi.get("priceInformation");
        Long price = (Long) priceInformationWrapper.get("price");

        var linksWrapper = (JSONObject)carFromApi.get("_links");
        var configurationsLink = linksWrapper.get("configurations").toString();

        response = restTemplate.getForEntity(configurationsLink, String.class);
        JSONObject carConfiguration = parseResponseEntity(response);
        CarTechnicalInformation technical = parseCarTechnicalInformation(carConfiguration);

        var imagesLinksWrapper = (JSONObject) carConfiguration.get("_links");
        var imagesLinks = imagesLinksWrapper.get("image").toString();

        response = restTemplate.getForEntity(imagesLinks, String.class);
        JSONObject imagesResponse = parseResponseEntity(response);
        JSONObject imagesJson = (JSONObject) imagesResponse.get("vehicle");
        Map<String,String> imagesUrls = new HashMap<String,String>();
        imagesJson.keySet().forEach(keyStr ->
        {
            Object nestedURL = imagesJson.get(keyStr);
            //for nested objects iteration if required
            if (nestedURL instanceof JSONObject)
               ((JSONObject) nestedURL).keySet().forEach(key -> {
                   Object imageURL = ((JSONObject) nestedURL).get(key);
                   if(key.toString().equals("url")){
                       imagesUrls.put(keyStr.toString(),imageURL.toString());
                   }
               });
        });
        return Car.builder()
                .modelId(modelId)
                .name(name)
                .bodyName(bodyName)
                .className(className)
                .price(price)
                .carTechnicalInformation(technical)
                .imagesUrls(imagesUrls)
                .build();
    }
    public Car saveCar(int index) {
        Car car = getCar(index);
        carRepository.save(car);
        return car;
    }
    private JSONArray parseResponseEntityArray(ResponseEntity<String> response){
        JSONParser jp = new JSONParser();
        JSONArray jsonArray = null;
        try {
            jsonArray = (JSONArray) jp.parse(response.getBody());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return jsonArray;
    }
    private JSONObject parseResponseEntity(ResponseEntity<String> response){
        JSONParser jp = new JSONParser();
        JSONObject jsonObject = null;
        try {
            jsonObject = (JSONObject) jp.parse(response.getBody());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }
    public CarTechnicalInformation parseCarTechnicalInformation(JSONObject carConfiguration){

        JSONObject technicalInformation = (JSONObject) carConfiguration.get("technicalInformation");
        //Technical information
        var accelerationWrapper = (JSONObject) technicalInformation.get("acceleration");
        var acceleration = (Double) accelerationWrapper.get("value");

        var topSpeedWrapper = (JSONObject) technicalInformation.get("topSpeed");
        var topSpeed = (Long) topSpeedWrapper.get("value");

        var doors = (Long) technicalInformation.get("doors");
        var seats = (Long) technicalInformation.get("seats");

        var actualMassWrapper = (JSONObject) technicalInformation.get("actualMass");
        var mass = (Long) actualMassWrapper.get("value");

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
        var engineWrapper =  (JSONObject) technicalInformation.get("engine");

        var fuelType = engineWrapper.get("fuelType"); //String
        var emissionStandard = engineWrapper.get("emissionStandard"); //String

        var powerHpWrapper = (JSONObject) engineWrapper.get("powerHp");
        var powerHp = (Long) powerHpWrapper.get("value"); // Long

        var powerKwWrapper = (JSONObject) engineWrapper.get("powerKw");
        var powerKw = (Long) powerKwWrapper.get("value"); // Long

        var cylinder = engineWrapper.get("cylinder"); //String

        var cylinderValvesWrapper = (JSONObject) engineWrapper.get("cylinderValves");
        var cylinderValves = (Long) cylinderValvesWrapper.get("value"); //Long

        var capacityWrapper = (JSONObject) engineWrapper.get("capacity");
        var capacity = (Long) capacityWrapper.get("value"); //Long

        var driveType = engineWrapper.get("typeOfPropulsion"); //string

        var fuelEconomy = (JSONObject) engineWrapper.get("fuelEconomy");
        var fuelConsumptionMinWrapper = (JSONObject) fuelEconomy.get("fuelConsumptionCombinedMin");
        var fuelConsumptionMin = (Double) fuelConsumptionMinWrapper.get("value"); // Double
        return Engine.builder()
                .fuelType(fuelType.toString())
                .emissionStandard(emissionStandard.toString())
                .powerHp(powerHp)
                .powerKw(powerKw)
                .cylinder(cylinder.toString())
                .cylinderValves(cylinderValves)
                .capacity(capacity)
                .driveType(driveType.toString())
                .fuelConsumptionMin(fuelConsumptionMin)
                .build();
    }

}
