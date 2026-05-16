import { createRouter, createWebHistory } from 'vue-router'
import LoginView from '../views/LoginView.vue'
import ProjectsView from '../views/ProjectsView.vue'
import ScanDetailView from '../views/ScanDetailView.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    { path: '/', redirect: '/login' },
    { path: '/login', component: LoginView },
    { path: '/projects', component: ProjectsView },
    { path: '/scans/:id', component: ScanDetailView },
  ],
})

export default router
