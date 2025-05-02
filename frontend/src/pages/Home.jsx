import { useEffect, useState } from 'react';
import { getData } from '../api/backend';

export default function Home() {
    const [data, setData] = useState(null);

    useEffect(() => {
        getData()
            .then(response => setData(response.data))
            .catch(error => console.error('Error:', error));
    }, []);

    return (
        <div>
            <h1>Mon Projet Frontend</h1>
            {data && <pre>{JSON.stringify(data, null, 2)}</pre>}
        </div>
    );
}