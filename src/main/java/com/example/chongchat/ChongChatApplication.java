package com.example.chongchat;

import com.example.chongchat.chat.GroupChatServer;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@EnableWebMvc
@MapperScan(value = "com.example.chongchat.mapper")
public class ChongChatApplication {

    public static void main(String[] args) {
        SpringApplication.run(ChongChatApplication.class, args);
        new Thread(() -> {
            try {
                new GroupChatServer(8282).run();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }
}
