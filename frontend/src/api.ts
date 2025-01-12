import {EventSourceFuncArg} from "@fullcalendar/core";

export const ROOT_PATH = "http://localhost:9000";

export async function getCalenderData(fetchInfo: EventSourceFuncArg, teachers: string[]) {
  const { startStr, endStr } = fetchInfo;
  const params = new URLSearchParams({ startStr, endStr, teachers: teachers.join(",") }).toString();
  return await fetch(`${ROOT_PATH}/data?${params}`)
    .then((response) => response.json())
    .then((data) => {
      let events = data.data;
      events.forEach((event: any) => {
        event.title = event.courseName;
        let date = new Date(Number(event.courseDate));
        event.start = date.toISOString();
        event.end = date.toISOString();
        event.allDay = true;
      });
      return events;
    })
    .catch((error) => {
      console.error("Error:", error);
      return [];
    });
}

export async function getAllTeachers() {
  return await fetch(`${ROOT_PATH}/teacher`)
    .then((response) => response.json())
    .then((data) => {
      return data.data;
    })
    .catch((error) => console.error("Error:", error));
}

export interface ClassroomType {
  id: number;
  name: string;
  size: number;
  software: string | null;
}

export async function getClassroom(): Promise<ClassroomType[]> {
  return await fetch(`${ROOT_PATH}/classroom`)
  .then((response) => response.json())
  .then((data) => {
    return data.data;
  })
  .catch((error) => console.error("Error:", error));
}

export async function saveClassroom(data: ClassroomType) {
  return await fetch(`${ROOT_PATH}/classroom`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(data),
  })
  .then((response) => response.json())
  .then((data) => {
    return data.data;
  })
  .catch((error) => console.error("Error:", error));
}

export async function deleteClassroom(id: number) {
  return await fetch(`${ROOT_PATH}/classroom/${id}`, {
    method: "DELETE",
  })
  .then((response) => response.json())
  .then((data) => {
    return data.data;
  })
  .catch((error) => console.error("Error:", error));
}