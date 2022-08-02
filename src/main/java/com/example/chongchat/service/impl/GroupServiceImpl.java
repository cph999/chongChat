package com.example.chongchat.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.chongchat.entity.Group;
import com.example.chongchat.mapper.GroupMapper;
import com.example.chongchat.service.GroupService;
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
public class GroupServiceImpl extends ServiceImpl<GroupMapper, Group> implements GroupService {

}
