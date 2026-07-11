<template>
  <div class="rank-page">
    <div class="page-header">
      <h1 class="page-title">
        <Trophy :size="32" />
        电影排行榜
      </h1>
      <p class="page-subtitle">最受欢迎的精彩影片</p>
    </div>

    <div class="content-section">
      <div class="rank-tabs">
        <button 
          v-for="tab in rankTabs" 
          :key="tab.value"
          class="rank-tab"
          :class="{ active: activeTab === tab.value }"
          @click="selectTab(tab.value)"
        >
          <component :is="tab.icon" :size="18" />
          {{ tab.label }}
        </button>
      </div>

      <div v-if="loading" class="loading-container">
        <Loader2 :size="48" class="spinner" />
        <p>正在加载排行榜数据...</p>
      </div>

      <div v-else-if="movies.length === 0" class="empty-state">
        <Film :size="48" />
        <p>暂无排行数据</p>
      </div>

      <div v-else class="rank-list">
        <div 
          v-for="(movie, index) in movies" 
          :key="movie.id" 
          class="rank-item"
          @click="handlePlay(movie)"
        >
          <div class="rank-badge" :class="getRankClass(index)">
            {{ index + 1 }}
          </div>
          <div class="movie-poster">
            <img 
              :src="movie.poster || 'https://images.unsplash.com/photo-1489599849927-2ee91cede3ba?w=100&h=150&fit=crop'" 
              :alt="movie.title"
            />
            <div v-if="movie.isVip === 1" class="vip-badge">
              <Crown :size="12" />
            </div>
          </div>
          <div class="movie-info">
            <h3 class="movie-title">{{ movie.title }}</h3>
            <div class="movie-meta">
              <span class="rating">
                <Star :size="14" />
                {{ movie.rating || '暂无评分' }}
              </span>
              <span class="type">{{ movie.type }}</span>
              <span class="region">{{ movie.region }}</span>
            </div>
            <div class="movie-stats">
              <span class="play-count">
                <Eye :size="14" />
                播放量: {{ movie.playCount || 0 }}
              </span>
            </div>
          </div>
          <div class="play-btn">
            <Play :size="24" />
          </div>
        </div>
      </div>
    </div>

    <PlayModal 
      v-if="showPlayModal" 
      :movie="selectedMovie" 
      @close="showPlayModal = false" 
    />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { Trophy, Film, Loader2, Star, Crown, Play, Eye, Calendar, TrendingUp, Award } from 'lucide-vue-next'
import { movieApi } from '../api'
import PlayModal from '../components/PlayModal.vue'

const rankTabs = [
  { label: '本周排行', value: 'week', icon: Calendar },
  { label: '本月排行', value: 'month', icon: Calendar },
  { label: '全部排行', value: 'all', icon: TrendingUp },
  { label: '好评排行', value: 'rating', icon: Award }
]

const movies = ref([])
const loading = ref(true)
const activeTab = ref('week')
const showPlayModal = ref(false)
const selectedMovie = ref(null)

async function loadRank(type) {
  loading.value = true
  try {
    const response = await movieApi.getRank(type)
    movies.value = response.data.records || response.data
  } catch (error) {
    console.error('加载排行榜失败:', error)
  } finally {
    loading.value = false
  }
}

function selectTab(type) {
  activeTab.value = type
  loadRank(type)
}

function getRankClass(index) {
  if (index === 0) return 'gold'
  if (index === 1) return 'silver'
  if (index === 2) return 'bronze'
  return ''
}

function handlePlay(movie) {
  selectedMovie.value = movie
  showPlayModal.value = true
}

onMounted(() => {
  loadRank('week')
})
</script>

<style scoped>
.rank-page {
  min-height: 100vh;
}

.page-header {
  background: linear-gradient(135deg, rgba(243, 156, 18, 0.3) 0%, rgba(15, 15, 35, 0.9) 100%);
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
  max-width: 1200px;
  margin: 0 auto;
  padding: 40px 24px;
}

