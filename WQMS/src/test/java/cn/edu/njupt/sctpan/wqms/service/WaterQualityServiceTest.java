package cn.edu.njupt.sctpan.wqms.service;

import cn.edu.njupt.sctpan.wqms.model.WaterQuality;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class WaterQualityServiceTest {
    @Autowired
    private WaterQualityService waterQualityService;
    @Test
    public void findAllWaterQualities() {
        List<WaterQuality> res = waterQualityService.findAllWaterQualities();
        for(WaterQuality waterQuality : res) {
            System.out.println(waterQuality);
        }
    }

}