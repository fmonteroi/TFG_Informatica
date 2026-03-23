import {NavLink, Route, Routes} from 'react-router-dom'
import Home from './pages/Home'
import Player from './pages/Player'
import Champions from './pages/Champions'
import Panel from './pages/Panel'

function navClass({isActive}: { isActive: boolean }) {
    const baseClasses = 'rounded-xl px-4 py-2 text-sm font-medium transition'

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
                <div className="mx-auto flex max-w-7xl items-center justify-between px-4 py-4">
                    <div>
                        <p className="text-lg font-bold">LoL Pro Tracker</p>
                        <p className="text-sm text-slate-400">Buscador, historial y builds</p>
                    </div>

                    <nav className="flex flex-wrap gap-2">
                        <NavLink to="/" className={navClass}>
                            Buscador
                        </NavLink>

                        <NavLink to="/campeones" className={navClass}>
                            Campeones
                        </NavLink>

                        <NavLink to="/pruebas" className={navClass}>
                            Panel de pruebas
                        </NavLink>
                    </nav>
                </div>
            </header>

            <main className="mx-auto max-w-7xl px-4 py-8">
                <Routes>
                    <Route path="/" element={<Home/>}/>
                    <Route path="/jugador/:platform/:gameName/:tagLine" element={<Player/>}/>
                    <Route path="/campeones" element={<Champions/>}/>
                    <Route path="/campeones/:championId" element={<Champions/>}/>
                    <Route path="/pruebas" element={<Panel/>}/>
                </Routes>
            </main>
        </div>
    )
}

export default App