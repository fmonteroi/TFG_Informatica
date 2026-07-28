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
                className="relative left-1/2 -my-7 flex min-h-[calc(100vh-8rem)] w-screen -translate-x-1/2 items-center justify-center overflow-hidden px-4 sm:-my-9"
            >
                <img
                    src="https://ddragon.leagueoflegends.com/cdn/img/champion/splash/Kindred_3.jpg"
                    alt=""
                    className="absolute inset-0 h-full w-full object-cover object-center"
                />
                <div className="absolute inset-0 bg-slate-950/70" />

                <div className="relative z-10 w-full max-w-5xl">
                    <h1 className="relative -top-16 mb-10 text-center text-6xl font-black sm:text-7xl">
                        <span className="text-white">Easy</span>
                        <span className="text-cyan-300">Rift</span>
                    </h1>
                    <PlayerSearchBar onSearch={handleSearch} />
                </div>
            </section>
        </>
    )
}

export default Home
