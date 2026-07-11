<template>
  <div class="ai-page">
    <div class="page-header">
      <h1 class="page-title">
        <Bot :size="32" />
        AI智能助手
      </h1>
      <p class="page-subtitle">基于AI技术为您推荐精彩电影</p>
    </div>

    <div class="content-section">
      <div class="ai-card">
        <div class="card-header">
          <Sparkles :size="24" />
          <h3 class="card-title">电影推荐</h3>
        </div>
        <div class="card-body">
          <div class="input-group">
            <input 
              type="text" 
              v-model="recommendForm.genre" 
              placeholder="电影类型（如：科幻、动作）"
              class="form-input"
            />
            <input 
              type="text" 
              v-model="recommendForm.actor" 
              placeholder="演员名字"
              class="form-input"
            />
            <input 
              type="text" 
              v-model="recommendForm.director" 
              placeholder="导演名字"
              class="form-input"
            />
          </div>
          <button class="action-btn" @click="getRecommend" :disabled="recommendLoading">
            <Loader2 v-if="recommendLoading" :size="18" class="spinner" />
            {{ recommendLoading ? '推荐中...' : '获取推荐' }}
          </button>
        </div>
      </div>

      <div class="ai-card">
        <div class="card-header">
          <PenTool :size="24" />
          <h3 class="card-title">影评生成</h3>
        </div>
        <div class="card-body">
          <input 
            type="text" 
            v-model="reviewMovie" 
            placeholder="输入电影名称"
            class="wide-input"
          />
          <button class="action-btn secondary" @click="generateReview" :disabled="reviewLoading">
            <Loader2 v-if="reviewLoading" :size="18" class="spinner" />
            {{ reviewLoading ? '生成中...' : '生成影评' }}
          </button>
        </div>
      </div>

      <div class="ai-card">
        <div class="card-header">
          <Search :size="24" />
          <h3 class="card-title">智能搜索</h3>
        </div>
        <div class="card-body">
          <input 
            type="text" 
            v-model="searchQuery" 
            placeholder="输入搜索关键词"
            class="wide-input"
          />
          <button class="action-btn secondary" @click="aiSearch" :disabled="searchLoading">
            <Loader2 v-if="searchLoading" :size="18" class="spinner" />
            {{ searchLoading ? '搜索中...' : '智能搜索' }}
          </button>
        </div>
      </div>

      <div class="ai-card chat-card">
        <div class="card-header">
          <MessageCircle :size="24" />
          <h3 class="card-title">AI聊天</h3>
        </div>
        <div class="card-body">
          <div class="chat-messages" ref="chatMessagesRef">
            <div 
              v-for="(msg, index) in chatMessages" 
              :key="index" 
              class="chat-message"
              :class="msg.type"
            >
              <span class="message-avatar">{{ msg.type === 'user' ? '👤' : '🤖' }}</span>
              <span class="message-content">{{ msg.content }}</span>
            </div>
          </div>
          <div class="chat-input-area">
            <input 
              type="text" 
              v-model="chatInput" 
              placeholder="输入您的问题..."
              @keyup.enter="sendChat"
              class="wide-input"
            />
            <button class="action-btn" @click="sendChat" :disabled="chatLoading">
              <Send :size="18" />
            </button>
          </div>
        </div>
      </div>

      <div v-if="aiResult" class="result-card">
        <h3 class="result-title">
          <Info :size="20" />
          AI结果
        </h3>
        <div class="result-content">{{ aiResult }}</div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, nextTick } from 'vue'
import { Bot, Sparkles, PenTool, Search, MessageCircle, Send, Info, Loader2 } from 'lucide-vue-next'
import { aiApi } from '../api'

const recommendForm = reactive({
  genre: '',
  actor: '',
  director: ''
})

const reviewMovie = ref('')
const searchQuery = ref('')
const chatInput = ref('')
const chatMessages = ref([])

const recommendLoading = ref(false)
const reviewLoading = ref(false)
const searchLoading = ref(false)
const chatLoading = ref(false)

const aiResult = ref('')
const chatMessagesRef = ref(null)

async function getRecommend() {
  recommendLoading.value = true
  aiResult.value = ''
  
  try {
    const params = {}
    if (recommendForm.genre) params.genre = recommendForm.genre
    if (recommendForm.actor) params.actor = recommendForm.actor
    if (recommendForm.director) params.director = recommendForm.director
    
    const response = await aiApi.recommend(params)
    if (response.data.success) {
      aiResult.value = response.data.content
    } else {
      alert(response.data.message)
    }
  } catch (error) {
    console.error('AI推荐失败:', error)
    alert('AI推荐失败')
  } finally {
    recommendLoading.value = false
  }
}

async function generateReview() {
  if (!reviewMovie.value.trim()) {
    alert('请输入电影名称')
    return
  }
  
  reviewLoading.value = true
  aiResult.value = ''
  
  try {
    const response = await aiApi.generateReview(reviewMovie.value)
    if (response.data.success) {
      aiResult.value = response.data.content
    } else {
      alert(response.data.message)
    }
  } catch (error) {
    console.error('影评生成失败:', error)
    alert('影评生成失败')
  } finally {
    reviewLoading.value = false
  }
}

async function aiSearch() {
  if (!searchQuery.value.trim()) {
    alert('请输入搜索关键词')
    return
  }
  
  searchLoading.value = true
  aiResult.value = ''
  
  try {
    const response = await aiApi.search(searchQuery.value)
    if (response.data.success) {
      aiResult.value = response.data.content
    } else {
      alert(response.data.message)
    }
  } catch (error) {
    console.error('智能搜索失败:', error)
    alert('智能搜索失败')
  } finally {
    searchLoading.value = false
  }
}

