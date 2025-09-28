<template>
  <div class="feedback-detail">
    <el-descriptions :column="1" border>
      <el-descriptions-item label="反馈ID">
        {{ feedback.id }}
      </el-descriptions-item>
      <el-descriptions-item label="反馈类型">
        <el-tag v-if="feedback.feedbackType === 'BUG'" type="danger">功能异常</el-tag>
        <el-tag v-else-if="feedback.feedbackType === 'SUGGESTION'" type="warning">功能建议</el-tag>
        <el-tag v-else type="info">其他</el-tag>
      </el-descriptions-item>
      <el-descriptions-item label="反馈内容">
        <div class="content-area">
          {{ feedback.content }}
        </div>
      </el-descriptions-item>
      <el-descriptions-item label="联系方式">
        {{ feedback.contactInfo || '未提供' }}
      </el-descriptions-item>
      <el-descriptions-item label="创建时间">
        {{ formatDateTime(feedback.createdAt) }}
      </el-descriptions-item>
      <el-descriptions-item label="更新时间">
        {{ formatDateTime(feedback.updatedAt) }}
      </el-descriptions-item>
      <el-descriptions-item label="状态">
        <el-tag v-if="feedback.status === 0" type="info">未处理</el-tag>
        <el-tag v-else-if="feedback.status === 1" type="success">已处理</el-tag>
      </el-descriptions-item>
    </el-descriptions>

    <div class="action-buttons">
      <el-button @click="handleClose">关闭</el-button>
      <el-button 
        v-if="feedback.status === 0" 
        type="primary" 
        @click="handleMarkAsProcessed"
      >
        标记为已处理
      </el-button>
      <el-button 
        v-else 
        type="warning" 
        @click="handleMarkAsUnprocessed"
      >
        标记为未处理
      </el-button>
    </div>
  </div>
</template>

<script>
import { feedbackApi } from '@/api/feedback';

export default {
  name: 'FeedbackDetail',
  props: {
    // 反馈详情对象
    feedback: {
      type: Object,
      required: true,
      default: () => ({})
    }
  },
  methods: {
    // 格式化日期时间
    formatDateTime(dateTime) {
      if (!dateTime) return '无';
      const date = new Date(dateTime);
      return `${date.getFullYear()}-${String(date.getMonth() + 1).padStart(2, '0')}-${String(
        date.getDate()
      ).padStart(2, '0')} ${String(date.getHours()).padStart(2, '0')}:${String(
        date.getMinutes()
      ).padStart(2, '0')}:${String(date.getSeconds()).padStart(2, '0')}`;
    },
    // 关闭对话框
    handleClose() {
      this.$emit('close');
    },
    // 标记为已处理
    handleMarkAsProcessed() {
      this.$confirm('确定要将该反馈标记为已处理吗?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
        .then(() => {
          return feedbackApi.setStatus(this.feedback.id, 1);
        })
        .then(() => {
          this.$message.success('已成功标记为已处理');
          this.$emit('refresh');
          this.$emit('close');
        })
        .catch(() => {
          this.$message.info('已取消操作');
        });
    },
    // 标记为未处理
    handleMarkAsUnprocessed() {
      this.$confirm('确定要将该反馈标记为未处理吗?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
        .then(() => {
          return feedbackApi.setStatus(this.feedback.id, 0);
        })
        .then(() => {
          this.$message.success('已成功标记为未处理');
          this.$emit('refresh');
          this.$emit('close');
        })
        .catch(() => {
          this.$message.info('已取消操作');
        });
    }
  }
};
</script>

<style scoped>
.feedback-detail {
  padding: 10px;
}
.content-area {
  white-space: pre-wrap;
  word-break: break-all;
  min-height: 100px;
  padding: 10px;
  background-color: #f9f9f9;
  border-radius: 4px;
}
.action-buttons {
  margin-top: 20px;
  text-align: right;
}
</style> 