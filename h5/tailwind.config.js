/** @type {import('tailwindcss').Config} */
export default {
  content: [
    "./index.html",
    "./src/**/*.{vue,js,ts,jsx,tsx}",
  ],
  theme: {
    extend: {
      colors: {
        primary: '#165DFF',
        secondary: '#4080FF',
        neutral: '#6B7280',
        success: '#00B42A',
      },
      fontFamily: {
        inter: ['Inter', 'system-ui', 'sans-serif'],
      },
    },
  },
  plugins: [],
}