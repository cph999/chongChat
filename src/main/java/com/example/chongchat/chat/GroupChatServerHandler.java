package com.example.chongchat.chat;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.chongchat.annotation.MyLog;
import com.example.chongchat.chat.entity.MessagePojo;
import com.example.chongchat.entity.GroupUser;
import com.example.chongchat.entity.Message;
import com.example.chongchat.entity.User;
import com.example.chongchat.service.GroupService;
import com.example.chongchat.service.GroupUserService;
import com.example.chongchat.service.MessageService;
import com.example.chongchat.service.UserService;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionManager;

import java.util.*;

/**
 * 这是群聊系统
 * 如果要实现点对点聊天，可以用map实现
 */
@Component
@MyLog
public class GroupChatServerHandler extends SimpleChannelInboundHandler<MessagePojo.Message> {

    @Autowired
    MessageService messageService;
    @Autowired
    UserService userService;
    @Autowired
    GroupService groupService;
    @Autowired
    GroupUserService groupUserService;
    //定义一个channel组，管理所有channel
    //GlobalEventExecutor.INSTANCE是个单例的全局事件执行器
//    private static final ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    private static final Map<Long,Channel> channelMaps = new HashMap<Long,Channel>();
    //第一个被调用的方法，一旦连接第一个被执行
    //将当前这个channel加入到channelGroup
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        //某用户上线，将其记录到map中，当有人个体他发消息的时候使用 这里的id实际应该是用户id
        Long id = new Random().nextLong();
        System.out.println(id);
        channelMaps.put(id,channel);

        //此时应该查询持久化后的数据库是否有未读消息。
        LambdaQueryWrapper<Message> wrapper = new LambdaQueryWrapper<>(Message.class);
        wrapper.eq(Message::getTargetId,id).eq(Message::getState,0);
        if(messageService != null)
        {
            List<Message> unReadMessage = messageService.list(wrapper);
            if(unReadMessage != null){
                Channel channelTarget = channelMaps.get(id);
                unReadMessage.forEach(channelTarget::writeAndFlush);
            }
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx.channel().remoteAddress() + "上线了");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx.channel().remoteAddress() + "离线了");
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        //这里的id实际应该是用户id
        Integer id = new Random().nextInt(65535);
        channelMaps.remove(id);
//        channelGroup.writeAndFlush("[客户端]"+ctx.channel().remoteAddress()+"离开聊天室");
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessagePojo.Message msg) throws Exception {
        Channel channel = ctx.channel();
        //有人发送书数据，我们先拿到msg中的目标id，判断目标是否在线，如果在线直接发送消息，不在线的话将消息持久化等到上线发送给目标
        //私聊
        if(msg.getType() == 1){
            //如果在线 发送消息
            if(channelMaps.containsKey(msg.getTargetId())){
                Channel channelTarget = channelMaps.get(msg.getTargetId());
                channelTarget.writeAndFlush(msg);
            }else{
                //不在线，持久化消息
                Message message = new Message();
                BeanUtils.copyProperties(msg,message);
                messageService.save(message);
            }
        }else if(msg.getType() == 0){
            //群聊 查询群里的成员，遍历每一个成员都发送消息
            long groupId = msg.getTargetId();
            LambdaQueryWrapper<GroupUser> wrapper = new LambdaQueryWrapper<>(GroupUser.class);
            wrapper.eq(GroupUser::getGroupId,groupId);
            List<GroupUser> tempList = groupUserService.list(wrapper);
            tempList.forEach(temp ->{
                User user = userService.getById(temp.getUserId());
                if(channelMaps.containsKey(user.getId())){
                    Channel channelTarget = channelMaps.get(user.getId());
                    channelTarget.writeAndFlush(msg);
                }
            });
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
