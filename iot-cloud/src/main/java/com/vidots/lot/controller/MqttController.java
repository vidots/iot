package com.vidots.lot.controller;

import com.alibaba.fastjson.JSON;
import com.vidots.lot.model.Device;
import com.vidots.lot.model.DeviceData;
import com.vidots.lot.repo.DataMapper;
import com.vidots.lot.service.DataService;
import com.vidots.lot.service.OrderService;
import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/mqtt")
public class MqttController {

    @Autowired
    private DataMapper dataMapper;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private IMqttClient mqttClient;

    @Autowired
    private OrderService orderService;

    @Autowired
    private DataService dataService;

    // 让iot平台订阅消息
    @GetMapping("/subscribe")
    public Map<String, String> subTopic(@RequestParam String topic) throws MqttException {
        System.out.println("subTopic -- " + topic);
        mqttClient.subscribe(topic);
        redisTemplate.opsForSet().add("topics::all", topic);
        Map<String, String> result = new HashMap<>();
        result.put("result", "ok");
        return result;
    }

    // 让iot平台不订阅消息
    @GetMapping("/unsubscribe")
    public Map<String, String> unsubTopic(@RequestParam String topic) throws MqttException {
        System.out.println("unsubTopic -- " + topic);
        mqttClient.unsubscribe(topic);
        redisTemplate.opsForSet().remove("topics::all", topic);
        Map<String, String> result = new HashMap<>();
        result.put("result", "ok");
        return result;
    }

    @GetMapping("/data")
    public Map<String, Object> getDataFromTopic(@RequestParam String deviceKey) throws MqttException {
        String topic = "/topic/" + deviceKey + "/client_send";
        System.out.println("getDataFromTopic -- " + topic);
        List<String> data = redisTemplate.opsForList().range(topic, 0, -1);
        List<Map> mapList = data.stream().map(item -> JSON.parseObject(item, Map.class)).collect(Collectors.toList());
        Map<String, Object> result = new HashMap<>();
        result.put("result", "ok");
        result.put("data", mapList);
        return result;
    }

    @GetMapping("/datamap")
    public Map<String, Object> getDatamapFromDevice(@RequestParam String deviceKey) {
        Device device= dataService.getDeviceByKey(deviceKey);
        String properties = device.getProperties();
        String[] parts = properties.split(",");
        Map<String, Map<String, String>> map = new HashMap<>();
        for(String part:parts) {
            String[] items = part.split("-");
            Map<String, String> detail = new HashMap<>();
            detail.put("name", items[0]);
            detail.put("datatype", items[2]);
            map.put(items[1], detail);
        }

        Map<String, Object> result = new HashMap<>();
        result.put("result", "ok");
        result.put("mapping", map);
        return result;
    }

    @GetMapping("/data/history")
    public Map<String, Object> getHisotryDataFromTopic(@RequestParam String deviceKey) throws MqttException {
        String topic = "/topic/" + deviceKey + "/client_send";
        System.out.println("getHisotryDataFromTopic -- " + topic);
        DeviceData deviceData = new DeviceData();
        deviceData.setTopic(topic);
        List<DeviceData> data = dataMapper.select(deviceData);
        List<Map> mapList = data.stream().map(item -> JSON.parseObject(item.getContent(), Map.class)).collect(Collectors.toList());
        Map<String, Object> result = new HashMap<>();
        result.put("result", "ok");
        result.put("data", mapList);
        return result;
    }

    @GetMapping("/order")
    public Map<String, String> order(@RequestParam String topic) throws MqttException {
        System.out.println("order -- " + topic);
        Map<String, String> payload = new HashMap<>();
        payload.put("temperature", "20");
        orderService.sendOrder(topic, payload);
        Map<String, String> result = new HashMap<>();
        result.put("result", "ok");
        return result;
    }

    @GetMapping("/connected-devices")
    public Map<String, Object> getConnectedDevices() throws MqttException {
        Set<String> data = redisTemplate.opsForSet().members("main:connected");
        Map<String, Object> result = new HashMap<>();
        result.put("result", "ok");
        result.put("connected-devices", data);
        return result;
    }

    @GetMapping("/all-devices")
    public Map<String, Object> getAllDevices() {
        List<Device> allDevices = dataService.getAllDevices();
        Map<String, Object> result = new HashMap<>();
        result.put("result", "ok");
        result.put("all-devices", allDevices);
        return result;
    }

    @GetMapping("/stat")
    public Map<String, Object> getStat() {
        Map<String, Object> stat = new HashMap<>();
        Long size = redisTemplate.opsForSet().size("main:connected");
        stat.put("runCount", size == null ? 0 : size);
        stat.put("allCount", dataService.getDeviceCount());
        stat.put("msgCount", dataService.getDataCount());
        return stat;
    }

    @PostMapping("/adddevice")
    public Map<String, Object> addDevice(@RequestBody Device device) throws MqttException {
        Device d = dataService.addDevice(device);
        String topic = "/topic/" + d.getDeviceKey() + "/client_send";
        mqttClient.subscribe(topic);
        Map<String, Object> result = new HashMap<>();
        result.put("device", d);
        return result;
    }
}
