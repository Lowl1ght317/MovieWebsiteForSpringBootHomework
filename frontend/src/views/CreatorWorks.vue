<template>
  <div class="creator-page">
    <div class="page-header">
      <h1 class="page-title">
        <Users :size="32" />
        主创作品
      </h1>
      <p class="page-subtitle">根据演员或导演探索精彩电影</p>
    </div>

    <div class="content-section">
      <div class="search-section">
        <div class="search-tabs">
          <button 
            class="search-tab" 
            :class="{ active: searchType === 'actor' }"
            @click="searchType = 'actor'"
          >
            <User :size="18" />
            搜索演员
          </button>
          <button 
            class="search-tab" 
            :class="{ active: searchType === 'director' }"
            @click="searchType = 'director'"
          >
            <Video :size="18" />
            搜索导演
          </button>
        </div>

        <div class="search-input-wrapper">
          <Search :size="20" />
          <input 
            type="text" 
            v-model="searchKeyword" 
            placeholder="输入演员或导演名字..."
            @keyup.enter="handleSearch"
          />
          <button class="search-btn" @click="handleSearch">搜索</button>
        </div>
      </div>

      <div v-if="searchResults.length > 0" class="results-section">
        <h3 class="results-title">搜索结果</h3>
        <div class="results-list">
          <div 
            v-for="item in searchResults" 
            :key="item.id" 
            class="result-item"
            @click="selectCreator(item)"
          >
            <UserCircle :size="40" />
            <div class="result-info">
              <span class="result-name">{{ item.name }}</span>
              <span class="result-country">{{ item.country || '未知国籍' }}</span>
            </div>
            <ChevronRight :size="20" />
          </div>
        </div>
      </div>

      <div v-if="selectedCreator && movies.length > 0" class="movies-section">
        <h3 class="movies-title">
          {{ searchType === 'actor' ? '主演作品' : '执导作品' }} - {{ selectedCreator.name }}
        </h3>
        <div class="movie-grid">
          <MovieCard 
            v-for="movie in movies" 
            :key="movie.id" 
            :movie="movie"
            @play="handlePlay"
          />
        </div>
      </div>

      <div v-if="loading" class="loading-container">
        <Loader2 :size="48" class="spinner" />
        <p>正在加载数据...</p>
      </div>

      <div v-if="!loading && searchKeyword && searchResults.length === 0 && !selectedCreator" class="empty-state">
        <Users :size="48" />
        <p>未找到相关{{ searchType === 'actor' ? '演员' : '导演' }}</p>
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
import { ref } from 'vue'
import { Users, User, Video, Search, UserCircle, ChevronRight, Loader2 } from 'lucide-vue-next'
import { creatorApi } from '../api'
import MovieCard from '../components/MovieCard.vue'
import PlayModal from '../components/PlayModal.vue'

const searchType = ref('actor')
const searchKeyword = ref('')
const searchResults = ref([])
const selectedCreator = ref(null)
const movies = ref([])
const loading = ref(false)
const showPlayModal = ref(false)
const selectedMovie = ref(null)

async function handleSearch() {
  if (!searchKeyword.value.trim()) return
  
  loading.value = true
  searchResults.value = []
  selectedCreator.value = null
  movies.value = []
  
  try {
    if (searchType.value === 'actor') {
      const response = await creatorApi.searchActor(searchKeyword.value)
      searchResults.value = response.data
    } else {
      const response = await creatorApi.searchDirector(searchKeyword.value)
      searchResults.value = response.data
    }
  } catch (error) {
    console.error('搜索失败:', error)
  } finally {
    loading.value = false
  }
}

async function selectCreator(creator) {
  selectedCreator.value = creator
  searchResults.value = []
  loading.value = true
  
  try {
    if (searchType.value === 'actor') {
      const response = await creatorApi.getActorMovies(creator.id)
      movies.value = response.data
    } else {
      const response = await creatorApi.getDirectorMovies(creator.id)
      movies.value = response.data
    }
  } catch (error) {
    console.error('加载作品失败:', error)
  } finally {
    loading.value = false
  }
}

function handlePlay(movie) {
  selectedMovie.value = movie
  showPlayModal.value = true
}
</script>

<style scoped>
.creator-page {
  min-height: 100vh;
}

.page-header {
  background: linear-gradient(135deg, rgba(155, 89, 182, 0.3) 0%, rgba(15, 15, 35, 0.9) 100%);
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

.search-section {
  margin-bottom: 32px;
}

.search-tabs {
  display: flex;
  gap: 8px;
  margin-bottom: 16px;
}

.search-tab {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 10px 20px;
  background: rgba(255, 255, 255, 0.05);
  border: 1px solid rgba(255, 255, 255, 0.1);
  border-radius: 20px;
  color: rgba(255, 255, 255, 0.7);
  font-size: 14px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.3s ease;
}

.search-tab:hover {
  background: rgba(255, 255, 255, 0.1);
}

.search-tab.active {
  background: linear-gradient(135deg, #9b59b6, #8e44ad);
  border-color: transparent;
  color: #fff;
}

.search-input-wrapper {
  display: flex;
  align-items: center;
  gap: 12px;
  background: rgba(255, 255, 255, 0.05);
  border: 1px solid rgba(255, 255, 255, 0.1);
  border-radius: 30px;
  padding: 14px 20px;
}

.search-input-wrapper input {
  flex: 1;
  background: transparent;
  border: none;
  outline: none;
  color: #fff;
  font-size: 16px;
}

.search-input-wrapper input::placeholder {
  color: rgba(255, 255, 255, 0.4);
}

.search-btn {
  padding: 10px 24px;
  background: linear-gradient(135deg, #9b59b6, #8e44ad);
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
  box-shadow: 0 4px 15px rgba(155, 89, 182, 0.4);
}

.results-section {
  margin-bottom: 32px;
}

.results-title {
  font-size: 18px;
  font-weight: 600;
  color: #fff;
  margin-bottom: 16px;
}

.results-list {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.result-item {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 16px;
  background: linear-gradient(135deg, rgba(26, 26, 62, 0.6), rgba(22, 33, 62, 0.6));
  border-radius: 12px;
  cursor: pointer;
  transition: all 0.3s ease;
  border: 1px solid rgba(255, 255, 255, 0.05);
}

.result-item:hover {
  background: linear-gradient(135deg, rgba(26, 26, 62, 0.8), rgba(22, 33, 62, 0.8));
  border-color: rgba(155, 89, 182, 0.3);
}

.result-info {
  flex: 1;
}

.result-name {
  display: block;
  font-size: 16px;
  font-weight: 600;
  color: #fff;
}

.result-country {
  display: block;
  font-size: 12px;
  color: rgba(255, 255, 255, 0.5);
  margin-top: 4px;
}

.movies-section {
  margin-top: 32px;
}

.movies-title {
  font-size: 20px;
  font-weight: 600;
  color: #fff;
  margin-bottom: 20px;
}

.movie-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(220px, 1fr));
  gap: 24px;
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
  color: #9b59b6;
}

@keyframes spin {
  from { transform: rotate(0deg); }
  to { transform: rotate(360deg); }
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