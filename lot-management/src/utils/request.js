import axios from 'axios'
import { Message } from 'element-ui'

// const service = axios.create({
//   baseURL: process.env.VUE_APP_BASE_API, // url = base url + request url
//   timeout: 5000 // request timeout
// })

const service = axios.create({
  baseURL: 'http://localhost:8089', // url = base url + request url
  timeout: 5000 // request timeout
})

// request interceptor
service.interceptors.request.use(
  config => {
    return config
  },
  error => {
    console.log(error) // for debug
    return Promise.reject(error)
  }
)

service.interceptors.response.use(
  response => {
    const res = response.data
      return res
  },
  error => {
    console.log('err' + error) // for debug
    Message({
      message: error.message,
      type: 'error',
      duration: 5 * 1000
    })
    return Promise.reject(error)
  }
)

export default service
