import React from "react";
import {UploadOutlined} from "@ant-design/icons";
import type {UploadProps} from "antd";
import {Button, message, Upload} from "antd";
import MyCalendar from "./MyCalender";
import {ROOT_PATH} from "./api";
import {useViewport} from "./ViewportContext";

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
      <Button icon={<UploadOutlined />}>Click to Upload</Button>
    </Upload>
  );
};

const App = () => {
  const viewport = useViewport();
  if (!viewport) return null;

  return (
    <div style={{ padding: viewport.width > 660 ? "0px 24px" : "0" }}>
      <h1>AI-based Scheduling System</h1>
      <UploadExcel />
      <MyCalendar />
    </div>
  );
};

export default App;
