// pages/_app.js
import '../styles/global.css';  // Import global CSS here

export default function App({ Component, pageProps }) {
  return <Component {...pageProps} />;
}
