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

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

@Service
@AllArgsConstructor
public class MercedesAPIService {
    private final RestTemplate restTemplate = new RestTemplate();
    private final String baseURL = "https://api.mercedes-benz.com/configurator/v1";
//    private final String baseURL = "https://api.mercedes-benz.com/configurator_tryout/v1";
    private final String apiKey = "ba307f29-7f19-4b33-b2ad-940e122fc7f6";
//    private final String apiKey = "Tyt82ndiKG0AdH8TCqe001ROh7RsGOKB";

    public String appendApiKey(String url) {
        return url + "apikey="+apiKey;
    }
    public Car getModelsForMarket() {
        String url = baseURL + "/markets/pl_PL/models?";
        url = appendApiKey(url);
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        JSONArray jsonArray = parseResponseEntityArray(response);
        JSONObject carFromApi = (JSONObject) jsonArray.get(0);
        String modelId = carFromApi.get("modelId").toString();
        String name = carFromApi.get("name").toString();;
        var vehicleBodyWrapper = (JSONObject) carFromApi.get("vehicleBody");
        String bodyName = vehicleBodyWrapper.get("bodyName").toString();;
        var vehicleClassWrapper = (JSONObject) carFromApi.get("vehicleClass");
        String className = vehicleClassWrapper.get("className").toString();;
        var priceInformationWrapper = (JSONObject) carFromApi.get("priceInformation");
        Long price = (Long) priceInformationWrapper.get("price");
//        Car car = Car.builder().modelId(modelId).name(name).bodyName(bodyName).className(className).price(price)

        var linksWrapper = (JSONObject)carFromApi.get("_links");
        var configurationsLink = linksWrapper.get("configurations").toString();

        response = restTemplate.getForEntity(configurationsLink, String.class);
        JSONObject carConfiguration = parseResponseEntity(response);

        JSONObject technicalInformation = (JSONObject) carConfiguration.get("technicalInformation");
//    Technical information
        var accelerationWrapper = (JSONObject) technicalInformation.get("acceleration");
        var acceleration = (Double) accelerationWrapper.get("value");

        var topSpeedWrapper = (JSONObject) technicalInformation.get("topSpeed");
        var topSpeed = (Long) topSpeedWrapper.get("value");

        var doors = (Long) technicalInformation.get("doors");
        var seats = (Long) technicalInformation.get("seats");

        var actualMassWrapper = (JSONObject) technicalInformation.get("actualMass");
        var mass = (Long) actualMassWrapper.get("value");

//        Transmission Model
        var transmissionWrapper = (JSONObject) technicalInformation.get("transmission");
        var transmissionName = transmissionWrapper.get("name");
        var transmissionType = transmissionWrapper.get("transmissionType");
        Transmission transmission = Transmission.builder()
                .name(transmissionName.toString())
                .type(transmissionType.toString())
                .build();
//  Engine
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
        Engine engine = Engine.builder()
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
        CarTechnicalInformation technical = CarTechnicalInformation.builder()
                .acceleration(acceleration)
                .topSpeed(topSpeed)
                .doors(doors)
                .seats(seats)
                .mass(mass)
                .engine(engine)
                .transmission(transmission).build();


        var d = (JSONObject) carConfiguration.get("_links");
        var e = d.get("image").toString();
        System.out.println(e);
        response = restTemplate.getForEntity(e, String.class);
        JSONObject jsonObject1 = parseResponseEntity(response);
        var f = (JSONObject) jsonObject1.get("vehicle");
        Map<String,String> imagesUrls = new HashMap<String,String>();
        f.keySet().forEach(keyStr ->
        {
            Object keyvalue = f.get(keyStr);
//            System.out.println("key: "+ keyStr + " value: " + keyvalue);

            //for nested objects iteration if required
            if (keyvalue instanceof JSONObject)
               ((JSONObject) keyvalue).keySet().forEach(keyStr1 -> {
                   Object keyvalue1 = ((JSONObject) keyvalue).get(keyStr1);
                   if(keyStr1.toString().equals("url")){
                       imagesUrls.put(keyStr.toString(),keyvalue1.toString());
//                       imagesUrls.add(keyvalue1.toString());
                   }
                   System.out.println("key: "+ keyStr1 + " value: " + keyvalue1);
               });
        });
        Car car = Car.builder()
                .modelId(modelId)
                .name(name)
                .bodyName(bodyName)
                .className(className)
                .price(price)
                .carTechnicalInformation(technical)
                .imagesUrls(imagesUrls)
                .build();
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
    public String printJsonObject(JSONObject jsonObj) {
        AtomicReference<String> s = new AtomicReference<>("");
        jsonObj.keySet().forEach(keyStr ->
        {
            Object keyvalue = jsonObj.get(keyStr);
            System.out.println("key: "+ keyStr + " value: " + keyvalue);
            if(keyStr=="url") {
                s.set(keyvalue.toString());
            }
        });
        return s.toString();
    }
}
