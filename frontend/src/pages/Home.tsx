import { useState } from 'react'
import { useNavigate } from 'react-router-dom'

const PLATFORMS = ['BR1','EUN1','EUW1','JP1','KR','LA1','LA2','ME1','NA1','OC1','RU','SG2','TR1','TW2','VN2']

function Home() {
    const navigate = useNavigate()
    const [gameName, setGameName] = useState('')
    const [tagLine, setTagLine] = useState('')
    const [platform, setPlatform] = useState('EUW1')

    function handleSubmit(event: React.FormEvent<HTMLFormElement>) {
        event.preventDefault()

        const trimmedGameName = gameName.trim()
        const trimmedTagLine = tagLine.trim()

        if (!trimmedGameName || !trimmedTagLine) {
            return
        }

        navigate(
            `/jugador/${encodeURIComponent(platform)}/${encodeURIComponent(trimmedGameName)}/${encodeURIComponent(trimmedTagLine)}`,
        )
    }

    return (
        <section className="flex min-h-[70vh] items-center justify-center">
            <div className="w-full max-w-5xl">
                <div className="mb-10 text-center">
                    <h1 className="text-5xl font-black tracking-tight">Busca un jugador</h1>
                    <p className="mt-3 text-lg text-slate-400">
                        Introduce nombre, tagline y plataforma.
                    </p>
                </div>

                <form
                    onSubmit={handleSubmit}
                    className="grid gap-3 rounded-3xl border border-slate-800 bg-slate-950/70 p-4 lg:grid-cols-[1.3fr_0.8fr_220px_160px]"
                >
                    <input
                        className="w-full rounded-2xl border border-slate-700 bg-slate-900 px-4 py-4 text-lg outline-none transition focus:border-cyan-400"
                        placeholder="Nombre del jugador"
                        value={gameName}
                        onChange={(e) => setGameName(e.target.value)}
                    />

                    <input
                        className="w-full rounded-2xl border border-slate-700 bg-slate-900 px-4 py-4 text-lg outline-none transition focus:border-cyan-400"
                        placeholder="Tagline (sin #)"
                        value={tagLine}
                        onChange={(e) => setTagLine(e.target.value)}
                    />

                    <select
                        className="w-full rounded-2xl border border-slate-700 bg-slate-900 px-4 py-4 text-lg outline-none transition focus:border-cyan-400"
                        value={platform}
                        onChange={(e) => setPlatform(e.target.value)}
                    >
                        {PLATFORMS.map((platformOption) => (
                            <option key={platformOption} value={platformOption}>
                                {platformOption}
                            </option>
                        ))}
                    </select>

                    <button
                        type="submit"
                        className="rounded-2xl bg-cyan-500 px-6 py-4 text-lg font-bold text-slate-950 transition hover:bg-cyan-400"
                    >
                        Buscar
                    </button>
                </form>
            </div>
        </section>
    )
}

export default Home
