import {NavLink, Route, Routes} from 'react-router-dom'
import Home from './pages/Home'
import Player from './pages/Player'
import Champions from './pages/Champions'
import ChampionBuilds from './pages/ChampionBuilds'

function navClass({isActive}: { isActive: boolean }) {
    const baseClasses = 'rounded-xl px-6 py-4 text-sm font-medium transition text-lg'

    if (isActive) {
        return `${baseClasses} bg-cyan-500 text-slate-950`
    } else {
        return `${baseClasses} bg-slate-800 text-slate-200 hover:bg-slate-700`
    }
}

function App() {
    return (
        <div className="min-h-screen bg-slate-950 text-slate-100">
            <header className="border-b border-slate-800 bg-slate-900">
                <div className="mx-auto flex max-w-7xl items-center justify-between px-5 py-5">
                    <div>
                        <p className="text-3xl font-bold">EasyRift</p>
                    </div>

                    <nav className="flex flex-wrap gap-2">
                        <NavLink to="/" className={navClass}>
                            Buscador
                        </NavLink>

                        <NavLink to="/campeones" className={navClass}>
                            Campeones
                        </NavLink>
                    </nav>
                </div>
            </header>

            <main className="mx-auto max-w-7xl px-4 py-8">
                <Routes>
                    <Route path="/" element={<Home/>}/>
                    <Route path="/jugador/:platform/:gameName/:tagLine" element={<Player/>}/>
                    <Route path="/campeones" element={<Champions/>}/>
                    <Route path="/campeones/:championId" element={<ChampionBuilds/>}/>
                </Routes>
            </main>
        </div>
    )
}

export default App