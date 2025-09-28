<template>
  <el-dialog 
    :title="isAdd ? '添加琴房' : '编辑琴房'" 
    :visible.sync="dialogVisible"
    width="650px"
    :close-on-click-modal="false"
    @closed="resetForm">
    
    <el-form 
      :model="roomForm" 
      :rules="rules" 
      ref="roomForm" 
      label-width="100px">
      
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="琴房号" prop="roomNumber">
            <el-input v-model="roomForm.roomNumber" placeholder="请输入琴房号"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="琴房名称" prop="name">
            <el-input v-model="roomForm.name" placeholder="请输入琴房名称"></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="所属教学楼" prop="building">
            <el-select v-model="roomForm.buildingId" placeholder="请选择教学楼" style="width: 100%">
              <el-option v-for="item in buildingList" :key="item.id" 
              :label="item.name" :value="item.id"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="琴房类型" prop="type">
            <el-select v-model="roomForm.roomTypeId" placeholder="请选择类型" style="width: 100%">
              <el-option v-for="item in roomTypeList" :key="item.id" 
              :label="item.typeName" :value="item.id"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="所属院系" prop="departmentId">
            <el-select v-model="roomForm.departmentId" placeholder="请选择院系" style="width: 100%">
              <el-option v-for="item in deptList" :key="item.id" 
              :label="item.name" :value="item.id"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="容纳人数" prop="capacity">
            <el-input-number v-model="roomForm.capacity" :min="1" :max="1000" style="width: 100%"></el-input-number>
          </el-form-item>
        </el-col>
      </el-row>
      
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="楼层" prop="floor">
            <el-input-number v-model="roomForm.floor" :min="1" :max="50" style="width: 100%"></el-input-number>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="状态" prop="status">
            <el-radio-group v-model="roomForm.status">
              <el-radio :label="1">可用</el-radio>
              <el-radio :label="0">禁用</el-radio>
            </el-radio-group>
          </el-form-item>
        </el-col>
      </el-row>
      
      <el-form-item label="设备配置">
        <el-checkbox-group v-model="selectedFacilities">
          <el-checkbox label="投影仪"></el-checkbox>
          <el-checkbox label="电脑"></el-checkbox>
          <el-checkbox label="麦克风"></el-checkbox>
          <el-checkbox label="空调"></el-checkbox>
          <el-checkbox label="智能黑板"></el-checkbox>
        </el-checkbox-group>
      </el-form-item>
      
      <el-form-item label="琴房描述" prop="description">
        <el-input 
          type="textarea" 
          v-model="roomForm.description" 
          placeholder="请输入琴房描述"
          :rows="3">
        </el-input>
      </el-form-item>
    </el-form>
    
    <div slot="footer" class="dialog-footer">
      <el-button @click="dialogVisible = false">取 消</el-button>
      <el-button type="primary" @click="submitForm" :loading="submitting">确 定</el-button>
    </div>
  </el-dialog>
</template>

<script>
import { roomApi } from '@/api/room'
import { roomTypeApi } from '@/api/roomType'
import { buildingApi } from '@/api/building'
import deptApi from '@/api/dept'

