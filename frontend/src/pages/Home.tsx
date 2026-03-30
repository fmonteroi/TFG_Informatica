import { useNavigate } from 'react-router-dom'
import PlayerSearchBar from '../components/player/PlayerSearchBar.tsx'

function Home() {
    const navigate = useNavigate()

    function handleSearch(platform: string, gameName: string, tagLine: string) {
        navigate(
            `/jugador/${encodeURIComponent(platform)}/${encodeURIComponent(gameName)}/${encodeURIComponent(tagLine)}`,
        )
    }

    return (
        <section className="flex min-h-[70vh] items-center justify-center">
            <div className="w-full max-w-5xl">
                <div className="mb-10 text-center">
                    <h1 className="text-5xl font-black tracking-tight">Busca un jugador</h1>
                </div>

                <PlayerSearchBar onSearch={handleSearch} />
            </div>
        </section>
    )
}

export default Home
