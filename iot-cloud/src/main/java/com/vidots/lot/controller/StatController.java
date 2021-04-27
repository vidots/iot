package com.vidots.lot.controller;

import com.vidots.lot.service.DataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/stat")
public class StatController {
    @Autowired
    private DataService dataService;

    @GetMapping("/devices-count")
    public Map<String, Object> getDevicesCount() {
        Map<String, Object> result = new HashMap<>();
        result.put("count", dataService.getDeviceCount());
        return result;
    }
}
