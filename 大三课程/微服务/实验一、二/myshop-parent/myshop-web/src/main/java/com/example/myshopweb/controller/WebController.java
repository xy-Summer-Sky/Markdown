package com.example.myshopweb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import com.example.myshopweb.pojo.User;
@RequestMapping("/web")
@RestController
public class WebController {
    /* * * 购票方法     */
//    @RequestMapping(value = "/order",method = RequestMethod.GET)
//    public String order(){
//        //模拟当前用户
//        Integer id = 2;
//        //远程调用用户微服务findbyid方法，第四步补充
//        System.out.println("==正在购票...");
//        return "购票成功";
//    }

    @Autowired
    private RestTemplate restTemplate;

    @RequestMapping(value = "/order", method=RequestMethod.GET)
    public String order() {

        Integer id = 2;
        User user=restTemplate.getForObject("http://localhost:9001/user/" + id, User.class);
        System.out.println(user.getUsername() + "=正在购票.，.");
        System.out.println("==正在购票..");
        return "购票成功";

    }

    ;
}
