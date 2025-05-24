import express from "express";
import { getDonations, postDonation } from "../controllers/donationController";

const router = express.Router();

// Post Donation Route
router.post("/donations/post", postDonation);

router.get("/donations/get", getDonations);

export default router;
