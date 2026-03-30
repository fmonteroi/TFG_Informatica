import type {CurrentGameDto} from '../types/api'

type CurrentGameStatusCardProps = {
    currentGame: CurrentGameDto | null
    loading: boolean
    error?: string | null
    championMap: Map<number, string> | null
}

function statusDotClass(inGame: boolean) {
    return inGame ? 'bg-emerald-400' : 'bg-rose-400'
}

function formatGameLength(seconds: number | null) {
    if (seconds == null || seconds < 0) {
        return '00:00'
    }

    const minutes = Math.floor(seconds / 60)
    const remainingSeconds = seconds % 60

    return `${String(minutes).padStart(2, '0')}:${String(remainingSeconds).padStart(2, '0')}`
}

function CurrentGameStatusCard({
                                   currentGame,
                                   loading,
                                   error = null,
                                   championMap,
                               }: CurrentGameStatusCardProps) {
    if (loading) {
        return (
            <section className="rounded-2xl border border-slate-800 bg-slate-900 p-4">
                <p className="text-sm text-slate-400">Comprobando estado de partida...</p>
            </section>
        )
    }

    if (error) {
        return (
            <section className="rounded-2xl border border-slate-800 bg-slate-900 p-4">
                <p className="text-sm text-slate-400">No se pudo comprobar el estado actual.</p>
                <p className="mt-2 text-xs text-rose-300">{error}</p>
            </section>
        )
    }


    if (!currentGame) {
        return (
            <section className="rounded-2xl border border-slate-800 bg-slate-900 p-4">
                <p className="text-sm text-slate-400">No hay información disponible.</p>
            </section>
        )
    }

    if (currentGame.hidden) {
        return (
            <section className="rounded-2xl border border-slate-800 bg-slate-900 p-4">
                <p className="text-sm text-slate-400">Estado oculto.</p>
            </section>
        )
    }


    const championIcon =
        currentGame.championId != null ? championMap?.get(currentGame.championId) ?? null : null

    return (
        <section className="rounded-2xl border border-slate-800 bg-slate-900 p-5">
            <div className="flex items-center justify-between gap-3">
                <div className="flex items-center gap-3">
                    <span className={`h-3 w-3 rounded-full ${statusDotClass(currentGame.inGame)}`}/>

                    <p className="font-semibold text-slate-100">
                        {currentGame.inGame ? 'En partida' : 'Desconectado'}
                    </p>
                </div>

                {currentGame.inGame && (
                    <p className="text-sm font-semibold text-slate-200">
                        {formatGameLength(currentGame.gameLengthSeconds)}
                    </p>
                )}
            </div>

            {currentGame.inGame && (
                <div className="mt-5 space-y-4">
                    <div>
                        <p className="font-medium rounded-xl tracking-wide text-slate-100">
                            Campeón actual
                        </p>
                    </div>

                    <div className="flex items-center gap-4">
                        {championIcon ? (
                            <img
                                src={championIcon}
                                alt={currentGame.championName ?? 'Champion'}
                                className="h-16 w-16 rounded-2xl"
                            />
                        ) : (
                            <div className="h-20 w-20 rounded-2xl border border-slate-700 bg-slate-800"/>
                        )}

                        <div className="min-w-0">
                            <p className="text-2xl font-medium text-slate-100">
                                {currentGame.championName ?? 'Campeón desconocido'}
                            </p>
                        </div>
                    </div>

                    <div className="border-t border-slate-800 pt-4">
                        <span
                            className="inline-flex rounded-xl bg-slate-800 px-3 py-1.5 text-sm font-medium text-slate-100">
                            {currentGame.queueName ?? currentGame.queueId ?? 'Cola desconocida'}
                        </span>
                    </div>
                </div>
            )}
        </section>
    )
}

export default CurrentGameStatusCard
