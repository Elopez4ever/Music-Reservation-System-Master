<template>
  <div class="feedback-page-container">
    <div class="page-header">
      <h2>用户反馈</h2>
      <p>您的反馈对我们很重要，我们将不断改进系统以提供更好的用户体验</p>
    </div>
    
    <el-card class="feedback-card">
      <div slot="header" class="clearfix">
        <span>提交反馈</span>
      </div>
      <feedback-form @submit-success="handleSubmitSuccess"></feedback-form>
    </el-card>
    
    <el-card class="tips-card">
      <div slot="header" class="clearfix">
        <span>反馈提示</span>
      </div>
      <div class="tips-content">
        <h4>提交反馈时请注意：</h4>
        <ul>
          <li>请清晰描述您遇到的问题或建议</li>
          <li>如果是功能异常，请说明操作步骤以便我们复现问题</li>
          <li>您可以提供截图或详细说明以帮助我们更好地理解</li>
          <li>请留下您的联系方式，以便我们需要时可以联系您</li>
          <li>我们会尽快处理您的反馈，感谢您的支持！</li>
        </ul>
      </div>
    </el-card>
    
    <!-- 页脚 -->
    <FooterBar />
  </div>
</template>

<script>
import FeedbackForm from '../components/FeedbackForm';
import TopNavbar from '@/views/front/components/TopNavbar';
import FooterBar from '@/views/front/components/FooterBar.vue';

export default {
  name: 'FeedbackPage',
  components: {
    FeedbackForm,
    TopNavbar,
    FooterBar
  },
  methods: {
    // 处理提交成功
    handleSubmitSuccess() {
      // 可以在这里添加额外的处理逻辑
    }
  }
};
</script>

<style scoped>
.feedback-page-container {
  max-width: 1000px;
  margin: 0 auto;
  padding: 20px;
}

.page-header {
  text-align: center;
  margin-bottom: 30px;
}

.page-header h2 {
  font-size: 24px;
  color: #303133;
}

.page-header p {
  color: #606266;
  margin-top: 10px;
}

.feedback-card {
  margin-bottom: 30px;
}

.tips-card {
  margin-bottom: 30px;
}

.tips-content {
  padding: 10px;
}

.tips-content h4 {
  margin-bottom: 10px;
  color: #303133;
}

.tips-content ul {
  padding-left: 20px;
}

.tips-content li {
  line-height: 1.8;
  color: #606266;
}
</style> 