import { describe, it, expect, vi, beforeEach, afterEach } from "vitest";
import { render, screen, waitFor } from "@testing-library/react";
import App from "./App";

const mockResponse = {
  tasks: [
    { id: 1, title: "Review pull request", priority: "HIGH", completed: false },
    { id: 2, title: "Write unit tests", priority: "MEDIUM", completed: true },
  ],
  summary: { total: 2, completedCount: 1 },
};

beforeEach(() => {
  vi.stubGlobal(
    "fetch",
    vi.fn().mockResolvedValue({
      ok: true,
      json: () => Promise.resolve(mockResponse),
    })
  );
});

afterEach(() => {
  vi.unstubAllGlobals();
});

describe("App", () => {
  it("renders each task's real title instead of falling back to Untitled", async () => {
    render(<App />);

    expect(await screen.findByText("Review pull request")).toBeInTheDocument();
    expect(screen.getByText("Write unit tests")).toBeInTheDocument();
    expect(screen.queryByText("Untitled")).not.toBeInTheDocument();
  });

  it("renders the completed count from the backend summary", async () => {
    render(<App />);

    expect(await screen.findByText(/Completed: 1 \/ 2/)).toBeInTheDocument();
  });
});
