import React, { useState } from 'react';
import {
  Table,
  Tag,
  Button,
  Space,
  Select,
  DatePicker,
  Row,
  Col,
  Typography,
  Modal,
  Descriptions
} from 'antd';
import {
  CheckCircleOutlined,
  ExclamationCircleOutlined,
  ArrowLeftOutlined
} from '@ant-design/icons';
import type { ColumnsType } from 'antd/es/table';
import { useNavigate } from 'react-router-dom';
import { useAlertStore } from '@/stores/alertStore';
import type { AlertEventVO } from '@/api/types';
import AppLayout from '@/components/Layout';

const { RangePicker } = DatePicker;
const { Title } = Typography;

const priorityConfig: Record<string, { color: string; label: string }> = {
  HIGH: { color: 'red', label: '高' },
  MEDIUM: { color: 'orange', label: '中' },
  LOW: { color: 'green', label: '低' }
};

const statusConfig: Record<string, { color: string; label: string }> = {
  PENDING: { color: 'red', label: '待确认' },
  CONFIRMED: { color: 'blue', label: '已确认' },
  RESOLVED: { color: 'green', label: '已解决' },
  DISMISSED: { color: 'default', label: '已忽略' }
};

const AlertCenter: React.FC = () => {
  const navigate = useNavigate();
  const alerts = useAlertStore((s) => s.alerts);
  const confirmAlert = useAlertStore((s) => s.confirmAlert);
  const batchConfirm = useAlertStore((s) => s.batchConfirm);
  const [selectedRowKeys, setSelectedRowKeys] = useState<React.Key[]>([]);
  const [detailVisible, setDetailVisible] = useState(false);
  const [currentAlert, setCurrentAlert] = useState<AlertEventVO | null>(null);

  const columns: ColumnsType<AlertEventVO> = [
    {
      title: '告警标题',
      dataIndex: 'ruleName',
      key: 'ruleName',
      render: (text, record) => (
        <a onClick={() => { setCurrentAlert(record); setDetailVisible(true); }}>
          {text}
        </a>
      )
    },
    {
      title: '优先级',
      dataIndex: 'priority',
      key: 'priority',
      width: 80,
      render: (p: string) => {
        const cfg = priorityConfig[p] || { color: 'default', label: p };
        return <Tag color={cfg.color}>{cfg.label}</Tag>;
      },
      filters: [
        { text: '高', value: 'HIGH' },
        { text: '中', value: 'MEDIUM' },
        { text: '低', value: 'LOW' }
      ],
      onFilter: (value, record) => record.priority === value
    },
    { title: '资源类型', dataIndex: 'resourceType', key: 'resourceType', width: 100 },
    { title: '资源名称', dataIndex: 'resourceName', key: 'resourceName', width: 120 },
    {
      title: '状态',
      dataIndex: 'status',
      key: 'status',
      width: 90,
      render: (s: string) => {
        const cfg = statusConfig[s] || { color: 'default', label: s };
        return <Tag color={cfg.color}>{cfg.label}</Tag>;
      },
      filters: [
        { text: '待确认', value: 'PENDING' },
        { text: '已确认', value: 'CONFIRMED' },
        { text: '已解决', value: 'RESOLVED' },
        { text: '已忽略', value: 'DISMISSED' }
      ],
      onFilter: (value, record) => record.status === value
    },
    { title: '触发值', dataIndex: 'triggerValue', key: 'triggerValue', width: 80 },
    { title: '阈值', dataIndex: 'threshold', key: 'threshold', width: 80 },
    { title: '时间', dataIndex: 'createdAt', key: 'createdAt', width: 170 },
    {
      title: '操作',
      key: 'action',
      width: 120,
      render: (_, record) => (
        <Space>
          <Button
            type="link"
            size="small"
            icon={<CheckCircleOutlined />}
            onClick={() => confirmAlert(record.id)}
          >
            确认
          </Button>
        </Space>
      )
    }
  ];

  return (
    <AppLayout showSider={false}>
      <div style={{ padding: 24, height: '100%', overflow: 'auto' }}>
        <Row justify="space-between" align="middle" style={{ marginBottom: 16 }}>
          <Col>
            <Space>
              <Button
                type="text"
                icon={<ArrowLeftOutlined />}
                onClick={() => navigate('/')}
              />
              <Title level={4} style={{ margin: 0 }}>告警中心</Title>
            </Space>
          </Col>
          <Col>
            <Space>
              <Select
                placeholder="优先级"
                allowClear
                style={{ width: 120 }}
                options={[
                  { value: 'HIGH', label: '高' },
                  { value: 'MEDIUM', label: '中' },
                  { value: 'LOW', label: '低' }
                ]}
              />
              <Select
                placeholder="状态"
                allowClear
                style={{ width: 120 }}
                options={[
                  { value: 'PENDING', label: '待确认' },
                  { value: 'CONFIRMED', label: '已确认' },
                  { value: 'RESOLVED', label: '已解决' }
                ]}
              />
              <RangePicker />
              <Button
                type="primary"
                disabled={selectedRowKeys.length === 0}
                onClick={() => {
                  batchConfirm(selectedRowKeys as string[]);
                  setSelectedRowKeys([]);
                }}
              >
                批量确认
              </Button>
            </Space>
          </Col>
        </Row>

        <Table
          rowKey="id"
          columns={columns}
          dataSource={alerts}
          rowSelection={{
            selectedRowKeys,
            onChange: setSelectedRowKeys
          }}
          pagination={{ pageSize: 15, showSizeChanger: true, showTotal: (t) => 共  条 }}
          size="middle"
          locale={{ emptyText: '暂无告警事件' }}
        />

        <Modal
          title="告警详情"
          open={detailVisible}
          onCancel={() => setDetailVisible(false)}
          footer={
            <Space>
              <Button onClick={() => setDetailVisible(false)}>关闭</Button>
              {currentAlert?.status === 'PENDING' && (
                <Button
                  type="primary"
                  onClick={() => {
                    if (currentAlert) confirmAlert(currentAlert.id);
                    setDetailVisible(false);
                  }}
                >
                  确认告警
                </Button>
              )}
            </Space>
          }
          width={600}
        >
          {currentAlert && (
            <Descriptions column={2} bordered size="small">
              <Descriptions.Item label="告警规则" span={2}>{currentAlert.ruleName}</Descriptions.Item>
              <Descriptions.Item label="资源名称">{currentAlert.resourceName}</Descriptions.Item>
              <Descriptions.Item label="资源类型">{currentAlert.resourceType}</Descriptions.Item>
              <Descriptions.Item label="优先级">
                <Tag color={priorityConfig[currentAlert.priority]?.color}>
                  {priorityConfig[currentAlert.priority]?.label}
                </Tag>
              </Descriptions.Item>
              <Descriptions.Item label="状态">
                <Tag color={statusConfig[currentAlert.status]?.color}>
                  {statusConfig[currentAlert.status]?.label}
                </Tag>
              </Descriptions.Item>
              <Descriptions.Item label="触发值">{currentAlert.triggerValue}</Descriptions.Item>
              <Descriptions.Item label="阈值">{currentAlert.threshold}</Descriptions.Item>
              <Descriptions.Item label="创建时间" span={2}>{currentAlert.createdAt}</Descriptions.Item>
            </Descriptions>
          )}
        </Modal>
      </div>
    </AppLayout>
  );
};

export default AlertCenter;
