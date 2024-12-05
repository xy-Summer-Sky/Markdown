package com.example.myshopuser.dao;

import com.example.myshopuser.pojo.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDao extends JpaRepository<User,Integer> {}

