package com.vidots.lot.service;

import com.alibaba.fastjson.JSON;
import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class OrderService {
    @Autowired
    private IMqttClient iMqttClient;

    public void sendOrder(String topic, Map<String, String> payload) throws MqttException {
        String json = JSON.toJSONString(payload);
        MqttMessage msg = new MqttMessage(json.getBytes());
        iMqttClient.publish(topic, msg);
    }
}





















