// 引入MySQL数据库，对连接的客户端进行验证
var mysql = require('mysql');
var connection = mysql.createConnection({
  host     : 'localhost',
  user     : 'root',
  password : 'weichuang',
  database : 'lot'
});
connection.connect(err => {
    if(err) {
        console.log('数据库连接失败')
        console.log(err)
        process.exit(-1)
    }
});

var redis = require('redis')
var redisClient = redis.createClient(6379, '127.0.0.1')
redisClient.on('connect', function() {
    console.log('redis connect')
})


const aedes = require('aedes')({
    authenticate: function(client, username, password, callback) {
        var sql = `select * from device where device_key="${username}" and device_secret="${password}"`
        connection.query(sql, function(err, result) {
            // 对连接的设备进行验证
            if(err) {
                console.log(err)
                var error = new Error('验证错误')
                error.returnCode = 4
                callback(error, false)
            } else {
                callback(null, true)
            }
        })
    }
})

aedes.on('clientReady', function(client) {
    console.log("---connect------ " + client.id)
    if(client.id != 'iot-homework') {
        redisClient.sadd('main:connected', [client.id])
    }
    
})

aedes.on('clientDisconnect', function(client) {
    console.log("---disconnect------ " + client.id)
    redisClient.srem('main:connected', [client.id])
})

 

const server = require('net').createServer(aedes.handle)
const port = 18080

server.listen(port, function () {
    console.log('支持MQTT协议的服务器启动成功', port)
})