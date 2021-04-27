<template>
  <div style="margin: 20px">
    <el-form label-width="80px">
      <el-form-item label="产品名称">
        <el-input v-model="productName"></el-input>
      </el-form-item>
      <el-form-item label="连接协议">
        <el-input value="MQTT" readonly></el-input>
      </el-form-item>
    </el-form>

    <el-table :data="properties" border="" style="width: 100%">
      <el-table-column align="center" prop="field" label="字段">
      </el-table-column>
      <el-table-column align="center" prop="name" label="字段含义">
      </el-table-column>
      <el-table-column align="center" prop="datatype" label="数据类型">
      </el-table-column>
    </el-table>

    <el-row style="margin-top: 20px; text-align: center">
      <el-button @click="addField = true" type="primary">添加字段</el-button>
      <el-button @click="uploadDevice" type="primary">保存设备信息</el-button>
    </el-row>

    <el-dialog title="添加字段" :visible.sync="addField" width="30%">
      <el-form :model="adding" label-width="80px">
        <el-form-item label="字段">
          <el-input v-model="adding.field"></el-input>
        </el-form-item>
        <el-form-item label="字段含义">
          <el-input v-model="adding.name"></el-input>
        </el-form-item>
        <el-form-item label="字段类型">
          <el-select v-model="adding.datatype" placeholder="请选择字段类型">
            <el-option label="浮点型" value="float"></el-option>
            <el-option label="整形" value="int"></el-option>
            <el-option label="字符串" value="str"></el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="addField = false">取 消</el-button>
        <el-button type="primary" @click="saveDevice">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import { addDevice } from "../../api/stat";

export default {
  name: "adddevice",
  data() {
    return {
      addField: false,
      productName: "",
      properties: [],
      adding: {
        field: "",
        name: "",
        datatype: "",
      },
    };
  },
  created() {
  },
  methods: {
      uploadDevice() {
          if(this.productName == '') {
              alert('添加的设备信息不能为空')
          } else {
              let props = this.properties
              let str = props.map(prop => {
                  return `${prop.name}-${prop.field}-${prop.datatype}`
              }).join(',')
              let data = {
                  name: this.productName,
                  properties: str
              }
              let that = this
              addDevice(data).then(res => {
                  that.$router.push({name: 'device'})
              })
          }
      },
    saveDevice() {
      if (
        this.adding["field"] == "" ||
        this.adding["name"] == "" ||
        this.adding["datatype"] == ""
      ) {
        alert("填写的信息不为空");
      } else {
        let props = this.properties;
        props.push({
          field: this.adding["field"],
          name: this.adding["name"],
          datatype: this.adding["datatype"],
        });
        this.properties = props;
        this.adding = {
          field: "",
          name: "",
          datatype: "",
        };
        this.addField = false
      }
    },
  },
};
</script>

<style lang="scss" scoped>
</style>
