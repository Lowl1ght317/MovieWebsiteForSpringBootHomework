<template>
  <header class="header">
    <div class="header-container">
      <div class="logo" @click="$router.push('/')">
        <Film class="logo-icon" :size="28" />
        <span class="logo-text">电影天堂</span>
      </div>
      
      <nav class="nav">
        <router-link to="/" class="nav-link" :class="{ active: $route.path === '/' }">
          <Flame :size="18" />
          <span>热播排行</span>
        </router-link>
        <router-link to="/category" class="nav-link" :class="{ active: $route.path === '/category' }">
          <Grid3X3 :size="18" />
          <span>电影分类</span>
        </router-link>
        <router-link to="/region" class="nav-link" :class="{ active: $route.path === '/region' }">
          <Globe :size="18" />
          <span>地区筛选</span>
        </router-link>
        <router-link to="/rank" class="nav-link" :class="{ active: $route.path === '/rank' }">
          <Trophy :size="18" />
          <span>排行榜</span>
        </router-link>
        <router-link to="/creator" class="nav-link" :class="{ active: $route.path === '/creator' }">
          <Users :size="18" />
          <span>主创作品</span>
        </router-link>
        <router-link to="/charts" class="nav-link" :class="{ active: $route.path === '/charts' }">
          <BarChart3 :size="18" />
          <span>数据图表</span>
        </router-link>
        <router-link to="/export" class="nav-link" :class="{ active: $route.path === '/export' }">
          <Download :size="18" />
          <span>导出报表</span>
        </router-link>
        <router-link to="/ai" class="nav-link" :class="{ active: $route.path === '/ai' }">
          <Bot :size="18" />
          <span>AI推荐</span>
        </router-link>
      </nav>

      <div class="auth-section">
        <div v-if="store.isLoggedIn" class="user-info">
          <span class="login-status">
            <User :size="16" />
            已登录
          </span>
          <button class="logout-btn" @click="handleLogout">
            <LogOut :size="16" />
            退出
          </button>
        </div>
        <div v-else class="auth-buttons">
          <button class="login-btn" @click="showLoginModal = true">
            <LogIn :size="16" />
            登录
          </button>
          <button class="register-btn" @click="showRegisterModal = true">
            <UserPlus :size="16" />
            注册
          </button>
        </div>
      </div>
    </div>

    <LoginModal v-if="showLoginModal" @close="showLoginModal = false" @login="handleLoginSuccess" />
    <RegisterModal v-if="showRegisterModal" @close="showRegisterModal = false" @register="handleRegisterSuccess" />
  </header>
</template>

<script setup>
import { ref } from 'vue'
import { Film, Flame, Grid3X3, Globe, Trophy, Users, BarChart3, Download, Bot, User, LogOut, LogIn, UserPlus } from 'lucide-vue-next'
import store, { login, logout } from '../store'
import LoginModal from './LoginModal.vue'
import RegisterModal from './RegisterModal.vue'

const showLoginModal = ref(false)
const showRegisterModal = ref(false)

function handleLoginSuccess(data) {
  login(data.token, data.user.id)
  showLoginModal.value = false
}

function handleRegisterSuccess(data) {
  login(data.token, data.user.id)
  showRegisterModal.value = false
}

function handleLogout() {
  logout()
}
</script>

<style scoped>
.header {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  z-index: 1000;
  background: rgba(15, 15, 35, 0.95);
  backdrop-filter: blur(20px);
  border-bottom: 1px solid rgba(255, 255, 255, 0.1);
}

.header-container {
  max-width: 1400px;
  margin: 0 auto;
  padding: 0 24px;
  height: 70px;
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.logo {
  display: flex;
  align-items: center;
  gap: 10px;
  cursor: pointer;
  transition: transform 0.3s ease;
}

.logo:hover {
  transform: scale(1.05);
}

.logo-icon {
  color: #e94560;
}

.logo-text {
  font-size: 22px;
  font-weight: 700;
  background: linear-gradient(135deg, #e94560, #ff6b6b);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.nav {
  display: flex;
  gap: 4px;
}

.nav-link {
  display: flex;
  align-items: center;
  gap: 6px;
  padding: 10px 16px;
  color: rgba(255, 255, 255, 0.7);
  text-decoration: none;
  border-radius: 20px;
  transition: all 0.3s ease;
  font-size: 14px;
}

.nav-link:hover {
  color: #fff;
  background: rgba(255, 255, 255, 0.1);
}

.nav-link.active {
  color: #fff;
  background: linear-gradient(135deg, #e94560, #ff6b6b);
}

.auth-section {
  display: flex;
  align-items: center;
  gap: 12px;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 12px;
}

.login-status {
  display: flex;
  align-items: center;
  gap: 6px;
  padding: 8px 14px;
  background: rgba(46, 204, 113, 0.15);
  color: #2ecc71;
  border-radius: 20px;
  font-size: 14px;
}

.logout-btn {
  display: flex;
  align-items: center;
  gap: 6px;
  padding: 8px 16px;
  background: rgba(255, 255, 255, 0.1);
  color: #fff;
  border: none;
  border-radius: 20px;
  cursor: pointer;
  transition: all 0.3s ease;
  font-size: 14px;
}

.logout-btn:hover {
  background: rgba(233, 69, 96, 0.3);
  color: #e94560;
}

.auth-buttons {
  display: flex;
  gap: 8px;
}

.login-btn, .register-btn {
  display: flex;
  align-items: center;
  gap: 6px;
  padding: 8px 16px;
  border: none;
  border-radius: 20px;
  cursor: pointer;
  transition: all 0.3s ease;
  font-size: 14px;
}

.login-btn {
  background: rgba(255, 255, 255, 0.1);
  color: #fff;
}

.login-btn:hover {
  background: rgba(255, 255, 255, 0.2);
}

.register-btn {
  background: linear-gradient(135deg, #e94560, #ff6b6b);
  color: #fff;
}

.register-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 15px rgba(233, 69, 96, 0.4);
}
</style>