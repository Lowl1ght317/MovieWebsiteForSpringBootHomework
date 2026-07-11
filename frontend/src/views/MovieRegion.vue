<template>
  <div class="region-page">
    <div class="page-header">
      <h1 class="page-title">
        <Globe :size="32" />
        地区筛选
      </h1>
      <p class="page-subtitle">探索世界各地的精彩电影</p>
    </div>

    <div class="content-section">
      <div class="filter-section">
        <div class="filter-tags">
          <button 
            v-for="region in regions" 
            :key="region"
            class="filter-tag"
            :class="{ active: selectedRegion === region }"
            @click="selectRegion(region)"
          >
            {{ region }}
          </button>
        </div>
      </div>

      <div v-if="loading" class="loading-container">
        <Loader2 :size="48" class="spinner" />
        <p>正在加载电影数据...</p>
      </div>

      <div v-else-if="movies.length === 0" class="empty-state">
        <Film :size="48" />
        <p>该地区暂无电影</p>
      </div>

      <div v-else class="movie-grid">
        <MovieCard 
          v-for="movie in movies" 
          :key="movie.id" 
          :movie="movie"
          @play="handlePlay"
        />
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
import { Globe, Film, Loader2 } from 'lucide-vue-next'
import { movieApi } from '../api'
import MovieCard from '../components/MovieCard.vue'
import PlayModal from '../components/PlayModal.vue'

const regions = ref([])
const movies = ref([])
const loading = ref(true)
const selectedRegion = ref('')
const showPlayModal = ref(false)
const selectedMovie = ref(null)

async function loadRegions() {
  try {
    const response = await movieApi.getAllRegions()
    regions.value = response.data
    if (regions.value.length > 0) {
      selectedRegion.value = regions.value[0]
      loadMoviesByRegion(regions.value[0])
    } else {
      loading.value = false
    }
  } catch (error) {
    console.error('加载地区失败:', error)
    loading.value = false
  }
}

async function loadMoviesByRegion(region) {
  loading.value = true
  try {
    const response = await movieApi.getMoviesByRegion(region)
    movies.value = response.data.records || response.data
  } catch (error) {
    console.error('加载电影失败:', error)
  } finally {
    loading.value = false
  }
}

function selectRegion(region) {
  selectedRegion.value = region
  loadMoviesByRegion(region)
}

function handlePlay(movie) {
  selectedMovie.value = movie
  showPlayModal.value = true
}

onMounted(() => {
  loadRegions()
})
</script>

<style scoped>
.region-page {
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
  max-width: 1400px;
  margin: 0 auto;
  padding: 40px 24px;
}

.filter-section {
  margin-bottom: 32px;
}

.filter-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 12px;
}

.filter-tag {
  padding: 10px 24px;
  background: rgba(255, 255, 255, 0.05);
  border: 1px solid rgba(255, 255, 255, 0.1);
  border-radius: 25px;
  color: rgba(255, 255, 255, 0.7);
  font-size: 14px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.3s ease;
}

.filter-tag:hover {
  background: rgba(255, 255, 255, 0.1);
  border-color: rgba(255, 255, 255, 0.2);
}

.filter-tag.active {
  background: linear-gradient(135deg, #2ecc71, #27ae60);
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
  color: #2ecc71;
}

@keyframes spin {
  from { transform: rotate(0deg); }
  to { transform: rotate(360deg); }
}

.movie-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(220px, 1fr));
  gap: 24px;
}

@media (max-width: 768px) {
  .page-title {
    font-size: 28px;
  }
  
  .movie-grid {
    grid-template-columns: repeat(auto-fill, minmax(180px, 1fr));
    gap: 16px;
  }
}
</style>