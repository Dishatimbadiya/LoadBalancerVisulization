import { useState, useEffect } from 'react';
import { useRouter } from 'next/router';

export default function RequestPage() {
    const router = useRouter();
    const { algorithm, numServers } = router.query; // Get query parameters from the URL
    const [serverRequests, setServerRequests] = useState([]);
    const [requestId, setRequestId] = useState(1);

    useEffect(() => {
        if (numServers) {
            setServerRequests(Array(parseInt(numServers, 10)).fill([]));
        }
    }, [numServers]);

    const handleSendRequest = async () => {
        const url = `http://localhost:8080/api/loadbalancer/request/${algorithm}`;
        try {
            const response = await fetch(url, { method: 'POST' });

            if (response.ok) {
                const serverId = await response.json();
                console.log(`Request handled by server: ${serverId}`);

                setServerRequests((prevRequests) => {
                    const updatedRequests = [...prevRequests];
                    updatedRequests[serverId - 1] = [
                        ...updatedRequests[serverId - 1],
                        `Request ${requestId}`
                    ];
                    return updatedRequests;
                });

                setRequestId((prevId) => prevId + 1);
            } else {
                const errorData = await response.text();
                console.error(`Error sending request: ${errorData}`);
            }
        } catch (error) {
            console.error('Error:', error);
        }
    };

    const handleGoBack = () => {
        router.push('/');
    };

    return (
        <div className="min-h-screen bg-purple-50 flex flex-col items-center justify-center p-6">
            <h1 className="text-5xl font-extrabold mb-8 text-purple-700">Request Visualization</h1>

            <div className="mb-6 text-lg text-gray-700">
                <p><strong>Algorithm:</strong> {algorithm}</p>
                <p><strong>Number of Servers:</strong> {numServers}</p>
            </div>

            <div className="grid grid-cols-2 sm:grid-cols-3 md:grid-cols-4 lg:grid-cols-5 gap-6 mb-8 w-full">
                {serverRequests.map((requests, index) => (
                    <div key={index} className="flex flex-col items-center justify-center p-8 bg-purple-300 text-white rounded-lg shadow-md" style={{ height: '200px', width: '200px' }}>
                        <div className="text-4xl mb-4">🖥️</div>
                        <span className="text-xl font-semibold">Server {index + 1}</span>
                        <ul className="mt-4 text-sm text-purple-900">
                            {requests.map((request, reqIndex) => (
                                <li key={reqIndex} className="bg-purple-100 rounded px-2 py-1 mb-1">{request}</li>
                            ))}
                        </ul>
                    </div>
                ))}
            </div>

            <div className="flex space-x-4">
                <button onClick={handleSendRequest} className="px-8 py-4 bg-purple-600 text-white rounded-md hover:bg-purple-700 transition duration-300">
                    Send Request
                </button>

                <button onClick={handleGoBack} className="px-8 py-4 bg-gray-500 text-white rounded-md hover:bg-gray-700 transition duration-300">
                    Back
                </button>
            </div>
        </div>
    );
}
