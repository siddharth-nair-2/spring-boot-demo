import { createStandaloneToast } from "@chakra-ui/react";

const { toast } = createStandaloneToast();

type statusOptions =
  | "loading"
  | "info"
  | "warning"
  | "success"
  | "error"
  | undefined;

export const notifications = (
  title: string,
  description: string,
  status: statusOptions
) => {
  toast({
    title,
    description,
    status,
    duration: 5000,
    isClosable: true,
  });
};
