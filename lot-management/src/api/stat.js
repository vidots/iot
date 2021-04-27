import request from '@/utils/request'

export function getDeviceCount() {
    return request({
      url: '/stat/devices-count',
      method: 'get'
    })
}

export function getAllDevices() {
  return request({
    url: '/mqtt/all-devices',
    method: 'get'
  })
}

export function getConnectedDevices() {
  return request({
    url: '/mqtt/connected-devices',
    method: 'get'
  })
}

export function getDeviceData(deviceKey) {
  return request({
    url: '/mqtt/data?deviceKey=' + deviceKey,
    method: 'get'
  })
}

export function getDevice(deviceKey) {
  return request({
    url: '/mqtt/datamap?deviceKey=' + deviceKey,
    method: 'get'
  })
}

export function getStat() {
  return request({
    url: '/mqtt/stat',
    method: 'get'
  })
}

export function addDevice(data) {
  return request({
    url: '/mqtt/adddevice',
    method: 'post',
    data
  })
}