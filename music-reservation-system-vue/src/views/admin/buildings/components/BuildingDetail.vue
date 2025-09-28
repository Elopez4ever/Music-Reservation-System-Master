<template>
  <el-dialog
    title="建筑详情"
    :visible.sync="dialogVisible"
    width="500px">
    <el-descriptions :column="1" border>
      <el-descriptions-item label="建筑名称">{{ building.name }}</el-descriptions-item>
      <el-descriptions-item label="建筑描述">{{ building.description }}</el-descriptions-item>
      <el-descriptions-item label="创建时间">{{ formatDateTime(building.createdAt) }}</el-descriptions-item>
      <el-descriptions-item label="更新时间">{{ formatDateTime(building.updatedAt) }}</el-descriptions-item>
    </el-descriptions>
    <div slot="footer" class="dialog-footer">
      <el-button @click="dialogVisible = false">关 闭</el-button>
    </div>
  </el-dialog>
</template>

<script>
import { formatDate } from '@/utils/date'

export default {
  name: 'BuildingDetail',
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    building: {
      type: Object,
      default: () => ({})
    }
  },
  data() {
    return {
      dialogVisible: false
    }
  },
  watch: {
    visible(val) {
      this.dialogVisible = val
    },
    dialogVisible(val) {
      this.$emit('update:visible', val)
    }
  },
  methods: {
    // 格式化日期时间
    formatDateTime(dateTime) {
      if (!dateTime) {
        return '--'
      }
      return formatDate(new Date(dateTime), 'yyyy-MM-dd HH:mm:ss')
    }
  }
}
</script>

<style scoped>
/* 详情样式 */
</style> 