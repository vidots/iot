package com.vidots.lot.mqtt;

import com.vidots.lot.model.Device;
import com.vidots.lot.model.DeviceData;
import com.vidots.lot.repo.DataMapper;
import com.vidots.lot.repo.DeviceMapper;
import org.eclipse.paho.client.mqttv3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Configuration
public class MqttConfig {

    @Autowired
    private DeviceMapper deviceMapper;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private DataMapper dataMapper;

    @Bean
    public IMqttClient mqttClient() throws MqttException {
        String publisherId = "iot-homework";
        IMqttClient publisher = new MqttClient("tcp://localhost:18080",publisherId);
        MqttConnectOptions options = new MqttConnectOptions();
        options.setAutomaticReconnect(true);
        options.setCleanSession(true);
        options.setUserName("admin");
        options.setPassword("admin".toCharArray());
        options.setConnectionTimeout(100);
        publisher.connect(options);
        // 删除所有的主题
        redisTemplate.delete("topics::all");
        redisTemplate.delete("main:connected");
        publisher.setCallback(new MqttCallback() {
            @Override
            public void connectionLost(Throwable cause) {

            }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                System.out.println("------------");
                System.out.println(topic + " " + new String(message.getPayload()));
                System.out.println("------------");
                DeviceData deviceData = new DeviceData();
                deviceData.setTopic(topic);
                deviceData.setTopic(new String(message.getPayload()));
                dataMapper.insert(deviceData);
                Long size = redisTemplate.opsForList().size(topic);
                if (size != null && size > 10) {
                    for (int i=10; i< size; i++) {
                        redisTemplate.opsForList().leftPop(topic);
                    }
                }
                redisTemplate.opsForList().rightPush(topic, new String(message.getPayload()));
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {

            }
        });
        List<Device> devices = deviceMapper.selectAll();
        for (Device device: devices) {
            if (device.getDeviceKey() != "admin") {
                publisher.subscribe("/topic/" + device.getDeviceKey() + "/client_send");
            }
        }
        return publisher;
    }
}
















