import express from "express";
import {
  deleteUser,
  login,
  readSingleUser,
  signUp,
  updateUser,
} from "../controllers/userController";

const router = express.Router();

// Signup Route
router.post("/users/signup", signUp);

// Login Route
router.post("/users/login", login);

// Read Single User
router.get("/users/readsingle", readSingleUser);

router.put("/users/updateuser", updateUser);

router.delete("/users/deleteuser", deleteUser);

export default router;
