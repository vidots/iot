var mqtt = require('mqtt')
var device_key = 'device_2'
var device_secret = '123'
var topic_client_send = `/topic/${device_key}/client_send`
var topic_lot_send = `/topic/${device_key}/lot_send`
var interval = 1000

var  base_temperature = 10

function collectData() {
    var data = {
        temperature: Math.random() * 10 + base_temperature,
        humidity: Math.random() * 5,
        innertemp: Math.random() * 10,
        date: Date.now().toString()
    }
    return data
}

var client = mqtt.connect("mqtt://localhost:18080", {
    username: device_key,
    password: device_secret,
    reconnectPeriod: 3000,
    clientId: device_key
})

client.on('connect', packet => {
    console.log('设备成功连接到iot平台')
    // 向平台发送数据
    setInterval(()=> {
        client.publish(topic_client_send, JSON.stringify(collectData()))
    }, interval)
    // 订阅某个topic
    client.subscribe(topic_lot_send, err => {
        if(err) {
            console.log('subscribe error..')
        }
    })
})


client.on('error', err => {
    console.log('connect failure')
})

// 接收平台的指令
client.on('message', function(topic, message) {
    var obj = JSON.parse(message.toString())
    base_temperature = parseInt(obj.temperature)
    console.log('device_2 receive ' + topic + " - "  + message.toString())
})