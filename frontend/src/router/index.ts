import { createRouter, createWebHistory } from 'vue-router'
import LoginView from '../views/LoginView.vue'
import RegisterView from '../views/RegisterView.vue'
import ProjectsView from '../views/ProjectsView.vue'
import Home from '../views/Home.vue'
import ScanDetail from '../components/scan/ScanDetail.vue'
import PasswordResetView from '../views/PasswordResetView.vue'
import NotFoundView from '../views/NotFoundView.vue'
import ScanHistoryView from '../views/ScanHistoryView.vue'
import LegalView from '../views/LegalView.vue'
import ProjectDetailView from '../views/ProjectDetailView.vue'
import AccountView from '../views/AccountView.vue'
import LandingView from '../views/LandingView.vue'


const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
   // { path: '/', redirect: '/login' },
    { path: '/', component: LandingView },
    { path: '/login', component: LoginView },
    { path: '/register', component: RegisterView },
    { path: '/password-reset', component: PasswordResetView},
    { path: '/datenschutz', component: LegalView },
    { path: '/impressum', component: LegalView },
    { path: '/projects/:id/scans', component: ScanHistoryView, meta: { requiresAuth: true } },
    { path: '/projects/:id', component: ProjectDetailView, meta: { requiresAuth: true } },
    { path: '/account', component: AccountView, meta: { requiresAuth: true } },
    { path: '/home', component: Home, meta: { requiresAuth: true } },
    { path: '/projects', component: ProjectsView, meta: { requiresAuth: true } },
    { path: '/scans/:id', component: ScanDetail, meta: { requiresAuth: true } },
    { path: '/:pathMatch(.*)*', component: NotFoundView },
  ],
})

router.beforeEach((to) => {
  const token = localStorage.getItem('a11yforge_token')
  if (to.meta.requiresAuth && !token) {
    return '/login'
  }
})

export default router
