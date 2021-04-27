import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router)

import Layout from '@/layout'

export const constantRoutes = [
  {
    path: '/login',
    component: () => import('@/views/login/index'),
    hidden: true
  },

  {
    path: '/404',
    component: () => import('@/views/404'),
    hidden: true
  },

  {
    path: '/',
    component: Layout,
    redirect: '/dashboard',
    children: [{
      path: 'dashboard',
      name: '面板',
      component: () => import('@/views/dashboard/index'),
      meta: { title: '面板', icon: 'dashboard' }
    }]
  },
  {
    path: '/adddevice',
    component: Layout,
    children: [
      {
        path: 'adddevice',
        name: 'adddevice',
        component: () => import('@/views/adddevice/index'),
        meta: { title: '添加设备', icon: 'form' }
      }
    ]
  },
  {
    path: '/device',
    component: Layout,
    children: [
      {
        path: 'index',
        name: 'device',
        component: () => import('@/views/device/index'),
        meta: { title: '所有设备', icon: 'form' }
      }
    ]
  },
  {
    path: '/connect',
    component: Layout,
    children: [
      {
        path: 'index',
        name: 'connect',
        component: () => import('@/views/connect/index'),
        meta: { title: '连接管理', icon: 'form' }
      }
    ]
  },
  {
    path: '/data',
    component: Layout,
    hidden: true,
    children: [
      {
        path: 'index',
        name: 'data',
        component: () => import('@/views/data/index'),
        meta: { title: '设备数据', icon: 'form' }
      }
    ]
  },


  // 404 page must be placed at the end !!!
  { path: '*', redirect: '/404', hidden: true }
]

const createRouter = () => new Router({
  // mode: 'history', // require service support
  scrollBehavior: () => ({ y: 0 }),
  routes: constantRoutes
})

const router = createRouter()

// Detail see: https://github.com/vuejs/vue-router/issues/1234#issuecomment-357941465
export function resetRouter() {
  const newRouter = createRouter()
  router.matcher = newRouter.matcher // reset router
}

export default router