export default {
  name: 'RoomForm',
  props: {
    // 对话框可见性
    visible: {
      type: Boolean,
      default: false
    },
    // 琴房对象
    room: {
      type: Object,
      default: () => ({})
    },
    // 是否为添加操作
    isAdd: {
      type: Boolean,
      default: true
    }
  },
  data() {
    return {
      buildingList: [],
      roomTypeList: [],
      deptList: [], // 院系列表
      // 提交中状态
      submitting: false,
      // 表单数据
      roomForm: {
        id: undefined,
        roomNumber: '',
        name: '',
        buildingId: '',
        roomTypeId: '',
        departmentId: '', // 院系ID
        capacity: 50,
        floor: 1,
        facilities: '',
        status: 1,
        description: ''
      },
      // 选中的设施
      selectedFacilities: [],
      // 表单验证规则
      rules: {
        roomNumber: [
          { required: true, message: '请输入琴房号', trigger: 'blur' },
          { max: 20, message: '琴房号长度不能超过20个字符', trigger: 'blur' }
        ],
        name: [
          { required: true, message: '请输入琴房名称', trigger: 'blur' },
          { max: 50, message: '琴房名称长度不能超过50个字符', trigger: 'blur' }
        ],
        buildingId: [
          { required: true, message: '请选择所属教学楼', trigger: 'change' }
        ],
        roomTypeId: [
          { required: true, message: '请选择琴房类型', trigger: 'change' }
        ],
        departmentId: [
          { required: true, message: '请选择所属院系', trigger: 'change' }
        ],
        capacity: [
          { required: true, message: '请输入容纳人数', trigger: 'blur' }
        ],
        floor: [
          { required: true, message: '请输入楼层', trigger: 'blur' }
        ],
        status: [
          { required: true, message: '请选择状态', trigger: 'change' }
        ]
      }
    }
  },
  computed: {
    // 对话框可见性,用于支持.sync修饰符
    dialogVisible: {
      get() {
        return this.visible
      },
      set(val) {
        this.$emit('update:visible', val)
      }
    }
  },
  watch: {
    // 监听visible属性变化，当对话框显示时初始化表单
    visible(val) {
      if (val && this.isAdd) {
        // 重置表单初始值
        this.resetForm()
      }
      if (val) {
        // 当对话框显示时，重新获取琴房类型列表和院系列表
        this.fetchRoomTypeList()
        this.fetchDeptList() // 获取院系列表
      }
    },
    // 监听room属性变化，填充表单
    room: {
      handler(val) {
        if (val && Object.keys(val).length > 0 && !this.isAdd) {
          // 处理后端返回的数据格式
          if (typeof val.data === 'object' && val.data !== null) {
            // 如果返回的是嵌套在data中的对象
            this.roomForm = { ...val.data }
          } else {
            // 直接使用返回的对象
            this.roomForm = { ...val }
          }
          
          // 确保数字类型的字段是数字
          if (this.roomForm.capacity !== undefined) {
            this.roomForm.capacity = Number(this.roomForm.capacity)
          }
          if (this.roomForm.floor !== undefined) {
            this.roomForm.floor = Number(this.roomForm.floor)
          }
          if (this.roomForm.status !== undefined) {
            this.roomForm.status = Number(this.roomForm.status)
          }
          
          // 处理设施列表
          this.selectedFacilities = this.roomForm.facilities ? this.roomForm.facilities.split(',') : []
        }
      },
      deep: true,
      immediate: true
    }
  },
  created() {
    // 组件创建时获取琴房类型列表和建筑列表
    this.fetchRoomTypeList()
    this.fetchBuildingList()
    this.fetchDeptList() // 获取院系列表
  },
  methods: {
    // 获取建筑列表
    fetchBuildingList() {
      buildingApi.getAllBuildings().then(res => {
        this.buildingList = res.data || []
      })
    },
    // 获取琴房类型列表
    fetchRoomTypeList() {
      console.log('正在获取琴房类型列表...')
      roomTypeApi.list().then(res => {
        if(res && res.code === 1 && res.data){
          this.roomTypeList = res.data.list || []
          console.log('获取琴房类型列表成功:', this.roomTypeList)
        } else {
          this.$message.error('获取琴房类型列表失败: ' + (res && res.msg || '未知错误'))
        }
      }).catch(error => {
        console.error('获取琴房类型列表异常:', error)
        this.$message.error('获取琴房类型列表失败')
      })
    },
    // 获取院系列表
    fetchDeptList() {
      console.log('正在获取院系列表...')
      deptApi.list().then(res => {
        if(res && res.data){
          this.deptList = res.data || []
          console.log('获取院系列表成功:', this.deptList)
        } else {
          this.$message.error('获取院系列表失败: ' + (res && res.msg || '未知错误'))
        }
      }).catch(error => {
        console.error('获取院系列表异常:', error)
        this.$message.error('获取院系列表失败')
      })
    },
    
    // 提交表单
    submitForm() {
      this.$refs.roomForm.validate(valid => {
        if (!valid) return
        
        // 处理设施
        this.roomForm.facilities = this.selectedFacilities.join(',')
        
        this.submitting = true
        
        // 构建提交数据
        const submitData = { ...this.roomForm }
        
        // 根据是否为添加操作选择API
        const apiMethod = this.isAdd ? roomApi.save : roomApi.update
        
        apiMethod(submitData)
          .then(response => {
            // 提交成功
            this.$message({
              type: 'success',
              message: this.isAdd ? '添加成功' : '更新成功'
            })
            this.dialogVisible = false
            this.$emit('success')
          })
          .catch(error => {
            console.error('提交失败', error)
            this.$message.error('操作失败: ' + (error.message || '未知错误'))
          })
          .finally(() => {
            this.submitting = false
          })
      })
    },
    
    // 重置表单
    resetForm() {
      if (this.$refs.roomForm) {
        this.$refs.roomForm.resetFields()
      }
      
      this.roomForm = {
        id: undefined,
        roomNumber: '',
        name: '',
        building: '',
        roomTypeId: '',
        deptId: '', // 院系ID
        capacity: 50,
        floor: 1,
        facilities: '',
        status: 1,
        description: ''
      }
      this.selectedFacilities = []
    }
  }
}
</script>

<style scoped>
.el-input-number {
  width: 100%;
}
</style> 