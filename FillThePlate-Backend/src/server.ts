import dotenv from "dotenv";

import express from "express";
import cors from "cors";

import userRoute from "./routes/userRoute";
import donationRoute from "./routes/donationRoute";

const app = express();
app.use(express.json());
app.use(cors());

dotenv.config();

//Default Routes
app.use("/api", userRoute, donationRoute);

app.listen(5000, "0.0.0.0", () => {
  console.log("Server running on port 5000");
});
