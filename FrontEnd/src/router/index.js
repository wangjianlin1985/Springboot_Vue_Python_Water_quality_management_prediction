import Vue from 'vue'
import Router from 'vue-router'
import Home from '@/components/Home'
import WaterManagement from '@/components/WaterManagement'
import RecentCollection from '@/components/RecentCollection'
import WaterTrend from '@/components/WaterTrend'
import WaterPrediction from '@/components/WaterPrediction'
import ModelTraining from '@/components/ModelTraining'
import Login from '@/components/Login'
import UserManagement from '@/components/UserManagement'

Vue.use(Router)

export default new Router({
  routes: [
   {
      path: '/',
      name: '登录',
      component: Login
   },
   {
      path: '/home',
      name: '主页',
      component: Home,
      children: [
        {
          path: '/recent',
          name: '近日收集',
          component: RecentCollection
        },
        {
          path: '/manage',
          name: '历史数据',
          component: WaterManagement
        },
        {
          path: '/trend',
          name: '数据走势',
          component: WaterTrend
        },
        {
          path: '/predict',
          name: '下月预测',
          component: WaterPrediction
        },
        {
          path: '/train',
          name: '模型更新',
          component: ModelTraining
        },
        {
          path: '/user',
          name: '用户管理',
          component: UserManagement
        }
      ]
   }
  ]
})
