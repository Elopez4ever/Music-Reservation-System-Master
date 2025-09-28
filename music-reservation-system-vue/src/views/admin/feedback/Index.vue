<template>
  <div class="feedback-container">
    <el-card class="box-card">
      <div slot="header" class="clearfix">
        <span>留言反馈管理</span>
        <el-button
          style="float: right; margin-left: 10px"
          type="danger"
          size="small"
          :disabled="selectedItems.length === 0"
          @click="handleBatchDelete"
        >
          批量删除
        </el-button>
      </div>

      <!-- 查询条件 -->
      <el-form :inline="true" :model="queryParams" class="demo-form-inline">
        <el-form-item label="反馈类型">
          <el-select v-model="queryParams.feedbackType" placeholder="选择反馈类型" clearable>
            <el-option label="功能异常(BUG)" value="BUG"></el-option>
            <el-option label="功能建议" value="SUGGESTION"></el-option>
            <el-option label="其他" value="OTHER"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="queryParams.status" placeholder="选择状态" clearable>
            <el-option label="未处理" :value="0"></el-option>
            <el-option label="已处理" :value="1"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleQuery">查询</el-button>
          <el-button @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>

      <!-- 数据表格 -->
      <el-table
        v-loading="loading"
        :data="feedbackList"
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="55"></el-table-column>
        <el-table-column prop="id" label="ID" width="80"></el-table-column>
        <el-table-column prop="feedbackType" label="反馈类型" width="120">
          <template slot-scope="scope">
            <el-tag v-if="scope.row.feedbackType === 'BUG'" type="danger">功能异常</el-tag>
            <el-tag v-else-if="scope.row.feedbackType === 'SUGGESTION'" type="warning">功能建议</el-tag>
            <el-tag v-else type="info">其他</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="content" label="反馈内容" show-overflow-tooltip></el-table-column>
        <el-table-column prop="contactInfo" label="联系方式" width="150"></el-table-column>
        <el-table-column prop="createdAt" label="创建时间" width="180">
          <template slot-scope="scope">
            {{ formatDateTime(scope.row.createdAt) }}
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template slot-scope="scope">
            <el-tag v-if="scope.row.status === 0" type="info">未处理</el-tag>
            <el-tag v-else-if="scope.row.status === 1" type="success">已处理</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="150">
          <template slot-scope="scope">
            <el-button
              v-if="scope.row.status === 0"
              type="text"
              size="small"
              @click="handleSetStatus(scope.row.id, 1)"
            >
              标记已处理
            </el-button>
            <el-button
              v-else
              type="text"
              size="small"
              @click="handleSetStatus(scope.row.id, 0)"
            >
              标记未处理
            </el-button>
            <el-button
              type="text"
              size="small"
              style="color: #f56c6c"
              @click="handleDelete(scope.row.id)"
            >
              删除
            </el-button>
            <el-button
              type="text"
              size="small"
              @click="handleViewDetail(scope.row)"
            >
              查看详情
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="queryParams.page"
        :page-sizes="[10, 20, 50, 100]"
        :page-size="queryParams.pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total"
        class="pagination"
      ></el-pagination>
    </el-card>

    <!-- 查看详情对话框 -->
    <el-dialog title="反馈详情" :visible.sync="detailDialogVisible" width="600px">
      <feedback-detail :feedback="currentFeedback" @close="detailDialogVisible = false"
       @refresh="fetchData"
       />
    </el-dialog>
  </div>
</template>

<script>
import { feedbackApi } from '@/api/feedback';
import FeedbackDetail from './components/FeedbackDetail';

export default {
  name: 'Feedback',
  components: {
    FeedbackDetail
  },
  data() {
    return {
      // 查询参数
      queryParams: {
        page: 1,
        pageSize: 10,
        feedbackType: '',
        status: ''
      },
      // 数据列表
      feedbackList: [],
      // 总记录数
      total: 0,
      // 加载状态
      loading: false,
      // 选中项
      selectedItems: [],
      // 详情对话框可见性
      detailDialogVisible: false,
      // 当前选中的反馈
      currentFeedback: {}
    };
  },
  created() {
    this.fetchData();
  },
  methods: {
    // 获取数据
    fetchData() {
      this.loading = true;
      feedbackApi
        .getAllFeedbacks(this.queryParams)
        .then(response => {
          const { data } = response;
          this.feedbackList = data.rows || [];
          this.total = data.total || 0;
        })
        .finally(() => {
          this.loading = false;
        });
    },
    // 查询按钮点击事件
    handleQuery() {
      this.queryParams.page = 1;
      this.fetchData();
    },
    // 重置查询
    resetQuery() {
      this.queryParams = {
        page: 1,
        pageSize: 10,
        feedbackType: '',
        status: ''
      };
      this.fetchData();
    },
    // 每页大小变化
    handleSizeChange(val) {
      this.queryParams.pageSize = val;
      this.fetchData();
    },
    // 页码变化
    handleCurrentChange(val) {
      this.queryParams.page = val;
      this.fetchData();
    },
    // 多选变化
    handleSelectionChange(selection) {
      this.selectedItems = selection;
    },
    // 处理删除
    handleDelete(id) {
      this.$confirm('此操作将永久删除该反馈信息, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
        .then(() => {
          return feedbackApi.deleteFeedbacks([id]);
        })
        .then(() => {
          this.$message.success('删除成功');
          this.fetchData();
        })
        .catch(() => {
          this.$message.info('已取消删除');
        });
    },
    // 批量删除
    handleBatchDelete() {
      const ids = this.selectedItems.map(item => item.id);
      if (ids.length === 0) {
        this.$message.warning('请选择要删除的项');
        return;
      }

      this.$confirm(`此操作将永久删除选中的 ${ids.length} 条反馈信息, 是否继续?`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
        .then(() => {
          return feedbackApi.deleteFeedbacks(ids);
        })
        .then(() => {
          this.$message.success('批量删除成功');
          this.fetchData();
        })
        .catch(() => {
          this.$message.info('已取消删除');
        });
    },
    // 设置状态
    handleSetStatus(id, status) {
      const statusText = status === 1 ? '已处理' : '未处理';
      this.$confirm(`确定要将该反馈标记为${statusText}吗?`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
        .then(() => {
          return feedbackApi.setStatus(id, status);
        })
        .then(() => {
          this.$message.success(`已成功标记为${statusText}`);
          this.fetchData();
        })
        .catch(() => {
          this.$message.info('已取消操作');
        });
    },
    // 查看详情
    handleViewDetail(row) {
      this.currentFeedback = { ...row };
      this.detailDialogVisible = true;
    },
    // 格式化日期时间
    formatDateTime(dateTime) {
      if (!dateTime) return '';
      const date = new Date(dateTime);
      return `${date.getFullYear()}-${String(date.getMonth() + 1).padStart(2, '0')}-${String(
        date.getDate()
      ).padStart(2, '0')} ${String(date.getHours()).padStart(2, '0')}:${String(
        date.getMinutes()
      ).padStart(2, '0')}:${String(date.getSeconds()).padStart(2, '0')}`;
    }
  }
};
</script>

<style scoped>
.feedback-container {
  padding: 20px;
}
.pagination {
  margin-top: 20px;
  text-align: right;
}
</style> 