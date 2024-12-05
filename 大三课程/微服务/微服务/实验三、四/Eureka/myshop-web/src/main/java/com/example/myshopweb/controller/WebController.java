package com.example.myshopweb.controller;

import com.example.myshopweb.client.UserController;
import com.example.myshopweb.client.UserControllerImpl;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import com.example.myshopweb.pojo.User;

import javax.annotation.Resource;
import java.util.List;

@RequestMapping("/web")
@RestController
@Api(description="购票web核心Api")
public class WebController {

    @Autowired
    private RestTemplate restTemplate;

    @Resource
    private UserController userController;

    @RequestMapping(value = "远程方法：根据用户ID查询用户的方法", method=RequestMethod.GET)
    @ApiOperation(value="/order", httpMethod = "GET", notes="购票")
    @HystrixCommand(fallbackMethod = "orderFallback")
    public String order() {

        Integer id = 2;
        User user= userController.findById(id);

        System.out.println(user+"==正在购票..");
        return "购票成功";

    }

    public String orderFallback() {
        return "购票失败,服务熔断";
    }
}
