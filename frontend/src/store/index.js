import { reactive } from 'vue'

const state = reactive({
  token: localStorage.getItem('token') || '',
  userId: localStorage.getItem('userId') || '',
  isLoggedIn: !!localStorage.getItem('token'),
  currentUser: null
})

export function login(token, userId) {
  state.token = token
  state.userId = userId
  state.isLoggedIn = true
  localStorage.setItem('token', token)
  localStorage.setItem('userId', userId)
}

export function logout() {
  state.token = ''
  state.userId = ''
  state.isLoggedIn = false
  state.currentUser = null
  localStorage.removeItem('token')
  localStorage.removeItem('userId')
}

export function setUserInfo(user) {
  state.currentUser = user
}

export function getUserInfo() {
  return state.currentUser
}

export function isAuthenticated() {
  return state.isLoggedIn
}

export default state