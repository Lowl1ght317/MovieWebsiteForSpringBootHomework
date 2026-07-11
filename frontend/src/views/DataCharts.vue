<template>
  <div class="charts-page">
    <div class="page-header">
      <h1 class="page-title">
        <BarChart3 :size="32" />
        数据图表
      </h1>
      <p class="page-subtitle">可视化展示电影数据统计</p>
    </div>

    <div class="content-section">
      <div class="action-section">
        <button class="load-btn" @click="loadCharts" :disabled="loading">
          <RefreshCw :size="18" />
          {{ loading ? '加载中...' : '加载图表' }}
        </button>
      </div>

      <div v-if="!dataLoaded" class="empty-state">
        <BarChart3 :size="48" />
        <p>点击上方按钮加载图表数据</p>
      </div>

      <div v-else class="charts-grid">
        <div class="chart-card">
          <h3 class="chart-title">电影播放量排行</h3>
          <div ref="barChartRef" class="chart-container"></div>
        </div>

        <div class="chart-card">
          <h3 class="chart-title">电影类型分布</h3>
          <div ref="pieChartRef" class="chart-container"></div>
        </div>

        <div class="chart-card">
          <h3 class="chart-title">电影地区分布</h3>
          <div ref="regionChartRef" class="chart-container"></div>
        </div>

        <div class="chart-card">
          <h3 class="chart-title">VIP/免费电影分布</h3>
          <div ref="vipChartRef" class="chart-container"></div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue'
import { BarChart3, RefreshCw } from 'lucide-vue-next'
import * as echarts from 'echarts'
import { reportApi } from '../api'

const loading = ref(false)
const dataLoaded = ref(false)
const barChartRef = ref(null)
const pieChartRef = ref(null)
const regionChartRef = ref(null)
const vipChartRef = ref(null)

let barChart = null
let pieChart = null
let regionChart = null
let vipChart = null

async function loadCharts() {
  loading.value = true
  
  try {
    const response = await reportApi.getStats()
    const data = response.data
    
    if (!data || data.length === 0) {
      alert('暂无图表数据')
      return
    }

    initBarChart(data)
    initPieChart(data)
    initRegionChart(data)
    initVipChart(data)
    
    dataLoaded.value = true
  } catch (error) {
    console.error('加载图表失败:', error)
    alert('加载图表失败')
  } finally {
    loading.value = false
  }
}

function initBarChart(data) {
  if (!barChartRef.value) return
  
  barChart = echarts.init(barChartRef.value)
  const top10 = data.slice(0, 10)
  
  barChart.setOption({
    backgroundColor: 'transparent',
    title: { 
      text: '播放量排行 Top 10', 
      left: 'center',
      textStyle: { color: '#fff', fontSize: 14 }
    },
    tooltip: { trigger: 'axis', axisPointer: { type: 'shadow' } },
    grid: { left: '3%', right: '4%', bottom: '15%', containLabel: true },
    xAxis: { 
      type: 'category', 
      data: top10.map(d => d.title),
      axisLabel: { color: '#999', rotate: 30, fontSize: 10 },
      axisLine: { lineStyle: { color: '#333' } }
    },
    yAxis: { 
      type: 'value',
      axisLabel: { color: '#999' },
      axisLine: { lineStyle: { color: '#333' } },
      splitLine: { lineStyle: { color: '#222' } }
    },
    series: [{ 
      type: 'bar', 
      data: top10.map(d => d.playCount), 
      itemStyle: { 
        color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
          { offset: 0, color: '#e94560' },
          { offset: 1, color: '#c0392b' }
        ])
      },
      label: { show: true, position: 'top', color: '#fff', fontSize: 10 }
    }]
  })
}

function initPieChart(data) {
  if (!pieChartRef.value) return
  
  pieChart = echarts.init(pieChartRef.value)
  const typeMap = {}
  data.forEach(d => {
    const types = d.type ? d.type.split('/') : ['其他']
    types.forEach(t => {
      typeMap[t] = (typeMap[t] || 0) + 1
    })
  })
  const pieData = Object.entries(typeMap).map(([name, value]) => ({ name, value }))
  
  pieChart.setOption({
    backgroundColor: 'transparent',
    title: { 
      text: '类型分布', 
      left: 'center',
      textStyle: { color: '#fff', fontSize: 14 }
    },
    tooltip: { trigger: 'item', formatter: '{a} <br/>{b}: {c} ({d}%)' },
    legend: { 
      orient: 'vertical', 
      left: 'left',
      textStyle: { color: '#999', fontSize: 11 }
    },
    series: [{ 
      name: '类型', 
      type: 'pie', 
      radius: ['40%', '70%'],
      center: ['60%', '55%'],
      avoidLabelOverlap: false,
      itemStyle: { borderRadius: 8, borderColor: '#1a1a3e', borderWidth: 2 },
      label: { show: true, color: '#999', fontSize: 11 },
      emphasis: { label: { show: true, fontSize: 14, fontWeight: 'bold' } },
      data: pieData 
    }]
  })
}

