<template>
  <div class="movie-card" @click="handleClick">
    <div class="card-poster">
      <img 
        :src="movie.poster || 'https://images.unsplash.com/photo-1489599849927-2ee91cede3ba?w=300&h=450&fit=crop'" 
        :alt="movie.title"
        class="poster-img"
      />
      <div v-if="movie.isVip === 1" class="vip-badge">
        <Crown :size="14" />
        VIP
      </div>
      <div class="play-overlay">
        <Play :size="36" />
      </div>
    </div>
    <div class="card-info">
      <h3 class="movie-title">{{ movie.title }}</h3>
      <div class="movie-meta">
        <span class="rating">
          <Star :size="14" />
          {{ movie.rating || '暂无评分' }}
        </span>
        <span class="type">{{ movie.type }}</span>
      </div>
      <div class="movie-region">{{ movie.region }}</div>
    </div>
  </div>
</template>

<script setup>
import { Play, Crown, Star } from 'lucide-vue-next'

const props = defineProps({
  movie: {
    type: Object,
    required: true
  }
})

const emit = defineEmits(['play'])

function handleClick() {
  emit('play', props.movie)
}
</script>

<style scoped>
.movie-card {
  background: linear-gradient(135deg, #1a1a3e, #16213e);
  border-radius: 16px;
  overflow: hidden;
  cursor: pointer;
  transition: all 0.4s cubic-bezier(0.4, 0, 0.2, 1);
  border: 1px solid rgba(255, 255, 255, 0.05);
}

.movie-card:hover {
  transform: translateY(-8px) scale(1.02);
  border-color: rgba(233, 69, 96, 0.3);
  box-shadow: 0 20px 40px rgba(0, 0, 0, 0.4);
}

.card-poster {
  position: relative;
  width: 100%;
  padding-top: 150%;
  overflow: hidden;
}

.poster-img {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.4s ease;
}

.movie-card:hover .poster-img {
  transform: scale(1.1);
}

.vip-badge {
  position: absolute;
  top: 12px;
  right: 12px;
  display: flex;
  align-items: center;
  gap: 4px;
  padding: 4px 10px;
  background: linear-gradient(135deg, #f39c12, #e67e22);
  color: #000;
  font-size: 12px;
  font-weight: 600;
  border-radius: 12px;
  z-index: 2;
}

.play-overlay {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%) scale(0);
  display: flex;
  align-items: center;
  justify-content: center;
  width: 64px;
  height: 64px;
  background: rgba(233, 69, 96, 0.9);
  border-radius: 50%;
  color: #fff;
  transition: all 0.3s ease;
  z-index: 2;
}

.movie-card:hover .play-overlay {
  transform: translate(-50%, -50%) scale(1);
}

.card-info {
  padding: 16px;
}

.movie-title {
  font-size: 16px;
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
  gap: 10px;
  margin-bottom: 6px;
}

.rating {
  display: flex;
  align-items: center;
  gap: 4px;
  color: #f39c12;
  font-size: 13px;
  font-weight: 500;
}

.type {
  color: rgba(255, 255, 255, 0.6);
  font-size: 12px;
}

.movie-region {
  color: rgba(255, 255, 255, 0.4);
  font-size: 12px;
}
</style>