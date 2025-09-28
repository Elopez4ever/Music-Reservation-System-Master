<template>
  <el-dialog
    :title="title"
    :visible.sync="dialogVisible"
    width="500px"
    @closed="handleClosed">
    <el-form 
      ref="form" 
      :model="buildingForm" 
      :rules="rules" 
      label-width="100px">
      <el-form-item label="建筑名称" prop="name">
        <el-input v-model="buildingForm.name" placeholder="请输入建筑名称"></el-input>
      </el-form-item>
      <el-form-item label="建筑描述" prop="description">
        <el-input 
          v-model="buildingForm.description" 
          type="textarea" 
          :rows="4"
          placeholder="请输入建筑描述信息">
        </el-input>
      </el-form-item>
    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button @click="dialogVisible = false">取 消</el-button>
      <el-button type="primary" @click="submitForm" :loading="loading">确 定</el-button>
    </div>
  </el-dialog>
</template>

<script>
import { buildingApi } from '@/api/building'

export default {
  name: 'BuildingForm',
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    title: {
      type: String,
      default: '添加建筑'
    },
    building: {
      type: Object,
      default: () => ({})
    }
  },
  data() {
    return {
      // 表单显示状态
      dialogVisible: false,
      // 提交按钮加载状态
      loading: false,
      // 表单数据
      buildingForm: {
        id: undefined,
        name: '',
        description: ''
      },
      // 表单验证规则
      rules: {
        name: [
          { required: true, message: '请输入建筑名称', trigger: 'blur' },
          { min: 2, max: 50, message: '长度在 2 到 50 个字符', trigger: 'blur' }
        ],
        description: [
          { max: 500, message: '长度不能超过 500 个字符', trigger: 'blur' }
        ]
      }
    }
  },
  watch: {
    visible(val) {
      this.dialogVisible = val
    },
    dialogVisible(val) {
      this.$emit('update:visible', val)
    },
    building: {
      handler(val) {
        // 深拷贝避免修改原对象
        this.buildingForm = JSON.parse(JSON.stringify(val))
      },
      immediate: true,
      deep: true
    }
  },
  methods: {
    // 提交表单
    submitForm() {
      this.$refs.form.validate(valid => {
        if (!valid) {
          return
        }
        
        this.loading = true
        
        // 根据 ID 判断是添加还是更新
        const request = this.buildingForm.id
          ? buildingApi.updateBuilding(this.buildingForm)
          : buildingApi.addBuilding(this.buildingForm)
        
        request
          .then(response => {
            this.loading = false
            if (response.code === 1) {
              this.$message.success(this.buildingForm.id ? '更新成功' : '添加成功')
              this.dialogVisible = false
              this.$emit('success')
            } else {
              this.$message.error(response.msg || '操作失败')
            }
          })
          .catch(() => {
            this.loading = false
            this.$message.error('操作失败，请重试')
          })
      })
    },
    
    // 对话框关闭事件
    handleClosed() {
      this.$refs.form.resetFields()
      this.buildingForm = {
        id: undefined,
        name: '',
        description: ''
      }
    }
  }
}
</script>

<style scoped>
/* 表单样式 */
</style> 