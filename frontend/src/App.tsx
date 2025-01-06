import MyCalendar from "./MyCalender";
import UploadPage from "./UploadPage";
import { useViewport } from "./ViewportContext";

const App = () => {
  const viewport = useViewport();
  if (!viewport) return null;

  return (
    <div style={{ padding: viewport.width > 660 ? "0px 24px" : "0" }}>
      <h1>AI-based Scheduling System</h1>
      <UploadPage />
      <MyCalendar />
    </div>
  );
};

export default App;
