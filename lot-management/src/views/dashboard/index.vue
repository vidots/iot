<template>
  <div style="margin: 20px;">
    <el-row :gutter="20">
      <el-col :span="8">
        <el-card>
          <div slot="header">
            <span>运行设备数量</span>
          </div>
          <div style="font-size:36px;">{{runCount}}</div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card>
          <div slot="header">
            <span>全部设备数量</span>
          </div>
          <div style="font-size:36px;">{{allCount}}</div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card>
          <div slot="header">
            <span>接收的消息总数</span>
          </div>
          <div style="font-size:36px;">{{msgCount}}</div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { getStat } from "../../api/stat";

export default {
  name: "Dashboard",
  data() {
    return {
      allCount: 0,
      runCount: 0,
      msgCount: 0
    };
  },
  created() {
    this.fetchData()
    setInterval(this.fetchData, 2000)
  },
  methods: {
    fetchData() {
      let that = this
      getStat().then((res) => {
        that.allCount = res.allCount - 1
        that.runCount = res.runCount
        that.msgCount = res.msgCount
      });
    },
  },
};
</script>
