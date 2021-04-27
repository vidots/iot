<template>
  <el-table :data="devices" border style="width: 100%; margin: 24px">
    <el-table-column align="center" prop="id" label="编号" width="180">
    </el-table-column>
    <el-table-column align="center" prop="name" label="设备名称">
    </el-table-column>
    <el-table-column align="center" prop="status" label="运行状态">
    </el-table-column>
    <el-table-column label="操作" align="center">
          <template slot-scope="scope">
            <el-button v-if="scope.row.status=='运行中'" type="text" size="small" @click="viewData(scope.row.deviceKey)">查看设备实时上报数据</el-button>
          </template>
    </el-table-column>
  </el-table>
</template>

<script>
import { getAllDevices, getConnectedDevices } from "../../api/stat";

export default {
  name: "connect",
  data() {
    return {
      devices: []
    };
  },
  created() {
    this.fetchData()
    let that = this
    setInterval(() => {
      that.fetchData()
    }, 2000);
  },
  methods: {
    viewData(deviceKey) {
      console.log(deviceKey)
      this.$router.push({name: 'data', params: {deviceKey: deviceKey}})
    },
    fetchData() {
      let that = this
      getAllDevices().then(res => {
        getConnectedDevices().then(data => {
          let devices = res['all-devices']
          let connected = data['connected-devices']
          let ds = devices.filter(d => {
            return d['deviceKey'] != 'admin'
          }).map(d => {
            if(connected.includes(d['deviceKey'])) {
              d['status'] = '运行中'
            } else {
              d['status'] = '暂未启动'
            }
            return d
          })
          that.devices = ds
        }) 
      })
        
    }
  }
}
</script>
