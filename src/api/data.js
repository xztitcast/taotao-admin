import axios from '@/libs/api.request'

export const uploadImg = formData => {
  return axios.request({
    url: 'image/upload',
    data: formData
  })
}
