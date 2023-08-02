import {
  Button,
  Drawer,
  DrawerBody,
  DrawerCloseButton,
  DrawerContent,
  DrawerFooter,
  DrawerHeader,
  DrawerOverlay,
  Text,
  useDisclosure,
} from "@chakra-ui/react";
import { IoMdAdd } from "react-icons/io";
import CreateCustomerForm from "./CreateCustomerForm";

const DrawerForm = ({
  customerExists,
  fetchCustomers,
}: {
  customerExists: boolean;
  fetchCustomers: () => void;
}) => {
  const { isOpen, onOpen, onClose } = useDisclosure();
  return (
    <>
      <Button
        leftIcon={<IoMdAdd style={{ color: "white" }} />}
        colorScheme="teal"
        onClick={onOpen}
      >
        Create customer
      </Button>
      {!customerExists && (
        <Text className=" text-center font-montserrat font-semibold text-xl">
          Sorry, no customers exist!
        </Text>
      )}
      <Drawer isOpen={isOpen} onClose={onClose} size={"xl"}>
        <DrawerOverlay />
        <DrawerContent>
          <DrawerCloseButton />
          <DrawerHeader>Add a customer</DrawerHeader>
          <DrawerBody>
            <CreateCustomerForm fetchCustomers={fetchCustomers} />
          </DrawerBody>
          <DrawerFooter>
            <Button onClick={onClose}>Close</Button>
          </DrawerFooter>
        </DrawerContent>
      </Drawer>
    </>
  );
};

export default DrawerForm;
