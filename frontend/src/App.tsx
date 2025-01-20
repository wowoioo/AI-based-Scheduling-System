import { useEffect, useState } from "react";
import MyRayCalendar from "./MyRayCalendar";
import UploadPage from "./UploadPage";
import { Button } from "./components/ui/button";
import { getUser, getUserToken, ROOT_PATH } from "./api";
import { LogIn } from "lucide-react";

function App() {
  const [data, setData] = useState<boolean>(false);
  const [name, setName] = useState<string>("");

  useEffect(() => {
    getUser().then((data) => setData(data));
  }, []);

  useEffect(() => {
    if (data) {
      getUserToken().then((token) => setName(token.name));
    }
  }, [data]);

  const login = async () => {
    const data = await getUser();
    if (!data) {
      window.location.href = `${ROOT_PATH}/oauth2/authorization/azure`;
    }
  };

  return (
    <div className="px-2 sm:px-6">
      <div className={`flex flex-col lg:flex-row justify-between items-center gap-4`}>
        <h1 className="my-5">AI Course Scheduling System</h1>
        {data ? (
          <span>Hello {name}</span>
        ) : (
          <Button onClick={login}>
            <LogIn className="w-4 h-4" />
            Login
          </Button>
        )}
      </div>
      {data && <UploadPage />}
      <MyRayCalendar />
    </div>
  );
}

export default App;
