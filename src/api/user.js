import axios from '@/libs/api.request'

export const login = ({ username, password, uuid, captcha }) => {
  const data = {
    username,
    password,
    uuid,
    captcha
  }
  return axios.request({
    url: '/sys/login',
    data,
    method: 'post'
  })
}

export const getUserInfo = (token) => {
  return axios.request({
    url: '/sys/user/info',
    params: {
      token
    },
    method: 'get'
  })
}

export const logout = (token) => {
  return axios.request({
    url: '/sys/logout',
    method: 'post'
  })
}
