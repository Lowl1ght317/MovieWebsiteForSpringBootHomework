<template>
  <div class="modal-overlay" @click.self="$emit('close')">
    <div class="modal-content">
      <div class="modal-header">
        <h2 class="modal-title">
          <Film :size="24" />
          {{ movie?.title }}
        </h2>
        <button class="close-btn" @click="$emit('close')">
          <X :size="20" />
        </button>
      </div>

      <div v-if="loading" class="loading-state">
        <Loader2 :size="48" class="spinner" />
        <p>正在加载播放资源...</p>
      </div>

      <div v-else-if="error" class="error-state">
        <AlertCircle :size="48" />
        <p>{{ error }}</p>
        <button class="retry-btn" @click="handlePlay">重试</button>
      </div>

      <div v-else class="video-container">
        <video 
          ref="videoRef" 
          :src="videoUrl" 
          controls 
          class="video-player"
        >
          您的浏览器不支持视频播放
        </video>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { Film, X, Loader2, AlertCircle } from 'lucide-vue-next'
import { movieApi } from '../api'
import store from '../store'

const props = defineProps({
  movie: {
    type: Object,
    required: true
  }
})

defineEmits(['close'])

const loading = ref(false)
const error = ref('')
const videoUrl = ref('')
const videoRef = ref(null)

async function handlePlay() {
  if (!store.isLoggedIn) {
    error.value = '请先登录后再观看'
    return
  }

  loading.value = true
  error.value = ''
  
  try {
    const response = await movieApi.playMovie(props.movie.id)
    if (response.data.success) {
      videoUrl.value = response.data.videoUrl
    } else {
      error.value = response.data.message
    }
  } catch (err) {
    error.value = '播放请求失败，请重试'
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  handlePlay()
})
</script>

<style scoped>
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.9);
  backdrop-filter: blur(10px);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 2000;
}

.modal-content {
  background: linear-gradient(135deg, #1a1a3e, #16213e);
  border-radius: 20px;
  padding: 24px;
  width: 100%;
  max-width: 800px;
  max-height: 80vh;
  overflow-y: auto;
  border: 1px solid rgba(255, 255, 255, 0.1);
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.5);
}

.modal-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 20px;
}

.modal-title {
  display: flex;
  align-items: center;
  gap: 10px;
  font-size: 20px;
  font-weight: 600;
  color: #fff;
}

.close-btn {
  background: rgba(255, 255, 255, 0.1);
  border: none;
  border-radius: 50%;
  width: 36px;
  height: 36px;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  color: #fff;
  transition: all 0.3s ease;
}

.close-btn:hover {
  background: rgba(233, 69, 96, 0.3);
  color: #e94560;
}

.loading-state, .error-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 60px 20px;
  color: rgba(255, 255, 255, 0.6);
}

.loading-state p, .error-state p {
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

.error-state {
  color: #e94560;
}

.retry-btn {
  margin-top: 20px;
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

.retry-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 15px rgba(233, 69, 96, 0.4);
}

.video-container {
  border-radius: 12px;
  overflow: hidden;
}

.video-player {
  width: 100%;
  height: 450px;
  background: #000;
}
</style>