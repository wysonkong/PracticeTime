export interface Task {
  id: number;
  name: string;
  priority: "LOW" | "MEDIUM" | "HIGH";
  completed: boolean;
}

export interface Summary {
  total: number;
  completedCount: number;
}

export interface TasksResponse {
  tasks: Task[];
  summary: Summary;
}
