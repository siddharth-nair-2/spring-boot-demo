import {
  Button,
  FormControl,
  FormErrorMessage,
  FormLabel,
  Input,
  Select,
} from "@chakra-ui/react";
import { Form, SubmitHandler, useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { z } from "zod";
import axios from "axios";

const createCustomerSchema = z.object({
  name: z.string().min(1, { message: "Name is required." }),
  email: z
    .string()
    .min(1, { message: "Email is required." })
    .email({ message: "Must be a valid email." }),
  age: z.number().gte(13, { message: "You must be at least 13 years old." }),
  gender: z.string(),
});

type CustomerSchema = z.infer<typeof createCustomerSchema>;

const CreateCustomerForm = () => {
  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm<CustomerSchema>({
    resolver: zodResolver(createCustomerSchema),
    defaultValues: {
      name: "",
      email: "",
      age: 18,
      gender: "MALE",
    },
  });

  return (
    <form
      className="px-8 pt-6 pb-8 mb-4"
      onSubmit={handleSubmit((d) => console.log(d))}
    >
      <div className="mb-4">
        <FormLabel
          className="block mb-2 text-sm font-bold text-gray-700"
          htmlFor="name"
        >
          Name
        </FormLabel>
        <Input id="name" type="text" placeholder="Name" {...register("name")} />
        {errors.name && (
          <div className=" text-red-500">{errors.name.message}</div>
        )}
      </div>
      <div className="mb-4">
        <FormLabel
          className="block mb-2 text-sm font-bold text-gray-700"
          htmlFor="email"
        >
          Email
        </FormLabel>
        <Input
          id="email"
          type="email"
          placeholder="Email"
          {...register("email")}
        />
        {errors.email && (
          <div className=" text-red-500">{errors.email.message}</div>
        )}
      </div>
      <div className="mb-4">
        <FormLabel
          className="block mb-2 text-sm font-bold text-gray-700"
          htmlFor="age"
        >
          Age
        </FormLabel>
        <Input
          id="age"
          type="number"
          placeholder="37"
          {...register("age", { valueAsNumber: true })}
        />
        {errors.age && (
          <div className=" text-red-500">{errors.age.message}</div>
        )}
      </div>
      <div className="mb-4">
        <FormLabel
          className="block mb-2 text-sm font-bold text-gray-700"
          htmlFor="gender"
        >
          Gender
        </FormLabel>
        <Select id="gender" {...register("gender")}>
          <option value="MALE">Male</option>
          <option value="FEMALE">Female</option>
        </Select>
        {errors.gender && (
          <div className=" text-red-500">{errors.gender.message}</div>
        )}
      </div>
      <div className="mb-6 text-center">
        <Button colorScheme="teal" type="submit">
          Create
        </Button>
      </div>
      <hr className="mb-6 border-t" />
    </form>
  );
};

export default CreateCustomerForm;
