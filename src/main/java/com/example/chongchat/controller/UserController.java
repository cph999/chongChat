package com.example.chongchat.controller;


import com.example.chongchat.entity.User;
import com.example.chongchat.service.UserService;
import com.example.chongchat.utils.RedisUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author chongpenghao
 * @since 2022-07-30
 */
@RestController
@RequestMapping("/user")
@Api(tags = "User控制器")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private RedisTemplate redisTemplate;

    @GetMapping("/add")
    @ApiOperation("register api")
    public String add(){
        User cph = User.builder().username("cph").password("123").build();
        redisTemplate.opsForZSet().add("ranking-list", "p1", 2.1);
        redisTemplate.opsForZSet().add("ranking-list", 1, 10.1);
        redisTemplate.opsForZSet().add("ranking-list", "操", 19.1);
        Set list = redisTemplate.opsForZSet().reverseRange("ranking-list", 0, 30);
        if(list != null){
            list.forEach(System.out::println);
        }
        userService.save(cph);
        return "success";
    }
}

