import { createRouter, createWebHistory } from 'vue-router'
import AuthView from '../views/AuthView.vue'
import LoginForm from '../components/auth/LoginForm.vue'
import RegisterForm from '../components/auth/RegisterForm.vue'
import PasswordResetSwitch from '../components/auth/PasswordResetSwitch.vue'
import ProjectsView from '../views/ProjectsView.vue'
import ScanDetailView from '../views/ScanDetailView.vue'
import NotFoundView from '../views/NotFoundView.vue'
import ScanHistoryView from '../views/ScanHistoryView.vue'
import LegalView from '../views/LegalView.vue'
import ProjectDetailView from '../views/ProjectDetailView.vue'
import AccountView from '../views/AccountView.vue'
import LandingView from '../views/LandingView.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    { path: '/', component: LandingView },
    {
      path: '/login',
      component: AuthView,
      meta: { layout: 'auth' },
      children: [{ path: '', component: LoginForm }],
    },
    {
      path: '/register',
      component: AuthView,
      meta: { layout: 'auth' },
      children: [{ path: '', component: RegisterForm }],
    },
    {
      path: '/password-reset',
      component: AuthView,
      meta: { layout: 'auth' },
      children: [{ path: '', component: PasswordResetSwitch }],
    },
    { path: '/datenschutz', component: LegalView, meta: { layout: 'public' } },
    { path: '/impressum', component: LegalView, meta: { layout: 'public' } },
    {
      path: '/projects/:id/scans',
      component: ScanHistoryView,
      meta: { requiresAuth: true, layout: 'default' },
    },
    {
      path: '/projects/:id',
      component: ProjectDetailView,
      meta: { requiresAuth: true, layout: 'default' },
    },
    { path: '/account', component: AccountView, meta: { requiresAuth: true, layout: 'default' } },
    { path: '/projects', component: ProjectsView, meta: { requiresAuth: true, layout: 'default' } },
    { path: '/scans/:id', component: ScanDetailView, meta: { requiresAuth: true, layout: 'default' } },
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
