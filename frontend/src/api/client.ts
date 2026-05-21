import axios from 'axios';
import type { ApiResult } from './types';
import { message } from 'antd';

const apiClient = axios.create({
  baseURL: '/api',
  timeout: 30000,
  headers: { 'Content-Type': 'application/json' }
});

apiClient.interceptors.request.use((config) => {
  const token = localStorage.getItem('token');
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

apiClient.interceptors.response.use(
  (response) => {
    const result: ApiResult<any> = response.data;
    if (result.code !== 200) {
      message.error(result.message || '请求失败');
      return Promise.reject(new Error(result.message));
    }
    return result.data;
  },
  (error) => {
    const msg = error.response?.data?.message || error.message || '网络错误';
    message.error(msg);
    return Promise.reject(error);
  }
);

export default apiClient;
