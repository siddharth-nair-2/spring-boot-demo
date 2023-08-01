import { Spinner, Text } from "@chakra-ui/react";
import SidebarWithHeader from "./components/shared/Sidebar";
import { useEffect, useState } from "react";
import { getCustomers } from "./services/client";
import { Customer } from "./interface/customer";
import ProfileCard from "./components/shared/ProfileCard";

function App() {
  const [customers, setCustomers] = useState<Customer[]>([]);
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    setLoading(true);
    getCustomers()
      .then((res) => {
        console.log(res.data);
        setCustomers(res.data);
      })
      .catch((err) => console.log(err))
      .finally(() => setLoading(false));
  }, []);

  if (loading) {
    return (
      <SidebarWithHeader>
        <Spinner
          thickness="4px"
          speed="0.65s"
          emptyColor="gray.200"
          color="blue.500"
          size="xl"
        />
      </SidebarWithHeader>
    );
  }

  if (customers.length <= 0) {
    return (
      <SidebarWithHeader>
        <Text>No Customers found!</Text>
      </SidebarWithHeader>
    );
  }

  return (
    <SidebarWithHeader>
      {customers.map((customer) => {
        return <ProfileCard customer={customer} />;
      })}
    </SidebarWithHeader>
  );
}

export default App;
