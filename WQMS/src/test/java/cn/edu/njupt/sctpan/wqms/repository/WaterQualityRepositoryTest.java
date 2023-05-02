package cn.edu.njupt.sctpan.wqms.repository;

import cn.edu.njupt.sctpan.wqms.model.WaterQuality;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class WaterQualityRepositoryTest {
    @Autowired
    private WaterQualityRepository waterQualityRepository;
    @Test
    public void findByDateBetweenAndStation() {
        Date start = null;
        Date end = null;
        try {
            start = DateFormat.getDateInstance().parse("2002-01-22");
            end = DateFormat.getDateInstance().parse("2006-10-22");
        } catch (Exception e) {
            e.printStackTrace();
        }
        List<WaterQuality> res = waterQualityRepository
                .findByDateBetweenAndStationOrderByDate(start, end, 8);
        for(WaterQuality waterQuality : res) {
            System.out.println(waterQuality);
        }
    }

    @Test
    public void findAllStations() {
        List<Integer> stations = waterQualityRepository.findAllStations();
        for(Integer station : stations) {
            System.out.println(station);
        }
    }

    @Test
    public void findByStationOrderByDateDesc() {
        Pageable p = PageRequest.of(0, 12);
        List<WaterQuality> res = waterQualityRepository.findByStationOrderByDateDesc(5, p);
        Collections.reverse(res);
        for(WaterQuality waterQuality : res) {
            System.out.println(waterQuality);
        }
    }

    @Test
    public void findLastDates() {
        Pageable p = PageRequest.of(0, 5);
        List<String> res = waterQualityRepository.findLastDates(p);
        for(String s : res) {
            System.out.println(s);
        }
    }

    @Test
    public void findBySpecificDate() {
        String dateStr = "2006-11-01";
        String startDateStr = "2006-11-01" + " 00:00:00";
        String endDateStr = "2006-12-01" + " 23:59:59";
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date startDate = null;
        Date endDate = null;
        try {
           startDate = dateFormat.parse(startDateStr);
           endDate = dateFormat.parse(endDateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        System.out.println(startDateStr);
        System.out.println(endDateStr);
        List<WaterQuality> res = waterQualityRepository.findBySpecificDate(startDate, endDate);
        for (WaterQuality waterQuality : res) {
            System.out.println("water quality: " + waterQuality);
        }
    }
}