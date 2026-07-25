import { useNavigate } from 'react-router-dom'
import PlayerSearchBar from '../components/player/PlayerSearchBar'
import TestingNotice from '../components/TestingNotice'

function Home() {
    const navigate = useNavigate()

    function handleSearch(platform: string, gameName: string, tagLine: string) {
        navigate(
            `/jugador/${encodeURIComponent(platform)}/${encodeURIComponent(gameName)}/${encodeURIComponent(tagLine)}`,
        )
    }

    return (
        <>
            <TestingNotice />
            <section
                aria-label="Buscar jugador"
                className="flex min-h-[calc(100vh-9rem)] items-center justify-center"
            >
                <div className="w-full max-w-5xl">
                    <PlayerSearchBar onSearch={handleSearch} />
                </div>
            </section>
        </>
    )
}

export default Home
