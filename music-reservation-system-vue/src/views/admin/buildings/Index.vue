<template>
  <div class="buildings-container">
    <h2 class="page-title">建筑管理</h2>
    
    <!-- 筛选和搜索 -->
    <el-card shadow="hover" class="filter-container">
      <div slot="header" class="clearfix">
        <span>建筑列表</span>
        <el-button type="primary" size="small" icon="el-icon-plus" @click="handleAdd">
          添加建筑
        </el-button>
      </div>
      
      <el-form :inline="true" :model="queryParams" class="filter-form">
        <el-form-item label="搜索">
          <el-input v-model="queryParams.keyword" placeholder="建筑名称/描述" clearable>
            <el-button slot="append" icon="el-icon-search" @click="handleSearch"></el-button>
          </el-input>
        </el-form-item>
        
        <el-form-item>
          <el-button type="primary" @click="handleSearch">查询</el-button>
          <el-button @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>

      <!-- 建筑列表表格 -->
      <el-table
        v-loading="loading"
        :data="buildingList"
        border
        stripe
        @selection-change="handleSelectionChange">
        <el-table-column prop="name" label="建筑名称" show-overflow-tooltip></el-table-column>
        <el-table-column prop="description" label="建筑描述" show-overflow-tooltip></el-table-column>
        <el-table-column prop="createdAt" label="创建时间" width="180">
          <template slot-scope="scope">
            {{ formatDateTime(scope.row.createdAt) }}
          </template>
        </el-table-column>
        <el-table-column prop="updatedAt" label="更新时间" width="180">
          <template slot-scope="scope">
            {{ formatDateTime(scope.row.updatedAt) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="220" align="center">
          <template slot-scope="scope">
            <transition-group name="fade">
              <el-button 
                key="view"
                size="mini" 
                type="primary" 
                icon="el-icon-view" 
                @click="handleView(scope.row)"
                title="查看">
              </el-button>
              <el-button 
                key="edit"
                size="mini" 
                type="success" 
                icon="el-icon-edit" 
                @click="handleUpdate(scope.row)"
                title="编辑">
              </el-button>
              <el-button
                key="delete"
                size="mini"
                type="danger"
                icon="el-icon-delete"
                @click="handleDelete(scope.row)"
                title="删除">
              </el-button>
            </transition-group>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <div class="pagination-container">
        <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="queryParams.pageNum"
          :page-sizes="[10, 20, 50, 100]"
          :page-size="queryParams.pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total">
        </el-pagination>
      </div>
    </el-card>
    
    <!-- 添加/编辑建筑对话框 -->
    <building-form
      :visible.sync="formDialogVisible"
      :title="formDialogTitle"
      :building="selectedBuilding"
      @success="handleFormSuccess"
    />
    
    <!-- 查看建筑详情对话框 -->
    <building-detail
      :visible.sync="detailDialogVisible"
      :building="viewingBuilding"
    />
  </div>
</template>

<script>
import { buildingApi } from '@/api/building'
import BuildingForm from './components/BuildingForm'
import BuildingDetail from './components/BuildingDetail'
import { formatDate } from '@/utils/date'
import {
  getMockBuildingData,
  mockAddBuilding,
  mockUpdateBuilding,
  mockDeleteBuilding,
  mockGetBuildingById
} from './mock'

// 是否使用模拟数据
const useMockData = false;

export default {
  name: 'Buildings',
  components: {
    BuildingForm,
    BuildingDetail
  },
  data() {
    return {
      // 加载状态
      loading: false,
      // 建筑列表
      buildingList: [],
      // 总记录数
      total: 0,
      // 选中的行
      selectedRows: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        keyword: ''
      },
      // 对话框显示状态
      formDialogVisible: false,
      detailDialogVisible: false,
      // 对话框标题
      formDialogTitle: '',
      // 当前选中的建筑（用于表单）
      selectedBuilding: {},
      // 当前查看的建筑（用于详情）
      viewingBuilding: {}
    }
  },
  created() {
    this.getList()
  },
  methods: {
    // 获取建筑列表
    getList() {
      this.loading = true
      
  
      buildingApi.getAllBuildings().then(response => {
        this.buildingList = response.data || []
        this.loading = false
      }).catch(() => {
        this.loading = false
      })
    },
    
    // 搜索
    handleSearch() {
      this.queryParams.pageNum = 1
      this.getList()
    },
    
    // 重置查询条件
    resetQuery() {
      this.queryParams = {
        pageNum: 1,
        pageSize: 10,
        keyword: ''
      }
      this.getList()
    },
    
    // 选择项变化
    handleSelectionChange(selection) {
      this.selectedRows = selection
    },
    
    // 每页大小变化
    handleSizeChange(val) {
      this.queryParams.pageSize = val
      this.getList()
    },
    
    // 页码变化
    handleCurrentChange(val) {
      this.queryParams.pageNum = val
      this.getList()
    },
    
    // 添加建筑
    handleAdd() {
      this.formDialogTitle = '添加建筑'
      this.selectedBuilding = {
        name: '',
        description: ''
      }
      this.formDialogVisible = true
    },
    
    // 更新建筑
    handleUpdate(row) {
      this.formDialogTitle = '编辑建筑'
      this.selectedBuilding = JSON.parse(JSON.stringify(row))
      this.formDialogVisible = true
    },
    
    // 查看建筑
    handleView(row) {
      this.viewingBuilding = JSON.parse(JSON.stringify(row))
      this.detailDialogVisible = true
    },
    
    // 删除建筑
    handleDelete(row) {
      this.$confirm('确认删除该建筑吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        // 根据是否使用模拟数据决定调用方式
        buildingApi.deleteBuilding(row.id).then(response => {
          this.$message.success('删除成功')
          this.getList()
        }).catch(() => {
          this.$message.error('删除失败')
        })
      }).catch(() => {
        // 取消删除
      })
    },
    
    // 表单提交成功
    handleFormSuccess() {
      this.getList()
    },
    
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
.buildings-container {
  padding: 20px;
}

.page-title {
  margin-bottom: 20px;
}

.filter-container {
  margin-bottom: 20px;
}

.filter-form {
  margin-bottom: 10px;
}

.pagination-container {
  margin-top: 20px;
  text-align: right;
}

/* 过渡动画 */
.fade-enter-active, .fade-leave-active {
  transition: opacity .3s;
}
.fade-enter, .fade-leave-to {
  opacity: 0;
}
</style> 