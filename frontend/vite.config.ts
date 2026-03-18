import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'
import tailwindcss from '@tailwindcss/vite'

// https://vite.dev/config/
export default defineConfig({
  plugins: [
      react(), // Activates the React plugin for Vite
      tailwindcss() // Activates the Tailwind CSS plugin for Vite
  ],
})
