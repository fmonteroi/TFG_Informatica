import { Link, useNavigate } from 'react-router-dom'
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
        <div className="space-y-8">
            <TestingNotice />

            <section className="grid min-h-[58vh] items-center gap-10 py-8 lg:grid-cols-[minmax(0,1fr)_360px]">
                <div>
                    <p className="text-sm font-semibold uppercase text-cyan-300">
                        League of Legends data
                    </p>
                    <h1 className="mt-3 max-w-3xl text-4xl font-black leading-tight sm:text-5xl">
                        Consulta tu rendimiento sin perderte entre datos.
                    </h1>
                    <p className="mt-4 max-w-2xl text-lg text-slate-400">
                        Busca un Riot ID para ver rangos, estadísticas, historial y partidas en
                        curso desde una única vista.
                    </p>

                    <div className="mt-8 max-w-4xl">
                        <PlayerSearchBar onSearch={handleSearch} />
                    </div>
                </div>

                <aside className="border-l border-slate-800 pl-6">
                    <p className="text-xs font-semibold uppercase text-slate-500">Explorar</p>
                    <div className="mt-4 space-y-3">
                        <Link
                            to="/campeones"
                            className="block rounded-lg border border-slate-800 bg-slate-900 p-4 transition hover:border-cyan-300/50"
                        >
                            <span className="font-bold">Campeones</span>
                            <span className="mt-1 block text-sm text-slate-400">
                                Estadísticas y builds recomendadas.
                            </span>
                        </Link>
                        <Link
                            to="/profesionales"
                            className="block rounded-lg border border-slate-800 bg-slate-900 p-4 transition hover:border-cyan-300/50"
                        >
                            <span className="font-bold">Profesionales</span>
                            <span className="mt-1 block text-sm text-slate-400">
                                Equipos y probuilds recientes.
                            </span>
                        </Link>
                    </div>
                </aside>
            </section>
        </div>
    )
}

export default Home
