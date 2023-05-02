package cn.edu.njupt.sctpan.wqms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.web.servlet.ServletComponentScan;


@SpringBootApplication
//@ServletComponentScan(basePackages = {"cn.edu.njupt.sctpan.wqms.filter"})
public class WqmsApplication {

    public static void main(String[] args) {
        SpringApplication.run(WqmsApplication.class, args);
    }

}
