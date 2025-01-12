import React, { useRef, useState } from "react";
import { Button, Drawer, message, Space, Upload } from "antd";
import { UploadOutlined } from "@ant-design/icons";
import type { UploadProps } from "antd";
import { ROOT_PATH } from "./api";
import ClassroomTable from "./ClassroomTable";

const UploadExcel: React.FC = () => {
  const props: UploadProps = {
    accept: ".xlsx",
    action: `${ROOT_PATH}/upload`,
    name: "file",
    async onChange(info) {
      if (info.file.status === "done") {
        message.success(`${info.file.name} file uploaded successfully`);
      } else if (info.file.status === "error") {
        message.error(`${info.file.name} file upload failed.`);
      }
    },
  };
  return (
    <Upload {...props}>
      <Button icon={<UploadOutlined />}>Click to Upload Course</Button>
    </Upload>
  );
};

const UploadPage: React.FC = () => {
  const [open, setOpen] = useState(false);
  const classroomTableRef = useRef<{ add: () => void } | null>(null);

  const handleAddRow = () => {
    if (classroomTableRef.current) {
      classroomTableRef.current.add();
    }
  };

  const showDrawer = () => {
    setOpen(true);
  };

  const onClose = () => {
    setOpen(false);
  };

  return (
    <>
      <Button type="primary" onClick={showDrawer}>
        Input Course Data
      </Button>
      <Drawer title="Input Course Data" onClose={onClose} open={open} size="large">
        <Space style={{ width: "100%" }}>
          <UploadExcel />{" "}
          <Button onClick={handleAddRow} type="primary" style={{ marginBottom: 16 }}>
            Add a row
          </Button>
        </Space>
        <ClassroomTable ref={classroomTableRef} />
      </Drawer>
    </>
  );
};

export default UploadPage;
