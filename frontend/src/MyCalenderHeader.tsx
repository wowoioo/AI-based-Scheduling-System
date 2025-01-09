import React, { useEffect, useState } from "react";
import { Button } from "@/components/ui/button";
import { Label } from "@/components/ui/label";
import { ChevronLeft, ChevronRight } from "lucide-react";
import { getAllTeachers } from "./api";
import { DatePicker } from "./components/date-picker";
import { Tabs, TabsList, TabsTrigger } from "./components/ui/tabs";
import { MultiSelect } from "./components/multi-select";
import { CalendarApi } from "@fullcalendar/core/index.js";

const viewOptions = [
  { label: "Month", value: "dayGridMonth" },
  { label: "Week", value: "timeGridWeek" },
  { label: "Day", value: "timeGridDay" },
];

const Header: React.FC<{
  calendarApi: CalendarApi;
  setTeachers: React.Dispatch<React.SetStateAction<string[]>>;
}> = ({ calendarApi, setTeachers }) => {
  const [radioValue, setRadioValue] = useState("dayGridMonth");
  const [weekend, setWeekend] = useState(true);
  const [selectedTeachers, setSelectedTeachers] = useState<string[]>([]);
  const [selectOptions, setSelectOptions] = useState<{ label: string; value: string }[]>([]);

  useEffect(() => {
    const fetchTeachers = async () => {
      const data: string[] = await getAllTeachers();
      const result = data.map((item: string) => ({ label: item, value: item }));
      setSelectOptions(result);
    };
    fetchTeachers();
  }, []);

  const changeTime = (date: Date | undefined) => {
    if (date) {
      calendarApi.gotoDate(date);
    }
  };

  const changeView = (value: string) => {
    calendarApi.changeView(value);
    setRadioValue(value);
  };

  const changeWeekendsVisible = () => {
    setWeekend(!weekend);
    calendarApi.setOption("weekends", !weekend);
  };

  const selectTeachers = (value: string[]) => {
    setSelectedTeachers(value);
    setTeachers(value);
  };

  useEffect(() => {
    if (calendarApi) {
      calendarApi.gotoDate(new Date(2024, 2, 1));
    }
  }, [calendarApi]);

  return (
    <div className="space-y-4">
      <div className="flex items-center gap-4">
        <Label className="min-w-[120px]">Select lecturers:</Label>
        <MultiSelect
          options={selectOptions}
          value={selectedTeachers}
          onValueChange={selectTeachers}
          placeholder="Please select lecturers"
          className="w-full bg-white"
        />
      </div>

      <div className={`flex flex-col lg:flex-row justify-between items-center gap-4 py-5`}>
        <div className="flex items-center gap-4">
          <Button variant="default" onClick={() => calendarApi?.today()}>
            Today
          </Button>
          <DatePicker
            onChange={changeTime}
            // className="border rounded-md"
          />
        </div>

        <div className="flex items-center gap-4">
          <Button variant="outline" size="icon" onClick={() => calendarApi?.prev()}>
            <ChevronLeft className="w-4 h-4" />
          </Button>
          <h1 className="min-w-[240px] text-center">{calendarApi?.view.title}</h1>
          <Button variant="outline" size="icon" onClick={() => calendarApi?.next()}>
            <ChevronRight className="w-4 h-4" />
          </Button>
        </div>

        <div className="flex items-center gap-4">
          <Button variant={weekend ? "default" : "outline"} onClick={changeWeekendsVisible}>
            Weekends
          </Button>
          <Tabs value={radioValue} onValueChange={changeView} className="flex gap-2">
            <TabsList>
              {viewOptions.map((option) => (
                <TabsTrigger key={option.value} value={option.value} onClick={() => changeView(option.value)}>
                  {option.label}
                </TabsTrigger>
              ))}
            </TabsList>
          </Tabs>
        </div>
      </div>
    </div>
  );
};

export default Header;
