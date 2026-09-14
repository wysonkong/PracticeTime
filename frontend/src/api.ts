import type { TasksResponse } from "./types";

export async function fetchTasks(): Promise<TasksResponse> {
  const res = await fetch("/api/tasks");
  if (!res.ok) {
    throw new Error(`Request failed: ${res.status}`);
  }
  return res.json();
}

export async function toggleTask(id: number): Promise<TasksResponse> {
  const res = await fetch(`/api/tasks/${id}/toggle`, { method: "PATCH" });
  if (!res.ok) {
    throw new Error(`Request failed: ${res.status}`);
  }
  return res.json();
}
