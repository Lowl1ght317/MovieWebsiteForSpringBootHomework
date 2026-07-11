import { createRouter, createWebHistory } from 'vue-router'
import Home from '../views/Home.vue'
import MovieCategory from '../views/MovieCategory.vue'
import MovieRegion from '../views/MovieRegion.vue'
import MovieRank from '../views/MovieRank.vue'
import CreatorWorks from '../views/CreatorWorks.vue'
import DataCharts from '../views/DataCharts.vue'
import ReportExport from '../views/ReportExport.vue'
import AIRecommend from '../views/AIRecommend.vue'

const routes = [
  { path: '/', name: 'Home', component: Home },
  { path: '/category', name: 'MovieCategory', component: MovieCategory },
  { path: '/region', name: 'MovieRegion', component: MovieRegion },
  { path: '/rank', name: 'MovieRank', component: MovieRank },
  { path: '/creator', name: 'CreatorWorks', component: CreatorWorks },
  { path: '/charts', name: 'DataCharts', component: DataCharts },
  { path: '/export', name: 'ReportExport', component: ReportExport },
  { path: '/ai', name: 'AIRecommend', component: AIRecommend }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router