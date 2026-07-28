import { Link, NavLink, Route, Routes } from 'react-router-dom'
import Home from './pages/Home'
import Player from './pages/Player'
import Champions from './pages/Champions'
import ChampionBuilds from './pages/ChampionBuilds'
import Professionals from './pages/Professionals'
import ProfessionalProfile from './pages/ProfessionalProfile'

function navClass({ isActive }: { isActive: boolean }) {
    const baseClasses =
        'border-b-2 px-2 py-3 text-base font-semibold transition focus-visible:outline-2 focus-visible:outline-cyan-300'

    if (isActive) {
        return `${baseClasses} border-cyan-300 text-cyan-200`
    } else {
        return `${baseClasses} border-transparent text-slate-300 hover:border-slate-600 hover:text-white`
    }
}

function App() {
    return (
        <div className="flex min-h-screen flex-col bg-slate-950 text-slate-100">
            <header className="sticky top-0 z-40 border-b border-slate-800 bg-slate-950/95 backdrop-blur">
                <div className="mx-auto flex max-w-7xl flex-col gap-3 px-4 py-3 sm:flex-row sm:items-center sm:justify-between">
                    <Link
                        to="/"
                        className="flex w-fit items-center gap-2 text-3xl font-black text-white focus-visible:outline-2 focus-visible:outline-cyan-300"
                    >
                        <img src="/favicon.svg" alt="" className="h-9 w-9" />
                        Easy<span className="text-cyan-300">Rift</span>
                    </Link>

                    <nav aria-label="Navegación principal" className="flex gap-4 overflow-x-auto">
                        <NavLink to="/" end className={navClass}>
                            Buscador
                        </NavLink>
                        <NavLink to="/campeones" className={navClass}>
                            Campeones
                        </NavLink>
                        <NavLink to="/profesionales" className={navClass}>
                            Profesionales
                        </NavLink>
                    </nav>
                </div>
            </header>

            <main className="mx-auto w-full max-w-7xl flex-1 px-4 py-7 sm:py-9">
                <Routes>
                    <Route path="/" element={<Home />} />
                    <Route path="/jugador/:platform/:gameName/:tagLine" element={<Player />} />
                    <Route path="/campeones" element={<Champions />} />
                    <Route path="/campeones/:championId" element={<ChampionBuilds />} />
                    <Route path="/profesionales" element={<Professionals />} />
                    <Route path="/profesionales/:puuid" element={<ProfessionalProfile />} />
                </Routes>
            </main>

            <footer className="relative z-10 border-t border-slate-800 bg-slate-950 px-4 py-4 text-center text-xs text-slate-400">
                EasyRift es un proyecto educativo desarrollado como Trabajo de Fin de Grado.
                No está afiliado, respaldado ni patrocinado por Riot Games.
            </footer>
        </div>
    )
}

export default App
