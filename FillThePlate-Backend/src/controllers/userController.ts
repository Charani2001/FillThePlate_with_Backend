import { supabaseClient } from "../dbconfigs";
import { loginRequest, signUpRequest } from "../types";

// Signup
export const signUp = async (req: any, res: any) => {
  const { email, password, fullName, userName }: signUpRequest = req.body;

  if (!email || !password || !fullName || !userName) {
    return res.status(400).json({ message: "All fields are required" });
  }

  try {
    const [fullNameCheck, emailCheck, userNameCheck] = await Promise.all([
      supabaseClient
        .from("users")
        .select("id")
        .eq("full_name", fullName)
        .limit(1),
      supabaseClient.from("users").select("id").eq("email", email).limit(1),
      supabaseClient
        .from("users")
        .select("id")
        .eq("user_name", userName)
        .limit(1),
    ]);

    if (fullNameCheck.data?.length) {
      return res.status(409).json({ message: "Full name already reserved" });
    }

    if (emailCheck.data?.length) {
      return res.status(409).json({ message: "Email already reserved" });
    }

    if (userNameCheck.data?.length) {
      return res.status(409).json({ message: "User name already reserved" });
    }

    const { error, status, statusText } = await supabaseClient
      .from("users")
      .insert([{ email, password, full_name: fullName, user_name: userName }]);

    if (error) {
      return res.status(500).json({ message: error.message });
    }

    return res.status(201).json({ message: statusText, status });
  } catch (err) {
    console.error("SignUp Error:", err);
    return res.status(500).json({ message: "Internal server error" });
  }
};

// Login
export const login = async (req: any, res: any) => {
  const { userName, password }: loginRequest = req.body;

  if (!userName || !password) {
    return res
      .status(400)
      .json({ message: "User name and password are required" });
  }

  const { data, error, statusText, status } = await supabaseClient
    .from("users")
    .select("*")
    .eq("user_name", userName)
    .single();

  if (error || !data) {
    return res
      .status(401)
      .json({ message: "Can't find account for this user name" });
  }

  if (data.password === password) {
    res.status(201).json({ message: statusText, status: status });
  } else {
    return res.status(401).json({ message: "Incorrect password" });
  }
};

// Read user data
export const readSingleUser = async (req: any, res: any) => {
  const userName = req.query.userName as string;

  const { data, error, statusText, status } = await supabaseClient
    .from("users")
    .select("*")
    .eq("user_name", userName);

  if (error || !data || data.length === 0) {
    return res.status(404).json({ message: "Can't find account" });
  }

  res.status(200).json({ data, message: statusText, status });
};

export const updateUser = async (req: any, res: any) => {
  const { userName, fullName, email, password } = req.body;

  // Step 1: Get existing user data
  const { data: existingUser, error: fetchError } = await supabaseClient
    .from("users")
    .select("*")
    .eq("user_name", userName)
    .single();

  if (fetchError || !existingUser) {
    return res.status(401).json({ message: "User not found" });
  }

  // Step 2: Merge fields
  const updatedUser = {
    full_name: fullName || existingUser.full_name,
    email: email || existingUser.email,
    password: password || existingUser.password,
  };

  // Step 3: Update with merged fields
  const { data, error: updateError } = await supabaseClient
    .from("users")
    .update(updatedUser)
    .eq("user_name", userName)
    .single();

  if (updateError) {
    return res
      .status(400)
      .json({ message: "Update failed", error: updateError });
  }

  res.status(200).json({ message: "User updated successfully", data });
};

// Delete user
export const deleteUser = async (req: any, res: any) => {
  const { userName } = req.query;

  const { data, error } = await supabaseClient
    .from("users")
    .delete()
    .eq("user_name", userName);

  if (error) {
    return res
      .status(401)
      .json({ message: "Can't find account for this user name" });
  }

  if (error) {
    return res.status(400).json({ message: "Delete failed", error });
  }

  res.status(200).json({ message: "User deleted successfully", data });
};
