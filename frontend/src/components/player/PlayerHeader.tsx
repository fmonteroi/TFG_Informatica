import { getProfileIconUrl } from '../../lib/dragontail.ts'
import { formatDate } from '../../lib/format.ts'
import { CARD_CLASS } from '../../lib/constants.ts'
import type { PlayerDto } from '../../types/api.ts'

type PlayerHeaderProps = {
    player: PlayerDto
    refreshing: boolean
    onRefresh: () => void
}

function PlayerHeader({ player, refreshing, onRefresh }: PlayerHeaderProps) {
    return (
        <section className={`${CARD_CLASS} flex flex-col gap-4 lg:flex-row lg:items-center lg:justify-between`}>
            <div className="flex items-center gap-4">
                <img
                    src={getProfileIconUrl(player.profileIconId)}
                    alt="Profile icon"
                    className="h-20 w-20 rounded-2xl"
                />

                <div>
                    <h1 className="text-3xl font-black">
                        {player.gameName}#{player.tagLine}
                    </h1>

                    <p className="text-slate-400">
                        {player.platform} · Nivel {player.summonerLevel}
                    </p>

                    <p className="text-sm text-slate-500">
                        Última sincronización:{' '}
                        {player.lastSyncAt ? formatDate(player.lastSyncAt) : 'Sin sincronizar'}
                    </p>
                </div>
            </div>

            <button
                onClick={onRefresh}
                className="rounded-xl bg-cyan-500 px-4 py-3 font-bold text-slate-950 transition hover:bg-cyan-400"
            >
                {refreshing ? 'Refrescando...' : 'Refrescar historial'}
            </button>
        </section>
    )
}

export default PlayerHeader
