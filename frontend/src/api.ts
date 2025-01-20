export const ROOT_PATH = process.env.NODE_ENV === "production" ? "" : "http://localhost:9000";

const getCsrfToken = () => {
  const csrfToken = document.cookie.match("(^|;)\\s*XSRF-TOKEN\\s*=\\s*([^;]+)")?.pop();
  return csrfToken || "";
};

export async function getCalenderData(startStr: string, endStr: string, teachers: string[], students: string[]) {
  const params = new URLSearchParams({
    startStr,
    endStr,
    teachers: teachers.join(","),
    students: students.join(","),
  }).toString();
  return await fetch(`${ROOT_PATH}/data?${params}`)
    .then((response) => response.json())
    .then((data) => {
      const events = data.data;
      events.forEach((event: any) => {
        event.title = event.courseName;
        event.resource = event.classroom;
        const date = new Date(Number(event.courseDate));
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

export async function getAllStudents() {
  return await fetch(`${ROOT_PATH}/student`)
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
  software?: string | undefined;
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
    headers: new Headers({
      "Content-Type": "application/json",
      "X-XSRF-Token": getCsrfToken(),
    }),
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
    headers: new Headers({
      "X-XSRF-Token": getCsrfToken(),
    }),
  })
    .then((response) => response.json())
    .then((data) => {
      return data.data;
    })
    .catch((error) => console.error("Error:", error));
}

export async function getUser(): Promise<boolean> {
  return await fetch(`${ROOT_PATH}/login`)
    .then((response) => response.json())
    .catch((error) => console.error("Error:", error));
}

export async function getClassname(): Promise<string[]> {
  return await fetch(`${ROOT_PATH}/classname`)
    .then((response) => response.json())
    .then((data) => {
      return data.data;
    })
    .catch((error) => console.error("Error:", error));
}

export const uploadExcel = async (file: File) => {
  const formData = new FormData();
  formData.append("file", file);

  const response = await fetch(`${ROOT_PATH}/upload`, {
    method: "POST",
    headers: new Headers({
      "X-XSRF-Token": getCsrfToken(),
    }),
    body: formData,
  });

  if (!response.ok) {
    throw new Error("Upload failed");
  }

  return response.json();
};

export const downloadExcel = async () => {
  const response = await fetch(`${ROOT_PATH}/download`, {
    method: "GET",
  });

  if (!response.ok) {
    throw new Error("Download failed");
  }

  const blob = await response.blob();
  const url = window.URL.createObjectURL(blob);
  const a = document.createElement("a");
  a.href = url;
  a.download = "Course.xlsx";
  document.body.appendChild(a);
  a.click();
  a.remove();
  window.URL.revokeObjectURL(url);
};

export async function getUserToken() {
  return await fetch(`${ROOT_PATH}/login/token_details`)
    .then((response) => response.json())
    .catch((error) => console.error("Error:", error));
}
