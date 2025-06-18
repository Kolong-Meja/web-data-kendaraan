import { defineConfig } from "vite";
import tailwindcss from "@tailwindcss/vite";

export default defineConfig({
  plugins: [tailwindcss()],
  server: {
    host: "0.0.0.0",
    port: 3000,
    strictPort: true,
    cors: {
      origin: "http://localhost:8082",
      credentials: true,
    },
    hmr: {
      host: "localhost",
    },
  },
});
