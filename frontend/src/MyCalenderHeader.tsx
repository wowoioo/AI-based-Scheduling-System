import React, { useEffect, useState } from "react";
import { Button, DatePicker, DatePickerProps, Radio, RadioChangeEvent, Select, SelectProps } from "antd";
import { LeftOutlined, RightOutlined } from "@ant-design/icons";
import { useViewport } from "./ViewportContext";
import { getAllTeachers } from "./api";

const viewOptions = [
  { label: "month", value: "dayGridMonth" },
  { label: "week", value: "timeGridWeek" },
  { label: "day", value: "timeGridDay" },
];

const Header: React.FC<{ calendarApi: any; setTeachers: React.Dispatch<React.SetStateAction<any>> }> = ({
  calendarApi,
  setTeachers,
}) => {
  const [radioValue, setRadioValue] = useState("dayGridMonth");
  const [weekend, setWeekend] = useState(true);
  const [selectOptions, setSelectOptions] = useState<SelectProps["options"]>([]);

  const viewport = useViewport();
  if (!viewport) return null;
  const { width } = viewport;

  useEffect(() => {
    const fetchTeachers = async () => {
      const data: string[] = await getAllTeachers();
      const result = data.map((item: string) => ({ label: item, value: item }));
      setSelectOptions(result);
    };
    fetchTeachers();
  }, []);

  const changeTime: DatePickerProps["onChange"] = (date) => {
    let time = date.toDate();
    calendarApi.gotoDate(time);
  };

  const changeView = ({ target: { value } }: RadioChangeEvent) => {
    calendarApi.changeView(value);
    setRadioValue(value);
  };

  const changeWeekendsVisible = () => {
    setWeekend(!weekend);
    calendarApi.setOption("weekends", !weekend);
  };

  const selectTeachers = (value: string[]) => {
    setTeachers(value);
  };

  useEffect(() => {
    if (calendarApi) {
      calendarApi.gotoDate(new Date(2023, 2, 1));
    }
  }, [calendarApi]);

  return (
    <>
      <div style={{ display: "flex", alignItems: "center", padding: "10px 0px" }}>
        <span style={{ marginRight: "10px", minWidth: "120px" }}>Select lecturers:</span>
        <Select
          mode="multiple"
          allowClear
          style={{ width: "100%" }}
          placeholder="Please select lecturers"
          onChange={selectTeachers}
          options={selectOptions}
        />
      </div>
      <div
        style={{
          display: "flex",
          justifyContent: "space-between",
          alignItems: "center",
          flexDirection: width > 1200 ? "row" : "column",
        }}
      >
        <div style={{ display: "flex", alignItems: "center", gap: "10px" }}>
          <Button type="primary" onClick={() => calendarApi?.today()}>
            Today
          </Button>
          <DatePicker onChange={changeTime} />
        </div>
        <div style={{ display: "flex", alignItems: "center", gap: "10px" }}>
          <Button type="primary" shape="circle" icon={<LeftOutlined />} onClick={() => calendarApi?.prev()} />
          <h1 style={{ minWidth: "300px", textAlign: "center" }}>{calendarApi?.view.title}</h1>
          <Button type="primary" shape="circle" icon={<RightOutlined />} onClick={() => calendarApi?.next()} />
        </div>
        <div style={{ display: "flex", gap: "10px", margin: "12px 0px" }}>
          <Button type={weekend ? "primary" : "default"} onClick={changeWeekendsVisible}>
            weekends
          </Button>
          <Radio.Group
            options={viewOptions}
            optionType="button"
            buttonStyle="solid"
            onChange={changeView}
            value={radioValue}
          />
        </div>
      </div>
    </>
  );
};

export default Header;
