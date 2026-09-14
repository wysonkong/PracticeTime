import { useEffect, useState } from "react";
import { fetchTasks, toggleTask } from "./api";
import type { Task, Summary } from "./types";
import "./App.css";

export default function App() {
  const [tasks, setTasks] = useState<Task[]>([]);
  const [summary, setSummary] = useState<Summary>({ total: 0, completedCount: 0 });
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  function load() {
    setLoading(true);
    fetchTasks()
      .then((data) => {
        setTasks(data.tasks);
        setSummary(data.summary);
        setError(null);
      })
      .catch((err) => setError(String(err)))
      .finally(() => setLoading(false));
  }

  useEffect(() => {
    load();
  }, []);

  async function handleToggle(id: number) {
    const data = await toggleTask(id);
    setTasks(data.tasks);
    setSummary(data.summary);
  }

  return (
    <main className="app">
      <h1>Task Board</h1>
      <p className="summary">
        Completed: {summary.completedCount} / {summary.total}
      </p>

      {loading && <p>Loading...</p>}
      {error && <p className="error">{error}</p>}

      <ul className="task-list">
        {tasks.map((task) => (
          <li key={task.id} className={task.completed ? "task done" : "task"}>
            <label>
              <input
                type="checkbox"
                checked={task.completed}
                onChange={() => handleToggle(task.id)}
              />
              {/* Title mapping bug lives somewhere between here and types.ts */}
              <span className="title">{task.name || "Untitled"}</span>
              <span className="priority">{task.priority}</span>
            </label>
          </li>
        ))}
      </ul>
    </main>
  );
}
