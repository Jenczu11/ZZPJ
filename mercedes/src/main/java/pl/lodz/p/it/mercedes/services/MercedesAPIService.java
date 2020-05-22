package pl.lodz.p.it.mercedes.services;

import lombok.AllArgsConstructor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

@Service
@AllArgsConstructor
public class MercedesAPIService {
    private final RestTemplate restTemplate = new RestTemplate();
    private final String baseURL = "https://api.mercedes-benz.com/configurator/v1";
//    private final String baseURL = "https://api.mercedes-benz.com/configurator_tryout/v1";
    private final String apiKey = "---";
//    private final String apiKey = "Tyt82ndiKG0AdH8TCqe001ROh7RsGOKB";

    public String appendApiKey(String url) {
        return url + "apikey="+apiKey;
    }
    public String getModelsForMarket() {
        String url = baseURL + "/markets/pl_PL/models?";
        url = appendApiKey(url);
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        JSONArray jsonArray = parseResponseEntityArray(response);
        var a = (JSONObject) jsonArray.get(0);
        var b = (JSONObject)a.get("_links");
        var c = b.get("configurations").toString();
        response = restTemplate.getForEntity(c, String.class);
        JSONObject jsonObject = parseResponseEntity(response);
        var d = (JSONObject) jsonObject.get("_links");
        var e = d.get("image").toString();
        System.out.println(e);
        response = restTemplate.getForEntity(e, String.class);
        jsonObject = parseResponseEntity(response);
        var f = (JSONObject) jsonObject.get("vehicle");
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
        return imagesUrls.toString();
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
