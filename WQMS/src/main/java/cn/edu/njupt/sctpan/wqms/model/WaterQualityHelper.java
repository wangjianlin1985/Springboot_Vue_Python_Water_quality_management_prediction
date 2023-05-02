package cn.edu.njupt.sctpan.wqms.model;

import java.text.DateFormat;
import java.text.ParseException;

public class WaterQualityHelper {

    private Integer id;
    private Float PH;
    private Float DO;
    private Float NH3N;
    private String date;
    private Integer station;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Float getPH() {
        return PH;
    }

    public void setPH(Float PH) {
        this.PH= PH;
    }

    public Float getDO() {
        return DO;
    }

    public void setDO(Float DO) {
        this.DO = DO;
    }

    public Float getNH3N() {
        return NH3N;
    }

    public void setNH3N(Float NH3N) {
        this.NH3N = NH3N;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Integer getStation() {
        return station;
    }

    public void setStation(Integer station) {
        this.station = station;
    }

    public WaterQuality convert()  {
        WaterQuality waterQuality = new WaterQuality();
        DateFormat dateFormat = DateFormat.getDateInstance();
        try {
            waterQuality.setDate(dateFormat.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
        waterQuality.setPH(PH);
        waterQuality.setNH3N(NH3N);
        waterQuality.setDO(DO);
        waterQuality.setStation(station);
        return waterQuality;
    }

    @Override
    public String toString() {
        return "WaterQualityHelper{" +
                "id=" + id +
                ", PH=" +  PH +
                ", DO=" +  DO +
                ", NH3N=" + NH3N +
                ", date='" + date + '\'' +
                ", station=" + station +
                '}';
    }
}