function initRegionChart(data) {
  if (!regionChartRef.value) return
  
  regionChart = echarts.init(regionChartRef.value)
  const regionMap = {}
  data.forEach(d => {
    regionMap[d.region] = (regionMap[d.region] || 0) + 1
  })
  const regionData = Object.entries(regionMap).map(([name, value]) => ({ name, value }))
  
  regionChart.setOption({
    backgroundColor: 'transparent',
    title: { 
      text: '地区分布', 
      left: 'center',
      textStyle: { color: '#fff', fontSize: 14 }
    },
    tooltip: { trigger: 'item' },
    legend: { 
      top: 'bottom',
      textStyle: { color: '#999', fontSize: 11 }
    },
    series: [{ 
      name: '地区', 
      type: 'pie', 
      radius: [20, 90],
      center: ['50%', '50%'],
      roseType: 'area',
      itemStyle: { borderRadius: 5 },
      label: { color: '#999', fontSize: 11 },
      data: regionData 
    }]
  })
}

function initVipChart(data) {
  if (!vipChartRef.value) return
  
  vipChart = echarts.init(vipChartRef.value)
  const vipCount = data.filter(d => d.isVip == 1).length
  const freeCount = data.filter(d => d.isVip == 0).length
  
  vipChart.setOption({
    backgroundColor: 'transparent',
    title: { 
      text: 'VIP/免费分布', 
      left: 'center',
      textStyle: { color: '#fff', fontSize: 14 }
    },
    tooltip: { trigger: 'item' },
    legend: { 
      orient: 'vertical', 
      left: 'left',
      textStyle: { color: '#999', fontSize: 11 }
    },
    series: [{ 
      name: '影片类型', 
      type: 'pie', 
      radius: ['50%', '70%'],
      center: ['60%', '55%'],
      label: { 
        show: true, 
        formatter: '{b}: {c}部\n({d}%)',
        color: '#999',
        fontSize: 11
      },
      data: [
        { value: vipCount, name: 'VIP影片', itemStyle: { color: '#e94560' } },
        { value: freeCount, name: '免费影片', itemStyle: { color: '#2ecc71' } }
      ]
    }]
  })
}

function handleResize() {
  barChart?.resize()
  pieChart?.resize()
  regionChart?.resize()
  vipChart?.resize()
}

onMounted(() => {
  window.addEventListener('resize', handleResize)
})

onUnmounted(() => {
  window.removeEventListener('resize', handleResize)
  barChart?.dispose()
  pieChart?.dispose()
  regionChart?.dispose()
  vipChart?.dispose()
})
</script>

<style scoped>
.charts-page {
  min-height: 100vh;
}

.page-header {
  background: linear-gradient(135deg, rgba(30, 144, 255, 0.3) 0%, rgba(15, 15, 35, 0.9) 100%);
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
  max-width: 1400px;
  margin: 0 auto;
  padding: 40px 24px;
}

.action-section {
  margin-bottom: 32px;
}

.load-btn {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 12px 28px;
  background: linear-gradient(135deg, #1e90ff, #0066cc);
  color: #fff;
  border: none;
  border-radius: 25px;
  font-size: 14px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.3s ease;
}

.load-btn:hover:not(:disabled) {
  transform: translateY(-2px);
  box-shadow: 0 4px 15px rgba(30, 144, 255, 0.4);
}

.load-btn:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

.empty-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 100px 20px;
  color: rgba(255, 255, 255, 0.5);
}

.empty-state p {
  margin-top: 16px;
  font-size: 16px;
}

.charts-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(500px, 1fr));
  gap: 24px;
}

.chart-card {
  background: linear-gradient(135deg, rgba(26, 26, 62, 0.8), rgba(22, 33, 62, 0.8));
  border-radius: 16px;
  padding: 24px;
  border: 1px solid rgba(255, 255, 255, 0.05);
}

.chart-title {
  font-size: 16px;
  font-weight: 600;
  color: #fff;
  margin-bottom: 16px;
}

.chart-container {
  height: 300px;
}

@media (max-width: 768px) {
  .page-title {
    font-size: 28px;
  }
  
  .charts-grid {
    grid-template-columns: 1fr;
  }
  
  .chart-container {
    height: 250px;
  }
}
</style>