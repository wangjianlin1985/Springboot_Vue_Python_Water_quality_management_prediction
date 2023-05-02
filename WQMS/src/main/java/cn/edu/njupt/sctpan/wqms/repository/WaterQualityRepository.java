package cn.edu.njupt.sctpan.wqms.repository;

import cn.edu.njupt.sctpan.wqms.model.WaterQuality;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import java.util.Date;
import java.util.List;

public interface WaterQualityRepository extends Repository<WaterQuality, Integer> {
    public List<WaterQuality> findAllByOrderByDate();
    public void save(WaterQuality waterQuality);
    public void deleteById(Integer id);
    public List<WaterQuality> findByDateBetweenOrderByDate(Date start, Date end);
    public List<WaterQuality> findByDateBetweenAndStationOrderByDate(Date start, Date end, Integer station);

    @Query("select distinct station from WaterQuality")
    public List<Integer> findAllStations();
    public List<WaterQuality> findByStationOrderByDateDesc(Integer station, Pageable pageable);
    public List<WaterQuality> findAllByOrderByDateDesc(Pageable pageable);

    @Query("select distinct DATE_FORMAT(date,'%Y-%m-%d') AS puredate from WaterQuality order by DATE_FORMAT(date,'%Y-%m-%d') desc")
    public List<String> findLastDates(Pageable pageable);

    @Query("select w from WaterQuality w where w.date between ?1 and ?2")
    public List<WaterQuality> findBySpecificDate(Date start, Date end);
}
