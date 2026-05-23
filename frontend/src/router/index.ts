import { createRouter, createWebHistory } from 'vue-router'
import LoginView from '../views/LoginView.vue'
import RegisterView from '../views/RegisterView.vue'
import ProjectsView from '../views/ProjectsView.vue'
import Home from '../views/Home.vue'
import ScanDetail from '../components/scan/ScanDetail.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    { path: '/', redirect: '/login' },
    { path: '/login', component: LoginView },
    { path: '/register', component: RegisterView },
    { path: '/home', component: Home, meta: { requiresAuth: true } },
    { path: '/projects', component: ProjectsView, meta: { requiresAuth: true } },
    { path: '/scans/:id', component: ScanDetail, meta: { requiresAuth: true } },
  ],
})

router.beforeEach((to) => {
  const token = localStorage.getItem('a11yforge_token')
  if (to.meta.requiresAuth && !token) {
    return '/login'
  }
})

export default router
