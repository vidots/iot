<template>
  <el-table :data="devices" border style="width: 100%; margin: 24px">
    <el-table-column align="center" prop="id" label="编号" width="180">
    </el-table-column>
    <el-table-column align="center" prop="name" label="设备名称" width="240">
    </el-table-column>
    <el-table-column
      align="center"
      prop="deviceKey"
      label="设备标志"
      width="180"
    >
    </el-table-column>
    <el-table-column
      align="center"
      prop="deviceSecret"
      label="设备密钥"
      width="180"
    >
    </el-table-column>
    <el-table-column align="center" prop="properties" label="设备可上传指标">
      <template slot-scope="scope">
        <el-table :data="scope.row.properties" border="" style="width: 60%;margin-left:20%">
          <el-table-column align="center" prop="id" label="指标标志">
          </el-table-column>
          <el-table-column align="center" prop="name" label="指标含义">
          </el-table-column>
          <el-table-column align="center" prop="cate" label="指标类型"> </el-table-column>
        </el-table>
      </template>
    </el-table-column>
  </el-table>
</template>

<script>
import { getAllDevices } from "../../api/stat";

export default {
  name: "device",
  data() {
    return {
      devices: [],
    };
  },
  created() {
    this.fetchData();
  },
  methods: {
    fetchData() {
      getAllDevices().then((res) => {
        console.log(res);
        this.devices = res["all-devices"]
          .filter((device) => {
            return device.deviceKey != "admin";
          })
          .map((device) => {
            console.log(device);
            let properties = device["properties"];
            let parts = properties.split(",");
            let metrics = parts.map((part) => {
              let props = part.split("-");
              let obj = {
                id: props[1],
                name: props[0],
                cate: props[2],
              };
              return obj;
            });
            device["properties"] = metrics;
            return device;
          });
      });
    },
  },
};
</script>

<style lang="scss" scoped>
</style>
