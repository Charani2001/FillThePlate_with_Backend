import { supabaseClient } from "../dbconfigs";

export const postDonation = async (req: any, res: any) => {
  const {
    fullName,
    contactNumber,
    email,
    address,
    foodType,
    foodName,
    servings,
    location,
  } = req.body;

  if (
    !fullName ||
    !contactNumber ||
    !email ||
    !address ||
    !foodType ||
    !foodName ||
    !servings ||
    !location
  ) {
    return res.status(400).json({ message: "All fields are required" });
  }
  const { data, error } = await supabaseClient.from("donations").insert([
    {
      full_name: fullName,
      contact_number: contactNumber,
      email: email,
      address: address,
      food_type: foodType,
      food_name: foodName,
      servings: servings,
      pickup_location: location,
    },
  ]);

  if (error) {
    return res.status(400).json({
      message: "Failed to create donation",
      error: error.message,
    });
  }

  return res.status(201).json({
    message: "Donation posted successfully",
    data,
  });
};

export const getDonations = async (req: any, res: any) => {
  try {
    const { data, error } = await supabaseClient
      .from("donations")
      .select("*")
      .order("created_at", { ascending: false }); // optional: sort by latest first

    if (error) {
      console.error("Error fetching donations:", error.message);
      return res.status(500).json({
        message: "Failed to fetch donations",
        error: error.message,
      });
    }

    return res.status(200).json({
      message: "Donations fetched successfully",
      data,
    });
  } catch (err: any) {
    console.error("Unexpected error:", err);
    return res.status(500).json({
      message: "Unexpected server error",
      error: err.message,
    });
  }
};
