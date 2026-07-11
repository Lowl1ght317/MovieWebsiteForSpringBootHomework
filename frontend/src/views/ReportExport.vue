<template>
  <div class="export-page">
    <div class="page-header">
      <h1 class="page-title">
        <Download :size="32" />
        导出报表
      </h1>
      <p class="page-subtitle">导出电影数据报表</p>
    </div>

    <div class="content-section">
      <div class="export-card">
        <div class="card-icon">
          <CalendarDays :size="32" />
        </div>
        <div class="card-content">
          <h3 class="card-title">按日期导出播放记录</h3>
          <p class="card-desc">选择日期范围，导出该时间段内的电影播放记录报表</p>
          <div class="date-picker">
            <div class="date-item">
              <label>开始日期</label>
              <input type="date" v-model="startDate" />
            </div>
            <div class="date-item">
              <label>结束日期</label>
              <input type="date" v-model="endDate" />
            </div>
          </div>
          <button class="export-btn" @click="exportReport">
            <FileSpreadsheet :size="18" />
            导出Excel
          </button>
        </div>
      </div>

      <div class="export-card">
        <div class="card-icon">
          <Film :size="32" />
        </div>
        <div class="card-content">
          <h3 class="card-title">导出全部电影榜单</h3>
          <p class="card-desc">导出系统中所有电影的详细信息榜单</p>
          <button class="export-btn secondary" @click="exportAllMovies">
            <FileSpreadsheet :size="18" />
            导出全部电影榜单
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { Download, CalendarDays, FileSpreadsheet, Film } from 'lucide-vue-next'
import { reportApi } from '../api'

const startDate = ref('')
const endDate = ref('')

function exportReport() {
  if (!startDate.value || !endDate.value) {
    alert('请选择开始和结束日期')
    return
  }
  
  const url = reportApi.exportReport(startDate.value, endDate.value).url
  window.location.href = url
}

function exportAllMovies() {
  const url = reportApi.exportAllMovies().url
  window.location.href = url
}
</script>

<style scoped>
.export-page {
  min-height: 100vh;
}

.page-header {
  background: linear-gradient(135deg, rgba(46, 204, 113, 0.3) 0%, rgba(15, 15, 35, 0.9) 100%);
  padding: 60px 24px 40px;
  text-align: center;
}

.page-title {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 12px;
  font-size: 36px;
  font-weight: 700;
  color: #fff;
  margin-bottom: 8px;
}

.page-subtitle {
  font-size: 16px;
  color: rgba(255, 255, 255, 0.6);
}

.content-section {
  max-width: 800px;
  margin: 0 auto;
  padding: 40px 24px;
  display: flex;
  flex-direction: column;
  gap: 24px;
}

.export-card {
  display: flex;
  gap: 24px;
  background: linear-gradient(135deg, rgba(26, 26, 62, 0.8), rgba(22, 33, 62, 0.8));
  border-radius: 20px;
  padding: 32px;
  border: 1px solid rgba(255, 255, 255, 0.05);
}

.card-icon {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 80px;
  height: 80px;
  background: linear-gradient(135deg, rgba(233, 69, 96, 0.2), rgba(233, 69, 96, 0.1));
  border-radius: 20px;
  color: #e94560;
}

.card-content {
  flex: 1;
}

.card-title {
  font-size: 20px;
  font-weight: 600;
  color: #fff;
  margin-bottom: 8px;
}

.card-desc {
  font-size: 14px;
  color: rgba(255, 255, 255, 0.5);
  margin-bottom: 20px;
}

.date-picker {
  display: flex;
  gap: 20px;
  margin-bottom: 20px;
}

.date-item {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.date-item label {
  font-size: 13px;
  color: rgba(255, 255, 255, 0.6);
}

.date-item input {
  padding: 12px 16px;
  background: rgba(255, 255, 255, 0.05);
  border: 1px solid rgba(255, 255, 255, 0.1);
  border-radius: 10px;
  color: #fff;
  font-size: 14px;
  cursor: pointer;
}

.date-item input:focus {
  outline: none;
  border-color: #e94560;
}

.date-item input::-webkit-calendar-picker-indicator {
  filter: invert(1);
}

.export-btn {
  display: inline-flex;
  align-items: center;
  gap: 8px;
  padding: 12px 28px;
  background: linear-gradient(135deg, #e94560, #ff6b6b);
  color: #fff;
  border: none;
  border-radius: 25px;
  font-size: 14px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.3s ease;
}

.export-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 15px rgba(233, 69, 96, 0.4);
}

.export-btn.secondary {
  background: linear-gradient(135deg, #3498db, #2980b9);
}

.export-btn.secondary:hover {
  box-shadow: 0 4px 15px rgba(52, 152, 219, 0.4);
}

@media (max-width: 768px) {
  .page-title {
    font-size: 28px;
  }
  
  .export-card {
    flex-direction: column;
    padding: 24px;
  }
  
  .card-icon {
    width: 60px;
    height: 60px;
  }
  
  .date-picker {
    flex-direction: column;
  }
}
</style>