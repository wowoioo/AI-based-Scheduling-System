import React, {memo, useCallback, useEffect, useRef, useState} from "react";
import FullCalendar from "@fullcalendar/react";
import {CalendarApi, EventApi, EventSourceFunc, formatDate} from "@fullcalendar/core";
import dayGridPlugin from "@fullcalendar/daygrid";
import timeGridPlugin from "@fullcalendar/timegrid";
import interactionPlugin from "@fullcalendar/interaction";
import {getCalenderData} from "./api";
import Header from "./MyCalenderHeader";
import {useViewport} from "./ViewportContext";
import {Modal} from "antd";

interface EventDetailsProps {
  selectedEvent: any;
  handleCloseDetails: () => void;
}

const EventDetails: React.FC<EventDetailsProps> = memo(({ selectedEvent, handleCloseDetails }) => {
  const { title, extendedProps, start } = selectedEvent || {};
  const {
    practiceArea,
    courseCode,
    duration,
    software,
    cohort,
    run,
    classroom,
    teacher1,
    teacher2,
    teacher3,
    manager,
    cert,
  } = extendedProps || {};

  const formatProperty = (label: string, value?: string) => (
    <p key={label}>
      <strong>{label}:</strong> {value}
    </p>
  );

  return (
    <Modal
      width={400}
      footer={null}
      title={null}
      open={selectedEvent != null}
      onOk={handleCloseDetails}
      onCancel={handleCloseDetails}
    >
      <h2>{title}</h2>
      {formatProperty("Practice Area", practiceArea)}
      {formatProperty("Course Code", courseCode)}
      {formatProperty("Duration (Days)", duration)}
      {formatProperty("Software", software)}
      {formatProperty("Cohort", cohort)}
      {formatProperty("Run #", run)}
      {formatProperty("Start", start ? new Date(start).toLocaleString() : "")}
      {formatProperty("Classroom", classroom)}
      {formatProperty("Teacher 1", teacher1)}
      {formatProperty("Teacher 2", teacher2)}
      {formatProperty("Teacher 3", teacher3)}
      {formatProperty("Manager", manager)}
      {formatProperty("Grad Cert (MTech)", cert)}
    </Modal>
  );
});

const MyCalendar: React.FC = () => {
  const calendarRef = useRef<FullCalendar | null>(null);
  const [calendarApi, setCalendarApi] = useState<CalendarApi | null>(null);
  const [events, setEvents] = useState<EventApi[]>([]);
  const [teachers, setTeachers] = useState<string[]>([]);
  const [selectedEvent, setSelectedEvent] = useState<EventApi | null>(null);

  useEffect(() => {
    let calendarApi = calendarRef.current?.getApi();
    if (calendarApi) {
      setCalendarApi(calendarApi);
      setEvents(calendarApi.getEvents());
    }
  }, []);

  const viewport = useViewport();
  if (!viewport) return null;
  const { width } = viewport;

  const handleEventClick = (clickInfo: { event: EventApi }) => {
    setSelectedEvent(clickInfo.event);
  };

  const handleCloseDetails = () => {
    setSelectedEvent(null);
  };

  const renderEventContent = (eventInfo: any) => {
    const title = eventInfo.event.title;
    const data = eventInfo.event.extendedProps;
    const color = getRandomColor(title);
    const fontSize1 = width > 1320 ? "18px" : width > 660 ? "14px" : "10px";
    const fontSize2 = width > 1320 ? "14px" : width > 660 ? "10px" : "8px";
    return (
      <div style={{ padding: "4px 8px", backgroundColor: color, cursor: "pointer" }}>
        <i style={{ fontSize: fontSize1, color: "#111", fontWeight: "bold" }}>{title}</i>
        <div style={{ fontSize: fontSize2, color: "#333", display: "flex", justifyContent: "space-between" }}>
          <i>{data.classroom}</i>
          <br />
          <i>{data.teacher1}</i>
        </div>
      </div>
    );
  };

  const fetchEvents: EventSourceFunc = useCallback(
    async (fetchInfo, successCallback, failureCallback) => {
      try {
        let data = await getCalenderData(fetchInfo, teachers);
        setEvents(data);
        successCallback(data);
      } catch (err: any) {
        failureCallback(err);
      }
    },
    [teachers]
  );

  return (
    <div>
      <Header calendarApi={calendarApi} setTeachers={setTeachers} />
      <div>
        <FullCalendar
          ref={calendarRef}
          plugins={[dayGridPlugin, timeGridPlugin, interactionPlugin]}
          headerToolbar={false}
          initialView="dayGridMonth"
          selectMirror={true}
          dayMaxEvents={true}
          events={fetchEvents}
          eventContent={renderEventContent} // custom render function
          eventClick={handleEventClick}
          slotMinTime={"08:00:00"}
          slotMaxTime={"20:00:00"}
          slotDuration={"01:00:00"}
          contentHeight={1500}
        />
      </div>
      <EventDetails selectedEvent={selectedEvent} handleCloseDetails={handleCloseDetails} />
      <Sidebar events={events} />
    </div>
  );
};

function getRandomColor(seed: string) {
  let hash = 0;
  for (let i = 0; i < seed.length; i++) {
    hash = seed.charCodeAt(i) + ((hash << 5) - hash);
  }
  let color = "#";
  for (let i = 0; i < 3; i++) {
    const value = (hash >> (i * 8)) & 0xff;
    color += ("00" + value.toString(16)).substr(-2);
  }
  return color;
}

export default MyCalendar;

const Sidebar: React.FC<{ events: EventApi[] }> = ({ events }) => {
  function renderSidebarEvent(event: any) {
    return (
      <li key={event.id}>
        <b>
          {formatDate(event.start, {
            year: "numeric",
            month: "short",
            day: "numeric",
          })}
        </b>
        <i>{event.title}</i>
      </li>
    );
  }
  return (
    <div>
      <h2>All Events ({events.length})</h2>
      <ul>{events.map(renderSidebarEvent)}</ul>
    </div>
  );
};
