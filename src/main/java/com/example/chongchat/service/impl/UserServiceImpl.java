package com.example.chongchat.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.chongchat.entity.User;
import com.example.chongchat.mapper.UserMapper;
import com.example.chongchat.service.UserService;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author chongpenghao
 * @since 2022-07-30
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

}
