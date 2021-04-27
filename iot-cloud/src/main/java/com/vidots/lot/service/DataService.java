package com.vidots.lot.service;

import com.vidots.lot.model.Device;
import com.vidots.lot.repo.DataMapper;
import com.vidots.lot.repo.DeviceMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class DataService {
    @Autowired
    private DeviceMapper deviceMapper;
    @Autowired
    private DataMapper dataMapper;

    public List<Device> getAllDevices() {
        return deviceMapper.selectAll();
    }

    public int getDeviceCount() {
        return deviceMapper.selectCount(null);
    }

    public int getDataCount() {
        return dataMapper.selectCount(null);
    }

    public Device getDeviceByKey(String key) {
        Device device = new Device();
        device.setDeviceKey(key);
        return deviceMapper.selectOne(device);
    }

    public Device addDevice(Device device) {
        device.setDeviceKey(UUID.randomUUID().toString());
        device.setDeviceSecret(UUID.randomUUID().toString());
        deviceMapper.insert(device);
        return device;
    }
}
