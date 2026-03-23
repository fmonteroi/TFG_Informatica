import { useState } from 'react'
import JsonViewer from '../components/JsonViewer'
import {
    searchPlayer,
    refreshPlayer,
    getCurrentGame,
    refreshProfessionals,
    getMatchById,
    getChampionById,
    getAllChampions,
    getChampionBuilds,
} from '../api/backendApi'

// Panel for tests.
function Panel() {
    // ------------------------------
    // Inputs state
    // ------------------------------
    const [platform, setPlatform] = useState('EUW1')
    const [gameName, setGameName] = useState('iFran28')
    const [tagLine, setTagLine] = useState('EUW')
    const [puuid, setPuuid] = useState('')
    const [matchId, setMatchId] = useState('')
    const [championId, setChampionId] = useState('3')
    const [buildCount, setBuildCount] = useState(10)

    // State to store the backend response
    const [result, setResult] = useState<unknown>(null)

    // State to show if we are loading
    const [loading, setLoading] = useState(false)

    // State to show errors
    const [error, setError] = useState<string | null>(null)

    // ------------------------------
    // Aux function to execute requests
    // ------------------------------
    async function executeRequest(requestFn: () => Promise<unknown>) {
        try {
            setLoading(true)
            setError(null)

            const data = await requestFn()
            setResult(data)
        } catch (err) {
            if (err instanceof Error) {
                setError(err.message)
            } else {
                setError('Ha ocurrido un error desconocido')
            }
        } finally {
            setLoading(false)
        }
    }

    return (
        <div className="min-h-screen bg-slate-100 px-6 py-8 text-slate-900">
            <div className="mx-auto max-w-6xl">
                <header className="mb-8 rounded-xl border border-slate-200 bg-white p-6 shadow-sm">
                    <h1 className="text-3xl font-bold">Panel de pruebas</h1>
                </header>

                <div className="grid grid-cols-1 gap-6 lg:grid-cols-2">
                    <div className="space-y-6">
                        {/* Players */}
                        <section className="rounded-xl border border-slate-200 bg-white p-6 shadow-sm">
                            <h2 className="text-2xl font-semibold">Players</h2>

                            <div className="mt-4 space-y-4">
                                <div>
                                    <label className="mb-1 block text-sm font-medium text-slate-700">
                                        Platform
                                    </label>
                                    <input
                                        className="w-full rounded-lg border border-slate-300 bg-white px-3 py-2 outline-none transition focus:border-slate-500"
                                        value={platform}
                                        onChange={(e) => setPlatform(e.target.value)}
                                    />
                                </div>

                                <div>
                                    <label className="mb-1 block text-sm font-medium text-slate-700">
                                        Game Name
                                    </label>
                                    <input
                                        className="w-full rounded-lg border border-slate-300 bg-white px-3 py-2 outline-none transition focus:border-slate-500"
                                        value={gameName}
                                        onChange={(e) => setGameName(e.target.value)}
                                    />
                                </div>

                                <div>
                                    <label className="mb-1 block text-sm font-medium text-slate-700">
                                        Tag Line
                                    </label>
                                    <input
                                        className="w-full rounded-lg border border-slate-300 bg-white px-3 py-2 outline-none transition focus:border-slate-500"
                                        value={tagLine}
                                        onChange={(e) => setTagLine(e.target.value)}
                                    />
                                </div>

                                <button
                                    className="rounded-lg border border-slate-300 bg-slate-900 px-4 py-2 font-medium text-white transition hover:bg-slate-800"
                                    onClick={() =>
                                        executeRequest(() => searchPlayer(platform, gameName, tagLine))
                                    }
                                >
                                    Buscar jugador
                                </button>

                                <div className="border-t border-slate-200 pt-4">
                                    <label className="mb-1 block text-sm font-medium text-slate-700">
                                        PUUID
                                    </label>
                                    <input
                                        className="w-full rounded-lg border border-slate-300 bg-white px-3 py-2 outline-none transition focus:border-slate-500"
                                        value={puuid}
                                        onChange={(e) => setPuuid(e.target.value)}
                                    />
                                </div>

                                <div className="flex flex-wrap gap-3">
                                    <button
                                        className="rounded-lg border border-slate-300 bg-white px-4 py-2 font-medium transition hover:bg-slate-100"
                                        onClick={() =>
                                            executeRequest(() => refreshPlayer(platform, puuid))
                                        }
                                    >
                                        Refrescar jugador
                                    </button>

                                    <button
                                        className="rounded-lg border border-slate-300 bg-white px-4 py-2 font-medium transition hover:bg-slate-100"
                                        onClick={() =>
                                            executeRequest(() => getCurrentGame(puuid))
                                        }
                                    >
                                        Ver partida actual
                                    </button>
                                </div>
                            </div>
                        </section>

                        {/* Professionals */}
                        <section className="rounded-xl border border-slate-200 bg-white p-6 shadow-sm">
                            <h2 className="text-2xl font-semibold">Professionals</h2>

                            <div className="mt-4">
                                <button
                                    className="rounded-lg border border-slate-300 bg-white px-4 py-2 font-medium transition hover:bg-slate-100"
                                    onClick={() => executeRequest(() => refreshProfessionals())}
                                >
                                    Refrescar profesionales
                                </button>
                            </div>
                        </section>

                        {/* Matches */}
                        <section className="rounded-xl border border-slate-200 bg-white p-6 shadow-sm">
                            <h2 className="text-2xl font-semibold">Matches</h2>

                            <div className="mt-4 space-y-4">
                                <div>
                                    <label className="mb-1 block text-sm font-medium text-slate-700">
                                        Match ID
                                    </label>
                                    <input
                                        className="w-full rounded-lg border border-slate-300 bg-white px-3 py-2 outline-none transition focus:border-slate-500"
                                        value={matchId}
                                        onChange={(e) => setMatchId(e.target.value)}
                                    />
                                </div>

                                <button
                                    className="rounded-lg border border-slate-300 bg-white px-4 py-2 font-medium transition hover:bg-slate-100"
                                    onClick={() => executeRequest(() => getMatchById(matchId))}
                                >
                                    Buscar partida
                                </button>
                            </div>
                        </section>

                        {/* Champions */}
                        <section className="rounded-xl border border-slate-200 bg-white p-6 shadow-sm">
                            <h2 className="text-2xl font-semibold">Champions</h2>

                            <div className="mt-4 space-y-4">
                                <div>
                                    <label className="mb-1 block text-sm font-medium text-slate-700">
                                        Champion ID
                                    </label>
                                    <input
                                        className="w-full rounded-lg border border-slate-300 bg-white px-3 py-2 outline-none transition focus:border-slate-500"
                                        value={championId}
                                        onChange={(e) => setChampionId(e.target.value)}
                                    />
                                </div>

                                <div>
                                    <label className="mb-1 block text-sm font-medium text-slate-700">
                                        Build count
                                    </label>
                                    <input
                                        className="w-full rounded-lg border border-slate-300 bg-white px-3 py-2 outline-none transition focus:border-slate-500"
                                        type="number"
                                        value={buildCount}
                                        onChange={(e) => setBuildCount(Number(e.target.value))}
                                    />
                                </div>

                                <div className="flex flex-wrap gap-3">
                                    <button
                                        className="rounded-lg border border-slate-300 bg-white px-4 py-2 font-medium transition hover:bg-slate-100"
                                        onClick={() => executeRequest(() => getAllChampions())}
                                    >
                                        Ver todos los campeones
                                    </button>

                                    <button
                                        className="rounded-lg border border-slate-300 bg-white px-4 py-2 font-medium transition hover:bg-slate-100"
                                        onClick={() => executeRequest(() => getChampionById(championId))}
                                    >
                                        Ver campeón por ID
                                    </button>

                                    <button
                                        className="rounded-lg border border-slate-300 bg-white px-4 py-2 font-medium transition hover:bg-slate-100"
                                        onClick={() =>
                                            executeRequest(() => getChampionBuilds(championId, buildCount))
                                        }
                                    >
                                        Ver builds del campeón
                                    </button>
                                </div>
                            </div>
                        </section>
                    </div>

                    {/* State */}
                    <div className="space-y-6">
                        <section className="rounded-xl border border-slate-200 bg-white p-6 shadow-sm">
                            <h2 className="text-2xl font-semibold">Estado</h2>

                            <div className="mt-4 space-y-3">
                                {loading && (
                                    <p className="rounded-lg border border-blue-200 bg-blue-50 px-3 py-2 text-blue-700">
                                        Cargando...
                                    </p>
                                )}

                                {error && (
                                    <p className="rounded-lg border border-red-200 bg-red-50 px-3 py-2 text-red-700">
                                        Error: {error}
                                    </p>
                                )}

                                {!loading && !error && (
                                    <p className="rounded-lg border border-slate-200 bg-slate-50 px-3 py-2 text-slate-600">
                                        Sin errores. Esperando una acción.
                                    </p>
                                )}
                            </div>
                        </section>

                        {/* Result */}
                        <section className="rounded-xl border border-slate-200 bg-white p-6 shadow-sm">
                            <h2 className="text-2xl font-semibold">Respuesta del backend</h2>
                            <div className="mt-4">
                                <JsonViewer data={result} />
                            </div>
                        </section>
                    </div>
                </div>
            </div>
        </div>
    )
}

export default Panel