async function sendChat() {
  if (!chatInput.value.trim()) return
  
  const message = chatInput.value.trim()
  chatInput.value = ''
  
  chatMessages.value.push({ type: 'user', content: message })
  chatMessages.value.push({ type: 'assistant', content: '正在思考...' })
  
  await nextTick()
  if (chatMessagesRef.value) {
    chatMessagesRef.value.scrollTop = chatMessagesRef.value.scrollHeight
  }
  
  try {
    const response = await aiApi.chat(message)
    const lastIndex = chatMessages.value.length - 1
    if (response.data.success) {
      chatMessages.value[lastIndex].content = response.data.content
    } else {
      chatMessages.value[lastIndex].content = response.data.message
    }
  } catch (error) {
    const lastIndex = chatMessages.value.length - 1
    chatMessages.value[lastIndex].content = 'AI服务调用失败'
  }
  
  await nextTick()
  if (chatMessagesRef.value) {
    chatMessagesRef.value.scrollTop = chatMessagesRef.value.scrollHeight
  }
}
</script>

<style scoped>
.ai-page {
  min-height: 100vh;
}

.page-header {
  background: linear-gradient(135deg, rgba(99, 102, 241, 0.3) 0%, rgba(15, 15, 35, 0.9) 100%);
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
  max-width: 1000px;
  margin: 0 auto;
  padding: 40px 24px;
  display: flex;
  flex-direction: column;
  gap: 24px;
}

.ai-card {
  background: linear-gradient(135deg, rgba(26, 26, 62, 0.8), rgba(22, 33, 62, 0.8));
  border-radius: 20px;
  border: 1px solid rgba(255, 255, 255, 0.05);
  overflow: hidden;
}

.card-header {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 20px 24px;
  background: rgba(255, 255, 255, 0.03);
  border-bottom: 1px solid rgba(255, 255, 255, 0.05);
}

.card-title {
  font-size: 18px;
  font-weight: 600;
  color: #fff;
}

.card-body {
  padding: 24px;
}

.input-group {
  display: flex;
  gap: 12px;
  margin-bottom: 16px;
}

.form-input {
  flex: 1;
  padding: 12px 16px;
  background: rgba(255, 255, 255, 0.05);
  border: 1px solid rgba(255, 255, 255, 0.1);
  border-radius: 10px;
  color: #fff;
  font-size: 14px;
}

.form-input:focus {
  outline: none;
  border-color: #6366f1;
}

.form-input::placeholder {
  color: rgba(255, 255, 255, 0.4);
}

.wide-input {
  width: 100%;
  padding: 12px 16px;
  background: rgba(255, 255, 255, 0.05);
  border: 1px solid rgba(255, 255, 255, 0.1);
  border-radius: 10px;
  color: #fff;
  font-size: 14px;
  margin-bottom: 16px;
}

.wide-input:focus {
  outline: none;
  border-color: #6366f1;
}

.wide-input::placeholder {
  color: rgba(255, 255, 255, 0.4);
}

.action-btn {
  display: inline-flex;
  align-items: center;
  gap: 8px;
  padding: 12px 28px;
  background: linear-gradient(135deg, #6366f1, #8b5cf6);
  color: #fff;
  border: none;
  border-radius: 25px;
  font-size: 14px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.3s ease;
}

.action-btn:hover:not(:disabled) {
  transform: translateY(-2px);
  box-shadow: 0 4px 15px rgba(99, 102, 241, 0.4);
}

.action-btn:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

.action-btn.secondary {
  background: linear-gradient(135deg, #3498db, #2980b9);
}

.action-btn.secondary:hover:not(:disabled) {
  box-shadow: 0 4px 15px rgba(52, 152, 219, 0.4);
}

.spinner {
  animation: spin 1s linear infinite;
}

@keyframes spin {
  from { transform: rotate(0deg); }
  to { transform: rotate(360deg); }
}

.chat-card {
  max-height: 600px;
}

.chat-messages {
  height: 300px;
  overflow-y: auto;
  margin-bottom: 16px;
  padding-right: 8px;
}

.chat-messages::-webkit-scrollbar {
  width: 6px;
}

.chat-messages::-webkit-scrollbar-track {
  background: rgba(255, 255, 255, 0.05);
  border-radius: 3px;
}

.chat-messages::-webkit-scrollbar-thumb {
  background: rgba(255, 255, 255, 0.2);
  border-radius: 3px;
}

.chat-message {
  display: flex;
  gap: 10px;
  margin-bottom: 12px;
  padding: 12px 16px;
  border-radius: 12px;
  max-width: 80%;
}

.chat-message.user {
  background: rgba(99, 102, 241, 0.2);
  margin-left: auto;
}

.chat-message.assistant {
  background: rgba(255, 255, 255, 0.05);
}

.message-avatar {
  font-size: 18px;
}

.message-content {
  font-size: 14px;
  line-height: 1.6;
}

.chat-input-area {
  display: flex;
  gap: 12px;
}

.chat-input-area .wide-input {
  margin-bottom: 0;
}

.result-card {
  background: linear-gradient(135deg, rgba(99, 102, 241, 0.1), rgba(139, 92, 246, 0.1));
  border: 1px solid rgba(99, 102, 241, 0.3);
  border-radius: 16px;
  padding: 24px;
}

.result-title {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 16px;
  font-weight: 600;
  color: #a5b4fc;
  margin-bottom: 12px;
}

.result-content {
  font-size: 14px;
  line-height: 1.8;
  color: rgba(255, 255, 255, 0.8);
  white-space: pre-wrap;
}

@media (max-width: 768px) {
  .page-title {
    font-size: 28px;
  }
  
  .input-group {
    flex-direction: column;
  }
  
  .chat-message {
    max-width: 95%;
  }
}
</style>