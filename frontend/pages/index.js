import Head from 'next/head';
import { useState } from 'react';
import { useRouter } from 'next/router';

export default function Home() {
  const [algorithm, setAlgorithm] = useState('round-robin');
  const [numServers, setNumServers] = useState(5);
  const router = useRouter();

  const handleInitialize = async () => {
    const url = `http://localhost:8080/api/loadbalancer/initialize/${algorithm}/${numServers}`;
    try {
      const response = await fetch(url, { method: 'POST' });
      if (response.ok) {
        console.log('Load balancer initialized successfully');
        // Redirect to the request page with algorithm and server count as query parameters
        router.push(`/request?algorithm=${algorithm}&numServers=${numServers}`);
      } else {
        const errorData = await response.text();
        console.error(`Error initializing load balancer: ${errorData}`);
      }
    } catch (error) {
      console.error('Error:', error);
    }
  };

  const incrementServers = () => {
    if (numServers < 20) setNumServers(numServers + 1);
  };

  const decrementServers = () => {
    if (numServers > 1) setNumServers(numServers - 1);
  };

  return (
    <div className="min-h-screen bg-purple-50 flex flex-col items-center justify-center p-6">
      <Head>
        <title>Load Balancer Initialization</title>
      </Head>
      <h1 className="text-5xl font-extrabold mb-8 text-purple-700">Load Balancer Initialization</h1>
      <div className="flex items-center justify-center mb-6">
        <label htmlFor="algorithm" className="mr-3 text-xl">Select Algorithm:</label>
        <select
          id="algorithm"
          className="border border-gray-300 rounded-md p-3 text-gray-800 text-lg"
          value={algorithm}
          onChange={(e) => setAlgorithm(e.target.value)}
        >
          <option value="round-robin">Round Robin</option>
          <option value="weightedroundrobin">Weighted Round Robin</option>
          <option value="least-connections">Least Connections</option>
        </select>
      </div>
      <div className="flex items-center justify-center mb-8">
        <label htmlFor="servers" className="mr-3 text-xl">Number of Servers:</label>
        <div className="flex items-center space-x-4">
          <button onClick={decrementServers} className="px-6 py-3 bg-purple-400 text-white rounded-md hover:bg-purple-700 transition duration-300">-</button>
          <input type="number" id="servers" className="w-20 text-center border border-gray-300 text-black rounded-md p-3 text-lg" value={numServers} readOnly />
          <button onClick={incrementServers} className="px-6 py-3 bg-purple-400 text-white rounded-md hover:bg-purple-700 transition duration-300">+</button>
        </div>
      </div>
      <button onClick={handleInitialize} className="px-6 py-3 bg-purple-600 text-white rounded-md hover:bg-purple-700 transition duration-300">
        Initialize Load Balancer
      </button>
    </div>
  );
}
