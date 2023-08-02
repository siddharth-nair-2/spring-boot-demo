import { Spinner, Wrap, WrapItem } from "@chakra-ui/react";
import SidebarWithHeader from "./components/shared/Sidebar";
import { useEffect, useState } from "react";
import { getCustomers } from "./services/client";
import { Customer } from "./interface/customer";
import ProfileCard from "./components/shared/ProfileCard";
import DrawerForm from "./components/DrawerForm";

function App() {
  const [customers, setCustomers] = useState<Customer[]>([]);
  const [loading, setLoading] = useState(false);

  const fetchCustomers = () => {
    setLoading(true);
    getCustomers()
      .then((res) => {
        console.log(res.data);
        setCustomers(res.data);
      })
      .catch((err) => console.log(err))
      .finally(() => setLoading(false));
  };

  useEffect(() => {
    fetchCustomers();
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

  return (
    <SidebarWithHeader>
      <DrawerForm
        customerExists={customers.length > 0}
        fetchCustomers={fetchCustomers}
      />
      <Wrap justify={"center"} spacing={30}>
        {customers.map((customer) => {
          return (
            <WrapItem key={customer.id}>
              <ProfileCard
                customer={customer}
                fetchCustomers={fetchCustomers}
              />
            </WrapItem>
          );
        })}
      </Wrap>
    </SidebarWithHeader>
  );
}

export default App;
