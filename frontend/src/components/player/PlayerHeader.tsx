import { getProfileIconUrl } from '../../lib/dragontail'
import { formatDate } from '../../lib/format'
import type { PlayerDto } from '../../types/api'

type PlayerHeaderProps = {
    player: PlayerDto
    dataDragonVersion: string | null
    refreshing: boolean
    onRefresh: () => void
}

function PlayerHeader({ player, dataDragonVersion, refreshing, onRefresh }: PlayerHeaderProps) {
    return (
        <section className="flex flex-col gap-4 border-b border-slate-800 pb-6 lg:flex-row lg:items-center lg:justify-between">
            <div className="flex items-center gap-4">
                {dataDragonVersion ? (
                    <img
                        src={getProfileIconUrl(player.profileIconId, dataDragonVersion)}
                        alt={`Icono de perfil de ${player.gameName}`}
                        className="h-20 w-20 rounded-lg border border-slate-700"
                    />
                ) : (
                    <div
                        role="img"
                        aria-label="Icono de perfil cargando"
                        className="h-20 w-20 rounded-lg border border-slate-700 bg-slate-800"
                    />
                )}

                <div>
                    <h1 className="text-3xl font-black">
                        {player.gameName}#{player.tagLine}
                    </h1>
                    <p className="mt-1 text-slate-400">
                        {player.platform} · Nivel {player.summonerLevel}
                    </p>
                    <p className="mt-1 text-sm text-slate-500">
                        Última sincronización:{' '}
                        {player.lastSyncAt ? formatDate(player.lastSyncAt) : 'Sin sincronizar'}
                    </p>
                </div>
            </div>

            <button
                type="button"
                onClick={onRefresh}
                disabled={refreshing}
                className="rounded-lg bg-cyan-400 px-4 py-3 font-bold text-slate-950 transition hover:bg-cyan-300 disabled:cursor-not-allowed disabled:opacity-60"
            >
                {refreshing ? 'Refrescando...' : 'Refrescar historial'}
            </button>
        </section>
    )
}

export default PlayerHeader
