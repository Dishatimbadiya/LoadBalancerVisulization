import Head from 'next/head';
import { useState } from 'react';

export default function Home() {
  const [algorithm, setAlgorithm] = useState('roundrobin');
  const [numServers, setNumServers] = useState(5);
  const [serversInitialized, setServersInitialized] = useState(false);
  const [serverStates, setServerStates] = useState({});
  const [loading, setLoading] = useState(false);

  const handleInitialize = async () => {
    try {
      setLoading(true);
      const response = await fetch(`http://localhost:8080/api/loadbalancer/initialize/${algorithm}/${numServers}`, {
        method: 'POST',
      });

      if (response.ok) {
        setServersInitialized(true);
        setServerStates({});
      } else {
        console.error('Initialization failed');
      }
    } catch (error) {
      console.error('Error:', error);
    } finally {
      setLoading(false);
    }
  };

  const handleSendRequest = async () => {
    if (!serversInitialized) return;

    try {
      setLoading(true);
      const response = await fetch(`http://localhost:8080/api/loadbalancer/request/${algorithm}`, {
        method: 'POST',
      });

      if (response.ok) {
        const assignedServerId = await response.json();
        setServerStates(prev => ({
          ...prev,
          [assignedServerId]: (prev[assignedServerId] || 0) + 1
        }));
      }
    } catch (error) {
      console.error('Error:', error);
    } finally {
      setLoading(false);
    }
  };

  const incrementServers = () => {
    if (numServers < 20) {
      setNumServers(numServers + 1);
    }
  };

  const decrementServers = () => {
    if (numServers > 1) {
      setNumServers(numServers - 1);
    }
  };

  return (
    <div className="min-h-screen bg-purple-50 flex flex-col items-center justify-center p-6">
      <Head>
        <title>Load Balancer Visualization</title>
      </Head>

      <div className="text-center text-purple-700">
        <h1 className="text-5xl font-extrabold mb-8">Load Balancer Visualization</h1>

        {/* Algorithm Selection */}
        <div className="flex items-center justify-center mb-6">
          <label htmlFor="algorithm" className="mr-3 text-xl">Select Algorithm:</label>
          <select
            id="algorithm"
            className="border border-gray-300 rounded-md p-3 text-gray-800 text-lg"
            value={algorithm}
            onChange={(e) => setAlgorithm(e.target.value)}
            disabled={serversInitialized}
          >
            <option value="roundrobin">Round Robin</option>
            <option value="leastconnections">Least Connections</option>
            <option value="weightedroundrobin">Weighted Round Robin</option>
          </select>
        </div>

        {/* Server Configuration */}
        {!serversInitialized && (
          <div className="flex items-center justify-center mb-8">
            <label className="mr-3 text-xl">Number of Servers:</label>
            <div className="flex items-center space-x-4">
              <button
                onClick={decrementServers}
                className="px-6 py-3 bg-purple-400 text-white rounded-md hover:bg-purple-700 transition duration-300"
              >
                -
              </button>
              <span className="text-xl">{numServers}</span>
              <button
                onClick={incrementServers}
                className="px-6 py-3 bg-purple-400 text-white rounded-md hover:bg-purple-700 transition duration-300"
              >
                +
              </button>
            </div>
            <button
              onClick={handleInitialize}
              disabled={loading}
              className="ml-6 px-6 py-3 bg-purple-600 text-white rounded-md hover:bg-purple-700 transition duration-300"
            >
              {loading ? 'Initializing...' : 'Initialize'}
            </button>
          </div>
        )}

        {/* Server Visualization */}
        {serversInitialized && (
          <>
            <div className="grid grid-cols-2 sm:grid-cols-3 md:grid-cols-4 lg:grid-cols-5 gap-6 mb-8">
              {Array.from({ length: numServers }).map((_, index) => (
                <div
                  key={index}
                  className="p-4 bg-white rounded-lg shadow-lg"
                >
                  <div className="text-2xl mb-2">🖥️</div>
                  <div className="font-semibold">Server {index + 1}</div>
                  <div className="text-sm text-purple-600">
                    Requests: {serverStates[index + 1] || 0}
                  </div>
                </div>
              ))}
            </div>

            <button
              onClick={handleSendRequest}
              disabled={loading}
              className="px-8 py-4 bg-purple-600 text-white rounded-md hover:bg-purple-700 transition duration-300"
            >
              {loading ? 'Processing...' : 'Send Request'}
            </button>
          </>
        )}
      </div>
    </div>
  );
}
