package com.example.chongchat.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.chongchat.entity.Message;
import com.example.chongchat.mapper.MessageMapper;
import com.example.chongchat.service.MessageService;
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
public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message> implements MessageService {

}
