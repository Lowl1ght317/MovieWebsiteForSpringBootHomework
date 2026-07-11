import axios from 'axios'

const instance = axios.create({
  baseURL: '/api',
  timeout: 10000
})

instance.interceptors.request.use(
  config => {
    const token = localStorage.getItem('token')
    if (token) {
      config.headers.Authorization = `Bearer ${token}`
    }
    return config
  },
  error => Promise.reject(error)
)

instance.interceptors.response.use(
  response => response,
  error => {
    if (error.response?.status === 401) {
      localStorage.removeItem('token')
      localStorage.removeItem('userId')
      window.location.reload()
    }
    return Promise.reject(error)
  }
)

export const userApi = {
  login: data => instance.post('/user/login', data),
  register: data => instance.post('/user/register', data),
  getUserInfo: () => instance.get('/user/info')
}

export const movieApi = {
  getHotMovies: () => instance.get('/movie/hot'),
  getMoviesByType: type => instance.get(`/movie/type/${type}`),
  getMoviesByRegion: region => instance.get(`/movie/region/${region}`),
  getRank: type => instance.get(`/movie/rank/${type}`),
  getAllTypes: () => instance.get('/movie/types'),
  getAllRegions: () => instance.get('/movie/regions'),
  searchMovies: keyword => instance.get(`/movie/search?keyword=${keyword}`),
  getMovieById: id => instance.get(`/movie/${id}`),
  playMovie: movieId => instance.post(`/movie/play/${movieId}`)
}

export const creatorApi = {
  searchActor: name => instance.get(`/creator/actor/search?name=${name}`),
  getActorMovies: id => instance.get(`/creator/actor/${id}`),
  searchDirector: name => instance.get(`/creator/director/search?name=${name}`),
  getDirectorMovies: id => instance.get(`/creator/director/${id}`)
}

export const aiApi = {
  chat: msg => instance.get(`/ai/chat?msg=${msg}`),
  recommend: params => instance.get('/ai/recommend', { params }),
  generateReview: movieName => instance.get(`/ai/review?movieName=${movieName}`),
  search: query => instance.get(`/ai/search?query=${query}`)
}

export const reportApi = {
  getStats: () => instance.get('/report/stats'),
  exportReport: (startDate, endDate) => ({
    url: `/api/report/export?startDate=${startDate}&endDate=${endDate}`
  }),
  exportAllMovies: () => ({
    url: '/api/report/export-all'
  })
}

export default instance