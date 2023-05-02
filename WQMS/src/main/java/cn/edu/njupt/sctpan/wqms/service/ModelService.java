package cn.edu.njupt.sctpan.wqms.service;

import cn.edu.njupt.sctpan.wqms.model.Model;
import cn.edu.njupt.sctpan.wqms.repository.ModelRepository;
import cn.edu.njupt.sctpan.wqms.repository.UserRepository;
import cn.edu.njupt.sctpan.wqms.repository.WaterQualityRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class ModelService {
    @Autowired
    private ModelRepository modelRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private WaterQualityService waterQualityService;

    private Map<String, Object> parseTrainingResponse(String response) {
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> resp = new HashMap<>();
        Map<String, Object> data = new HashMap<>();
        JsonNode rootNode = null;
        try {
            rootNode = objectMapper.readTree(response);
        } catch (IOException e) {
            e.printStackTrace();
            resp.put("status", "failure");
            return resp;
        }
        String status = rootNode.path("status").asText();
        if(!status.equalsIgnoreCase("success")) {
            resp.put("status", "failure");
            return resp;
        }
        JsonNode dataNode = rootNode.path("data");
        Double rmse = dataNode.path("rmse").asDouble();
        List<Double> pred = new ArrayList<>();
        List<Double> real = new ArrayList<>();
        JsonNode predNode = dataNode.path("pred");
        JsonNode realNode = dataNode.path("real");
        for(int i=0; i<predNode.size(); i++) {
            Double predValue = predNode.get(i).asDouble();
            Double realValue = realNode.get(i).asDouble();
            pred.add(predValue);
            real.add(realValue);
        }
        data.put("rmse", rmse);
        data.put("pred", pred);
        data.put("real", real);
        resp.put("data", data);
        return resp;
    }

    private Map<String, Object> parsePredictionResponse(String response) {
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> resp = new HashMap<>();
        JsonNode rootNode = null;
        try {
            rootNode = objectMapper.readTree(response);
        } catch (IOException e) {
            e.printStackTrace();
            resp.put("status", "failure");
            return resp;
        }
        String status = rootNode.path("status").asText();
        if(status.equalsIgnoreCase("failure")) {
            resp.put("status", "failure");
            return resp;
        }
        JsonNode dataNode = rootNode.path("data");
        Double prediction = dataNode.path("pred").asDouble();
        resp.put("pred", prediction);
        return resp;
    }

    private String sendRequest(String url) {
        HttpClient httpclient = HttpClients.createDefault();
        HttpGet httpget = new HttpGet(url);
        String resp = null;
        try {
            HttpResponse response = httpclient.execute(httpget);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                resp = EntityUtils.toString(entity, "UTF-8").trim();

            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            httpget.abort();
        }
        return resp;
    }

    private Map<String, Object> sendTrainingRequest(int modelId) {
        String url = "http://localhost:8000/api/training?id=" + modelId;
        String jsonResp = sendRequest(url);
        return parseTrainingResponse(jsonResp);
    }

    private Map<String, Object> sendPredictionRequest(int modelId, String indicator) {
        Map<String, Object> map = waterQualityService.getDateForPrediction(indicator);
        List<Float> forPlot = (List<Float>) map.get("forPlot");
        List<Float> forPrediction = (List<Float>) map.get("forPrediction");
        List<String> dates = (List<String>) map.get("dates");
        String startDate = dates.get(2);
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date date = null;
        try {
            date = dateFormat.parse(startDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int startMonth = calendar.get(Calendar.MONTH) + 1;
        String url = "http://localhost:8000/api/prediction?id=" + modelId;
        url = url + "&one=" + forPrediction.get(0) + "&two=" +
                forPrediction.get(1) + "&three=" + forPrediction.get(2) + "&startMonth=" + startMonth;
        System.out.println(url);
        String jsonResp = sendRequest(url);
        Map<String, Object> resp = parsePredictionResponse(jsonResp);
        Double d = (Double) resp.get("pred");
        forPlot.add(d.floatValue());
        calendar.add(Calendar.MONTH, 3);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        dates.add(df.format(calendar.getTime()));
        resp.put("forPlot", forPlot);
        resp.put("dates", dates);
        return resp;
    }

    public Map<String, Object> predictNextMonth(int modelId, String indicator) {
        Map<String, Object> resp = null;
        try {
            resp = sendPredictionRequest(modelId, indicator);
        } catch (RuntimeException e) {
            e.printStackTrace();
            resp = new HashMap<>();
            resp.put("status", "failure");
            return resp;
        }
        if(!resp.containsKey("status")) {
            resp.put("status", "success");
        }
        return resp;
    }

    public Map<String, Object> trainModel(String indicator, String method, Integer uid) {
        String modelName = indicator + "_" + method;
        Model model = new Model();
        Map<String, Object> resp = null;
        try {
            model.setDate(new Date());
            model.setName(modelName);
            model.setTarget(indicator);
            model.setMethod(method);
            model.setUser(userRepository.findUserById(uid));
            model = modelRepository.save(model);
            resp = sendTrainingRequest(model.getId());
            if(resp == null) {
                resp = new HashMap<>();
                resp.put("status", "failure");
                modelRepository.delete(model);
            } else {
                Map<String, Object> data = (Map<String, Object>)resp.get("data");
                Double rmse = (Double)data.get("rmse");
                model.setRmse(rmse.floatValue());
                modelRepository.save(model);
            }
        } catch (Exception e) {
            e.printStackTrace();
            resp = new HashMap<>();
            resp.put("status", "failure");
            modelRepository.delete(model);
        }
        if(!resp.containsKey("status")) {
            resp.put("status", "success");
        }
        return resp;
    }

    public List<Model> getAvailableModel(String indicator) {
        return modelRepository.findModelByTargetOrderByRmseAsc(indicator);
    }

    public List<Model> getAvailableModel(String indicator, String method) {
        return modelRepository.findModelByTargetAndMethodOrderByRmseAsc(indicator, method);
    }

    public Boolean deleteModel(int id) {
        try {
            Model model = modelRepository.findModelById(id);
            modelRepository.delete(model);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public List<String> getAllModelsByTarget(String target) {
        return modelRepository.findAllModelsByTarget(target);
    }
}
