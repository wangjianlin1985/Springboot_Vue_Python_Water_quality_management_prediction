package cn.edu.njupt.sctpan.wqms.controller;

import cn.edu.njupt.sctpan.wqms.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/status")
public class ErrorController {

    @Autowired
    private UserService userService;

    @GetMapping("/noLogin")
    public Map<String, Object> noLogin() {
        Map<String, Object> resp = new HashMap<>();
        resp.put("status", "noLogin");
        return resp;
    }
}
