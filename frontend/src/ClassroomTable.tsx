import React, { forwardRef, useEffect, useImperativeHandle, useState } from "react";
import { Form, Input, Modal, Popconfirm, Space, Table } from "antd";
import { ClassroomType, deleteClassroom, getClassroom, saveClassroom } from "./api";

const ClassroomTable: React.FC = forwardRef<{ add: () => void }, {}>((_, ref) => {
  const [form] = Form.useForm();
  const [data, setData] = useState<ClassroomType[]>([]);
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [editingRecord, setEditingRecord] = useState<Partial<ClassroomType> | null>(null);

  useImperativeHandle(ref, () => ({
    add,
  }));

  const edit = (record: Partial<ClassroomType>) => {
    setEditingRecord(record);
    setIsModalOpen(true);
  };

  const save = async (values: Partial<ClassroomType>) => {
    if (editingRecord && editingRecord.id) {
      values.id = editingRecord.id;
    }
    await saveClassroom(values as ClassroomType);
    await fetch();
    setIsModalOpen(false);
    setEditingRecord(null);
  };

  const add = () => {
    setEditingRecord(null);
    setIsModalOpen(true);
  };

  const handleDelete = async (key: number) => {
    await deleteClassroom(key);
    await fetch();
  };

  const fetch = async () => {
    let data = await getClassroom();
    setData(data);
  };

  useEffect(() => {
    fetch();
  }, []);

  useEffect(() => {
    if (editingRecord) {
      form.setFieldsValue(editingRecord);
    } else {
      form.resetFields();
    }
  }, [editingRecord, form]);

  const columns = [
    {
      title: "Classroom",
      dataIndex: "name",
      key: "name",
      sorter: (a: ClassroomType, b: ClassroomType) => a.name.localeCompare(b.name),
    },
    {
      title: "Size",
      dataIndex: "size",
      key: "size",
      sorter: (a: ClassroomType, b: ClassroomType) => a.size - b.size,
    },
    {
      title: "Software",
      dataIndex: "software",
      key: "software",
      render: (text: string | null) => (text ? text : "N/A"),
    },
    {
      title: "Action",
      key: "action",
      width: 100,
      render: (_: any, record: ClassroomType) => (
        <Space size="middle">
          <a onClick={() => edit(record)}>Edit</a>
          <Popconfirm title="Sure to delete?" onConfirm={() => handleDelete(record.id)}>
            <a>Delete</a>
          </Popconfirm>
        </Space>
      ),
    },
  ];

  return (
    <>
      <Modal
        title={editingRecord ? "Edit Classroom" : "Create a new classroom"}
        okText={editingRecord ? "Save" : "Create"}
        open={isModalOpen}
        onCancel={() => setIsModalOpen(false)}
        onOk={() => form.submit()}
        destroyOnClose
      >
        <Form layout="vertical" form={form} name="form_in_modal" initialValues={{ modifier: "public" }} onFinish={save}>
          <Form.Item
            name="name"
            label="Classroom"
            rules={[{ required: true, message: "Please input the classroom name!" }]}
          >
            <Input />
          </Form.Item>
          <Form.Item name="size" label="Size" rules={[{ required: true, message: "Please input the size!" }]}>
            <Input type="number" />
          </Form.Item>
          <Form.Item name="software" label="Software">
            <Input />
          </Form.Item>
        </Form>
      </Modal>
      <Table<ClassroomType> columns={columns} dataSource={data} rowKey="id" pagination={{ pageSize: 10 }} />
    </>
  );
});

export default ClassroomTable;
