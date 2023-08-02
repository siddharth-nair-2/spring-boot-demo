"use client";

import {
  Heading,
  Avatar,
  Box,
  Center,
  Image,
  Flex,
  Text,
  Stack,
  Tag,
  useColorModeValue,
  useDisclosure,
  Button,
  AlertDialog,
  AlertDialogBody,
  AlertDialogFooter,
  AlertDialogHeader,
  AlertDialogContent,
  AlertDialogOverlay,
} from "@chakra-ui/react";
import { Customer } from "../../interface/customer";
import { MdDelete } from "react-icons/md";
import { useRef, useState } from "react";
import { deleteCustomer } from "../../services/client";
import { notifications } from "../../services/notification";
import { AxiosError } from "axios";

export default function ProfileCard({
  customer,
  fetchCustomers,
}: {
  customer: Customer;
  fetchCustomers: () => void;
}) {
  const { isOpen, onOpen, onClose } = useDisclosure();
  const cancelRef = useRef<HTMLButtonElement>(null);
  const [IsSubmitting, setIsSubmitting] = useState(false);

  const handleDelete = () => {
    console.log(customer);
    setIsSubmitting(true);
    deleteCustomer(customer.id)
      .then((res) => {
        console.log(res);
        fetchCustomers();
        notifications(
          "Deleted customer",
          `We have deleted ${customer.name} successfully.`,
          "success"
        );
      })
      .catch((error: unknown | AxiosError) => {
        console.log(error);
      })
      .finally(() => setIsSubmitting(false));
    onClose();
  };

  return (
    <>
      <Center py={6}>
        <Box
          minW={"250px"}
          maxW={"350px"}
          w={"full"}
          bg={useColorModeValue("white", "gray.800")}
          boxShadow={"2xl"}
          rounded={"md"}
          overflow={"hidden"}
        >
          <Image
            h={"120px"}
            w={"full"}
            src={
              "https://images.unsplash.com/photo-1612865547334-09cb8cb455da?ixid=MXwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHw%3D&ixlib=rb-1.2.1&auto=format&fit=crop&w=634&q=80"
            }
            objectFit="cover"
            alt="#"
          />
          <Flex justify={"center"} mt={-12}>
            <Avatar
              size={"xl"}
              src={`https://randomuser.me/api/portraits/${
                customer.gender === "MALE" ? "men" : "women"
              }/${customer.id}.jpg`}
              css={{
                border: "2px solid white",
              }}
            />
          </Flex>

          <Box p={6} className="flex flex-col justify-center items-center">
            <Stack spacing={2} align={"center"} mb={5}>
              <Tag borderRadius={"full"}>{customer.id}</Tag>
              <Heading fontSize={"2xl"} fontWeight={500} fontFamily={"body"}>
                {customer.name}
              </Heading>
              <Text color={"gray.500"}>{customer.email}</Text>
              <Text color={"gray.500"}>
                Age {customer.age} | {customer.gender}
              </Text>
            </Stack>
            <Button
              colorScheme="red"
              onClick={onOpen}
              variant={"solid"}
              size={"sm"}
              leftIcon={<MdDelete />}
            >
              Delete
            </Button>
          </Box>
        </Box>
      </Center>
      <AlertDialog
        isOpen={isOpen}
        leastDestructiveRef={cancelRef}
        onClose={onClose}
      >
        <AlertDialogOverlay>
          <AlertDialogContent>
            <AlertDialogHeader fontSize="lg" fontWeight="bold">
              Delete Customer
            </AlertDialogHeader>

            <AlertDialogBody>
              Are you sure? You can't undo this action afterwards.
            </AlertDialogBody>

            <AlertDialogFooter>
              <Button ref={cancelRef} onClick={onClose}>
                Cancel
              </Button>
              <Button
                colorScheme="red"
                onClick={handleDelete}
                ml={3}
                isLoading={IsSubmitting}
              >
                Delete
              </Button>
            </AlertDialogFooter>
          </AlertDialogContent>
        </AlertDialogOverlay>
      </AlertDialog>
    </>
  );
}
