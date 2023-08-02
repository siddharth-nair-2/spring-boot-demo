import axios, { AxiosError } from "axios";
import { CustomerSchema } from "../components/CreateCustomerForm";
import { notifications } from "./notification";

export const getCustomers = async () => {
  try {
    return await axios.get(
      `${import.meta.env.VITE_API_BASE_URL}/api/v1/customers`
    );
  } catch (error: unknown | AxiosError) {
    console.log(error);
    if (axios.isAxiosError(error)) {
      notifications(
        error.response?.data.error,
        error.response?.data.message,
        "error"
      );
    } else {
      notifications("ERROR", "An Unknown error occured", "error");
    }
    throw error;
  }
};

export const saveCustomer = async (customer: CustomerSchema) => {
  try {
    return await axios.post(
      `${import.meta.env.VITE_API_BASE_URL}/api/v1/customers`,
      customer
    );
  } catch (error: unknown | AxiosError) {
    console.log(error);
    if (axios.isAxiosError(error)) {
      notifications(
        error.response?.data.error,
        error.response?.data.message,
        "error"
      );
    } else {
      notifications("ERROR", "An Unknown error occured", "error");
    }
    throw error;
  }
};

export const deleteCustomer = async (id: number) => {
  try {
    return await axios.delete(
      `${import.meta.env.VITE_API_BASE_URL}/api/v1/customers/${id}`
    );
  } catch (error: unknown | AxiosError) {
    console.log(error);
    if (axios.isAxiosError(error)) {
      notifications(
        error.response?.data.error,
        error.response?.data.message,
        "error"
      );
    } else {
      notifications("ERROR", "An Unknown error occured", "error");
    }
    throw error;
  }
};
