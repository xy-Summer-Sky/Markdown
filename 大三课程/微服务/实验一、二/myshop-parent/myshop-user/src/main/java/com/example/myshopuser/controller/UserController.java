package com.example.myshopuser.controller;

import com.example.myshopuser.pojo.User;
import com.example.myshopuser.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

//@RequestMapping("/user")
//@RestController
//public class UserController {
//    @RequestMapping(method = RequestMethod.GET)
//    public List<User> findAll() {
//        List<User> list = new ArrayList<>();
//        list.add(new User(1, "张三", "123456", "男", 1000.0));
//        list.add(new User(2, "李四", "123456", "男", 2000.0));
//        list.add(new User(3, "王五", "123456", "男", 2500.0));
//        return list;
//    }
//}
@RequestMapping("/user")
@RestController   // @RestController=@RequestMapping + @ResponseBody
//@Api(description = "用户控制器")
public class UserController {
    @Autowired
    private UserService userService;
    /*** 查询所有用户     */
    @RequestMapping(method = RequestMethod.GET)
    public List<User> findAll(){
        return userService.findAll();
    }
    /*** 根据id查询用户     */
    @RequestMapping(value = "/{id}",method = RequestMethod.GET)
    public User findById(@PathVariable Integer id){
        return userService.findById(id);    }
    /*** 添加用户     */
    @RequestMapping(method = RequestMethod.POST)
    public String add(@RequestBody User user){
        userService.add(user);
        return "添加成功";    }
    /*** 修改用户     */
    @RequestMapping(value = "/{id}",method = RequestMethod.PUT)
    public String update(@RequestBody User user,@PathVariable Integer id){
        //设置id
        user.setId(id);
        userService.update(user);
        return "修改成功";    }
    /*** 根据id删除用户     */
    @RequestMapping(value = "/{id}",method = RequestMethod.DELETE)
    public String deleteById(@PathVariable Integer id){
        userService.deleteById(id);
        return "删除成功";    }
}
