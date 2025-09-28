// 模拟建筑数据列表
export const mockBuildingList = [
  {
    id: 1,
    name: '信息科学楼',
    description: '位于校区东部，主要用于计算机科学和电子工程课程',
    createdAt: '2023-01-01T08:00:00',
    updatedAt: '2023-01-01T08:00:00'
  },
  {
    id: 2,
    name: '工程技术楼',
    description: '位于校区北部，主要用于机械工程和材料科学课程',
    createdAt: '2023-01-02T09:00:00',
    updatedAt: '2023-01-02T09:00:00'
  },
  {
    id: 3,
    name: '理学楼',
    description: '位于校区中部，主要用于物理、化学和数学课程',
    createdAt: '2023-01-03T10:00:00',
    updatedAt: '2023-01-03T10:00:00'
  },
  {
    id: 4,
    name: '图书馆',
    description: '校区中心的主图书馆，提供自习空间和图书借阅',
    createdAt: '2023-01-04T11:00:00',
    updatedAt: '2023-01-04T11:00:00'
  },
  {
    id: 5,
    name: '行政楼',
    description: '校区南部的行政办公楼，包含各部门办公室',
    createdAt: '2023-01-05T12:00:00',
    updatedAt: '2023-01-05T12:00:00'
  }
];

// 生成自增ID
let currentId = mockBuildingList.length + 1;

/**
 * 根据查询条件获取模拟数据
 * @param {Object} params - 查询条件
 * @returns {Object} - 符合条件的结果
 */
export function getMockBuildingData(params = {}) {
  let filteredData = [...mockBuildingList];
  
  // 关键字过滤
  if (params.keyword) {
    const keyword = params.keyword.toLowerCase();
    filteredData = filteredData.filter(item => 
      item.name.toLowerCase().includes(keyword) || 
      item.description.toLowerCase().includes(keyword)
    );
  }
  
  // 计算总记录数
  const total = filteredData.length;
  
  // 返回结果
  return {
    code: 1,
    msg: 'success',
    data: filteredData,
    total: total
  };
}

/**
 * 添加模拟建筑数据
 * @param {Object} building - 建筑对象
 * @returns {Object} - 操作结果
 */
export function mockAddBuilding(building) {
  const newBuilding = {
    ...building,
    id: currentId++,
    createdAt: new Date().toISOString(),
    updatedAt: new Date().toISOString()
  };
  
  mockBuildingList.push(newBuilding);
  
  return {
    code: 1,
    msg: 'success',
    data: newBuilding
  };
}

/**
 * 更新模拟建筑数据
 * @param {Object} building - 建筑对象
 * @returns {Object} - 操作结果
 */
export function mockUpdateBuilding(building) {
  const index = mockBuildingList.findIndex(item => item.id === building.id);
  
  if (index === -1) {
    return {
      code: 0,
      msg: '建筑不存在',
      data: null
    };
  }
  
  const updatedBuilding = {
    ...mockBuildingList[index],
    ...building,
    updatedAt: new Date().toISOString()
  };
  
  mockBuildingList[index] = updatedBuilding;
  
  return {
    code: 1,
    msg: 'success',
    data: updatedBuilding
  };
}

/**
 * 删除模拟建筑数据
 * @param {number} id - 建筑ID
 * @returns {Object} - 操作结果
 */
export function mockDeleteBuilding(id) {
  const index = mockBuildingList.findIndex(item => item.id === id);
  
  if (index === -1) {
    return {
      code: 0,
      msg: '建筑不存在',
      data: null
    };
  }
  
  mockBuildingList.splice(index, 1);
  
  return {
    code: 1,
    msg: 'success',
    data: null
  };
}

/**
 * 根据ID获取模拟建筑数据
 * @param {number} id - 建筑ID
 * @returns {Object} - 操作结果
 */
export function mockGetBuildingById(id) {
  const building = mockBuildingList.find(item => item.id === id);
  
  if (!building) {
    return {
      code: 0,
      msg: '建筑不存在',
      data: null
    };
  }
  
  return {
    code: 1,
    msg: 'success',
    data: building
  };
} 