.rank-tabs {
  display: flex;
  gap: 12px;
  margin-bottom: 32px;
  overflow-x: auto;
}

.rank-tab {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 12px 24px;
  background: rgba(255, 255, 255, 0.05);
  border: 1px solid rgba(255, 255, 255, 0.1);
  border-radius: 25px;
  color: rgba(255, 255, 255, 0.7);
  font-size: 14px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.3s ease;
  white-space: nowrap;
}

.rank-tab:hover {
  background: rgba(255, 255, 255, 0.1);
}

.rank-tab.active {
  background: linear-gradient(135deg, #f39c12, #e67e22);
  border-color: transparent;
  color: #fff;
}

.loading-container, .empty-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 80px 20px;
  color: rgba(255, 255, 255, 0.5);
}

.loading-container p, .empty-state p {
  margin-top: 16px;
  font-size: 16px;
}

.spinner {
  animation: spin 1s linear infinite;
  color: #f39c12;
}

@keyframes spin {
  from { transform: rotate(0deg); }
  to { transform: rotate(360deg); }
}

.rank-list {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.rank-item {
  display: flex;
  align-items: center;
  gap: 16px;
  background: linear-gradient(135deg, rgba(26, 26, 62, 0.8), rgba(22, 33, 62, 0.8));
  border-radius: 16px;
  padding: 20px;
  cursor: pointer;
  transition: all 0.3s ease;
  border: 1px solid rgba(255, 255, 255, 0.05);
}

.rank-item:hover {
  transform: translateX(5px);
  border-color: rgba(233, 69, 96, 0.3);
}

.rank-badge {
  width: 40px;
  height: 40px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: rgba(255, 255, 255, 0.1);
  border-radius: 50%;
  font-size: 18px;
  font-weight: 700;
  color: rgba(255, 255, 255, 0.6);
}

.rank-badge.gold {
  background: linear-gradient(135deg, #f39c12, #f1c40f);
  color: #000;
}

.rank-badge.silver {
  background: linear-gradient(135deg, #bdc3c7, #ecf0f1);
  color: #000;
}

.rank-badge.bronze {
  background: linear-gradient(135deg, #cd7f32, #d4a574);
  color: #000;
}

.movie-poster {
  position: relative;
  width: 100px;
  height: 150px;
  border-radius: 8px;
  overflow: hidden;
}

.movie-poster img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.vip-badge {
  position: absolute;
  top: 4px;
  right: 4px;
  width: 24px;
  height: 24px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #f39c12, #e67e22);
  border-radius: 50%;
}

.movie-info {
  flex: 1;
  min-width: 0;
}

.movie-title {
  font-size: 18px;
  font-weight: 600;
  color: #fff;
  margin-bottom: 8px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.movie-meta {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 8px;
}

.rating {
  display: flex;
  align-items: center;
  gap: 4px;
  color: #f39c12;
  font-size: 13px;
}

.type, .region {
  color: rgba(255, 255, 255, 0.5);
  font-size: 12px;
}

.movie-stats {
  display: flex;
  align-items: center;
  gap: 12px;
}

.play-count {
  display: flex;
  align-items: center;
  gap: 4px;
  color: rgba(255, 255, 255, 0.5);
  font-size: 12px;
}

.play-btn {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 48px;
  height: 48px;
  background: rgba(233, 69, 96, 0.2);
  border-radius: 50%;
  color: #e94560;
  transition: all 0.3s ease;
}

.rank-item:hover .play-btn {
  background: rgba(233, 69, 96, 0.4);
  transform: scale(1.1);
}

@media (max-width: 768px) {
  .page-title {
    font-size: 28px;
  }
  
  .rank-item {
    padding: 16px;
  }
  
  .movie-poster {
    width: 80px;
    height: 120px;
  }
  
  .movie-title {
    font-size: 16px;
  }
}
</style>