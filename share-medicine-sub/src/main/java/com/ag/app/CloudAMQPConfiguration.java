package com.ag.app;


import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CloudAMQPConfiguration {
    @Value("${cloudamqp.host}")
    private String host;
    @Value("${cloudamqp.port}")
    private int port;
    @Value("${cloudamqp.username}")
    private String username = "aannimrm";
    @Value("${cloudamqp.password}")
    private String password;

    @Value("${cloudamqp.virtualhost}")
    private String virtualHost = "aannimrm";


    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setHost(host);
        connectionFactory.setPort(port);
        connectionFactory.setUsername(username);
        connectionFactory.setPassword(password);
        connectionFactory.setVirtualHost(virtualHost);
        return connectionFactory;
    }
}
