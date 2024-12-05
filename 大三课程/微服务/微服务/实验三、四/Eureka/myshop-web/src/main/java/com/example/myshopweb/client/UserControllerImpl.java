package com.example.myshopweb.client;

import com.example.myshopweb.pojo.User;
import org.springframework.stereotype.Component;

@Component
public class UserControllerImpl implements UserController {
    @Override
    public User findById(Integer id) {
        System.out.println("我执行了熔断方法");
        return null;
    }
}
