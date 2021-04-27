<template>
  <el-table :data="tableData" border style="width: 100%; margin: 24px">
    <el-table-column
      v-for="(item, i) in headers"
      align="center"
      :prop="item.field"
      :label="item.name"
      :key="i"
    >
    </el-table-column>
  </el-table>
</template>

<script>
import { getDeviceData, getDevice } from "../../api/stat";

export default {
  name: "connect",
  data() {
    return {
      deviceKey: '',
      tableData: [],
      headers: []
    };
  },
  created() {
  },
  mounted() {
    this.deviceKey = this.$route.params.deviceKey;
    console.log(this.deviceKey)
    this.fetchData()
    let that = this
    setInterval(() => {
      that.fetchData()
    }, 2000);
  },
  methods: {
    fetchData() {
      let that = this
      getDevice(that.deviceKey).then(data => {
        that.mapping = data['mapping']
        let headers = []
        headers.push(
          {
            'field': 'date',
            'name': '日期'
          }
        )
        console.log(that.mapping)
        for(let key in that.mapping) {
          console.log(key)
          console.log(that.mapping[key])
          headers.push({
            'field': key,
            'name': that.mapping[key]['name']
          })
        }
        getDeviceData(that.deviceKey).then(res => {
          console.log(res)
          let arr = res['data']
          let items = arr.map(item => {
            var obj = {}
            for(let key in item) {
              if(key == 'date') {
                let mdate = new Date(parseInt(item[key]))
                obj[key] = mdate.toLocaleString(

                )
              } else {
                let dt = that.mapping[key]['datatype']
                if(dt == 'float') {
                  obj[key] = item[key].toFixed(2)
                } else {
                  obj[key] = item[key]
                }
              }
            }
            return obj
          })
          that.headers = headers
          that.tableData = items.reverse()
      })
      })
      
    }
  }
}
</script>
