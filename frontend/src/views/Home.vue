<template>
  <div class="home-page">
    <div class="hero-section">
      <div class="hero-content">
        <h1 class="hero-title">
          <Film :size="48" />
          探索精彩电影世界
        </h1>
        <p class="hero-subtitle">海量影片，高清画质，VIP尊享体验</p>
        <div class="search-box">
          <Search :size="20" />
          <input 
            type="text" 
            v-model="searchKeyword" 
            placeholder="搜索电影、演员、导演..."
            @keyup.enter="handleSearch"
          />
          <button class="search-btn" @click="handleSearch">搜索</button>
        </div>
      </div>
    </div>

    <div class="content-section">
      <div class="section-header">
        <h2 class="section-title">
          <Flame :size="24" />
          🔥 热播电影
        </h2>
      </div>

      <div v-if="loading" class="loading-container">
        <Loader2 :size="48" class="spinner" />
        <p>正在加载电影数据...</p>
      </div>

      <div v-else-if="movies.length === 0" class="empty-state">
        <Film :size="48" />
        <p>暂无电影数据</p>
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
import { Film, Search, Flame, Loader2 } from 'lucide-vue-next'
import { movieApi } from '../api'
import MovieCard from '../components/MovieCard.vue'
import PlayModal from '../components/PlayModal.vue'

const movies = ref([])
const loading = ref(true)
const searchKeyword = ref('')
const showPlayModal = ref(false)
const selectedMovie = ref(null)

async function loadHotMovies() {
  try {
    const response = await movieApi.getHotMovies()
    movies.value = response.data.records || response.data
  } catch (error) {
    console.error('加载热播电影失败:', error)
  } finally {
    loading.value = false
  }
}

async function handleSearch() {
  if (!searchKeyword.value.trim()) {
    loadHotMovies()
    return
  }
  
  loading.value = true
  try {
    const response = await movieApi.searchMovies(searchKeyword.value)
    movies.value = response.data
  } catch (error) {
    console.error('搜索失败:', error)
  } finally {
    loading.value = false
  }
}

function handlePlay(movie) {
  selectedMovie.value = movie
  showPlayModal.value = true
}

onMounted(() => {
  loadHotMovies()
})
</script>

<style scoped>
.home-page {
  min-height: 100vh;
}

.hero-section {
  background: linear-gradient(135deg, rgba(233, 69, 96, 0.3) 0%, rgba(15, 15, 35, 0.9) 50%),
              url('https://images.unsplash.com/photo-1489599849927-2ee91cede3ba?w=1920&h=800&fit=crop');
  background-size: cover;
  background-position: center;
  padding: 100px 24px 80px;
  text-align: center;
}

.hero-content {
  max-width: 800px;
  margin: 0 auto;
}

.hero-title {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 16px;
  font-size: 48px;
  font-weight: 700;
  color: #fff;
  margin-bottom: 16px;
  text-shadow: 0 4px 20px rgba(0, 0, 0, 0.5);
}

.hero-subtitle {
  font-size: 20px;
  color: rgba(255, 255, 255, 0.8);
  margin-bottom: 40px;
}

.search-box {
  display: flex;
  align-items: center;
  gap: 12px;
  background: rgba(255, 255, 255, 0.1);
  backdrop-filter: blur(10px);
  border: 1px solid rgba(255, 255, 255, 0.2);
  border-radius: 30px;
  padding: 12px 20px;
}

.search-box input {
  flex: 1;
  background: transparent;
  border: none;
  outline: none;
  color: #fff;
  font-size: 16px;
}

.search-box input::placeholder {
  color: rgba(255, 255, 255, 0.5);
}

.search-btn {
  padding: 10px 24px;
  background: linear-gradient(135deg, #e94560, #ff6b6b);
  color: #fff;
  border: none;
  border-radius: 20px;
  font-size: 14px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.3s ease;
}

.search-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 15px rgba(233, 69, 96, 0.4);
}

.content-section {
  max-width: 1400px;
  margin: 0 auto;
  padding: 40px 24px;
}

.section-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 32px;
}

.section-title {
  display: flex;
  align-items: center;
  gap: 10px;
  font-size: 28px;
  font-weight: 600;
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
  color: #e94560;
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
  .hero-title {
    font-size: 32px;
  }
  
  .hero-subtitle {
    font-size: 16px;
  }
  
  .movie-grid {
    grid-template-columns: repeat(auto-fill, minmax(180px, 1fr));
    gap: 16px;
  }
}
</style>