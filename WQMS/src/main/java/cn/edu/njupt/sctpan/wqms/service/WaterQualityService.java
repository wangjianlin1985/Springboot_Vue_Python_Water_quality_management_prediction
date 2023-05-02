package cn.edu.njupt.sctpan.wqms.service;

import cn.edu.njupt.sctpan.wqms.model.WaterQuality;
import cn.edu.njupt.sctpan.wqms.repository.WaterQualityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class WaterQualityService {
    @Autowired
    private WaterQualityRepository waterQualityRepository;

    public List<WaterQuality> findAllWaterQualities() {
        List<WaterQuality> res = waterQualityRepository.findAllByOrderByDate();
        return res;
    }

    public List<WaterQuality> findQueriedWaterQualities(String startDateStr, String endDateStr, Integer station) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date startDate = null;
        Date endDate = null;
        try {
            startDate = df.parse(startDateStr);
            endDate = df.parse(endDateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if(station == -1) {
            return waterQualityRepository.findByDateBetweenOrderByDate(startDate, endDate);
        }
        return waterQualityRepository.findByDateBetweenAndStationOrderByDate(startDate, endDate, station);
    }

    public Boolean updateWaterQuality(WaterQuality waterQuality) {
        try {
            waterQualityRepository.save(waterQuality);
        } catch (RuntimeException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public Boolean deleteWaterQuality(Integer id) {
        try {
            waterQualityRepository.deleteById(id);
        } catch (RuntimeException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public Boolean addWaterQuality(WaterQuality waterQuality) {
        try {
            waterQualityRepository.save(waterQuality);
        } catch (RuntimeException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public List<Integer> getAllStations() {
        return waterQualityRepository.findAllStations();
    }

    public Map<String, Object> getDataForPlot(int station, int period, String indicator) {
        Pageable pageable = PageRequest.of(0, period * 12);
        List<WaterQuality> total = waterQualityRepository.findByStationOrderByDateDesc(station, pageable);
        List<Float> specWaterQualities = new ArrayList<>();
        List<String> dates = new ArrayList<>();
        for(WaterQuality waterQuality : total) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(waterQuality.getDate());
            String dateStr = calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH) + 1);
            dates.add(dateStr);
            if(indicator.equalsIgnoreCase("ph")) {
                specWaterQualities.add(waterQuality.getPH());
            } else if(indicator.equalsIgnoreCase("do")) {
                specWaterQualities.add(waterQuality.getDO());
            } else {
                specWaterQualities.add(waterQuality.getNH3N());
            }
        }
        Collections.reverse(specWaterQualities);
        Collections.reverse(dates);
        Map<String, Object> res = new HashMap<>();
        res.put("waterquality", specWaterQualities);
        res.put("dates", dates);
        return res;
    }

    public Map<String, Object> getMeanDataForPredictionPlot(String indicator) {
        return null;
    }

    public List<WaterQuality> getRecentWaterQualities(int num) {
        Pageable pageable = PageRequest.of(0, num);
        List<WaterQuality> recent = waterQualityRepository.findAllByOrderByDateDesc(pageable);
        return recent;
    }

    public Map<String, Object> getDateForPrediction(String indicator) {
        Map<String, Object> res = new HashMap<>();
        Pageable pageable = PageRequest.of(0, 5);
        List<String> dateStrs = waterQualityRepository.findLastDates(pageable);
//        for(String dateStr : dateStrs) {
//            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//            dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
//            try {
//                System.out.println("parsed date: " + dateFormat.parse(dateStr));
//            } catch (ParseException e){
//                e.printStackTrace();
//            }
//
//        }
        List<Float> forPlot = new ArrayList<>();
        List<Float> forPrediction = new ArrayList<>();
        for(String dateStr : dateStrs) {
            List<WaterQuality> waterQualitiesByDate = getWaterQaulitiesBySpecificDate(dateStr);
            //System.out.println("size: " + waterQualitiesByDate.size());
            Float average = calAverage(indicator, waterQualitiesByDate);
            BigDecimal bigDecimal = new BigDecimal(average);
            forPlot.add(bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue());
        }
        for(int i=0; i<3; i++) {
            forPrediction.add(forPlot.get(i));
        }
        Collections.reverse(dateStrs);
        Collections.reverse(forPlot);
        Collections.reverse(forPrediction);
        res.put("dates", dateStrs);
        res.put("forPlot", forPlot);
        res.put("forPrediction", forPrediction);
        return res;
    }

    private float calAverage(String indicator, List<WaterQuality> waterQualities) {
        float sum = 0;
        for (WaterQuality waterQuality : waterQualities) {
            if(indicator.equalsIgnoreCase("PH")) {
                sum = sum + waterQuality.getPH();
            } else if(indicator.equalsIgnoreCase("DO")) {
                sum = sum + waterQuality.getDO();
            } else {
                sum = sum + waterQuality.getNH3N();
            }
        }
        return sum / waterQualities.size();
    }

    private List<WaterQuality> getWaterQaulitiesBySpecificDate(String dateStr) {
        String startDateStr = dateStr + " 00:00:00";
        String endDateStr = dateStr + " 23:59:59";
        System.out.println(startDateStr);
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date startDate = null;
        Date endDate = null;
        try {
            startDate = dateFormat.parse(startDateStr);
            System.out.println("startDate: " + startDate);
            endDate = dateFormat.parse(endDateStr);
            System.out.println("endDate: " + endDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        List<WaterQuality> res = waterQualityRepository.findBySpecificDate(startDate, endDate);
        System.out.println("specific dates size: " + res.size());
        return res;
    }

}
