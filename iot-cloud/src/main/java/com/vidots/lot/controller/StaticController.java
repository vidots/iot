package com.vidots.lot.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/page")
public class StaticController {

    @RequestMapping("/devices/status")
    public String getDeviceStatus() {
        return "/html/index.html";
    }

    @RequestMapping("/devices/realtime_data")
    public String getRealtimeData(@RequestParam String deviceKey) {
        return "/html/realtime_data.html";
    }
}
