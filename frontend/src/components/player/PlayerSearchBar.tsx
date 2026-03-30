import {useEffect, useState, type FormEvent} from 'react'
import {PLATFORMS} from '../../lib/constants.ts'


type PlayerSearchBarProps = {
    onSearch: (platform: string, gameName: string, tagLine: string) => void
    initialPlatform?: string
    initialGameName?: string
    initialTagLine?: string
}

function PlayerSearchBar({
                             onSearch,
                             initialPlatform = 'EUW1',
                             initialGameName = '',
                             initialTagLine = '',
                         }: PlayerSearchBarProps) {
    const [gameName, setGameName] = useState(initialGameName)
    const [tagLine, setTagLine] = useState(initialTagLine)
    const [platform, setPlatform] = useState(initialPlatform)

    useEffect(() => {
        setPlatform(initialPlatform)
    }, [initialPlatform])

    useEffect(() => {
        setGameName(initialGameName)
    }, [initialGameName])

    useEffect(() => {
        setTagLine(initialTagLine)
    }, [initialTagLine])


    function handleSubmit(event: FormEvent<HTMLFormElement>) {
        event.preventDefault()

        const trimmedGameName = gameName.trim()
        const trimmedTagLine = tagLine.trim()

        if (!trimmedGameName || !trimmedTagLine) {
            return
        }

        onSearch(platform, trimmedGameName, trimmedTagLine)
    }

    return (
        <form onSubmit={handleSubmit} className="w-full">
            <div
                className="flex flex-col overflow-hidden rounded-3xl border border-slate-800 bg-slate-900 shadow-[0_12px_40px_rgba(0,0,0,0.25)] md:flex-row md:items-stretch">
                <div className="border-b border-slate-800 px-4 py-3 md:w-[150px] md:border-b-0 md:border-r">
                    <label
                        htmlFor="platform"
                        className="mb-1 block text-xs font-bold uppercase tracking-wide text-slate-400"
                    >
                        Región
                    </label>

                    <select
                        id="platform"
                        className="w-full bg-transparent text-sm font-medium text-slate-100 outline-none"
                        value={platform}
                        onChange={(event) => setPlatform(event.target.value)}
                    >
                        {PLATFORMS.map((platformOption) => (
                            <option key={platformOption} value={platformOption} className="bg-slate-900">
                                {platformOption}
                            </option>
                        ))}
                    </select>
                </div>

                <div className="border-b border-slate-800 px-4 py-3 md:flex-1 md:border-b-0 md:border-r">
                    <label
                        htmlFor="gameName"
                        className="mb-1 block text-xs font-bold uppercase tracking-wide text-slate-400"
                    >
                        Buscar
                    </label>

                    <input
                        id="gameName"
                        className="w-full bg-transparent text-base text-slate-100 outline-none placeholder:text-slate-500"
                        placeholder="Nombre en el juego"
                        autoComplete="off"
                        value={gameName}
                        onChange={(event) => setGameName(event.target.value)}
                    />
                </div>

                <div className="border-b border-slate-800 px-4 py-3 md:w-[160px] md:border-b-0 md:border-r">
                    <label
                        htmlFor="tagLine"
                        className="mb-1 block text-xs font-bold uppercase tracking-wide text-slate-400"
                    >
                        Tag
                    </label>

                    <input
                        id="tagLine"
                        className="w-full bg-transparent text-base text-slate-100 outline-none placeholder:text-slate-500"
                        placeholder="EUW"
                        autoComplete="off"
                        value={tagLine}
                        onChange={(event) => setTagLine(event.target.value)}
                    />
                </div>

                <button
                    type="submit"
                    className="flex items-center justify-center bg-cyan-500 px-6 py-4 text-sm font-bold text-slate-950 transition hover:bg-cyan-400 md:px-7"
                >
                    Buscar
                </button>
            </div>
        </form>
    )
}

export default PlayerSearchBar
