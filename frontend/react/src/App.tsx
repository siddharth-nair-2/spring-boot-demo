import { Button } from "@chakra-ui/react";
import SidebarWithHeader from "./components/shared/Sidebar";
import { useEffect } from "react";
import { getCustomers } from "./services/client";

function App() {
  useEffect(() => {
    getCustomers()
      .then((res) => console.log(res))
      .catch((err) => console.log(err));
  }, []);

  return (
    <SidebarWithHeader>
      <Button colorScheme="teal" variant="outline">
        Click me!
      </Button>
    </SidebarWithHeader>
  );
}

export default App;
