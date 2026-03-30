import CurrentGameStatusCard from '../CurrentGameStatusCard.tsx'
import type { CurrentGameDto } from '../../types/api.ts'

type PlayerSidebarProps = {
    currentGame: CurrentGameDto | null
    loadingCurrentGame: boolean
    currentGameError: string | null
    championMap: Map<number, string> | null
}

function PlayerSidebar({
                           currentGame,
                           loadingCurrentGame,
                           currentGameError,
                           championMap,
                       }: PlayerSidebarProps) {
    return (
        <div className="space-y-6">
            <CurrentGameStatusCard
                currentGame={currentGame}
                loading={loadingCurrentGame}
                error={currentGameError}
                championMap={championMap}
            />

            <aside className="rounded-2xl border border-slate-800 bg-slate-900 p-4 h-fit space-y-4">
                <div className="flex items-center justify-between">
                    <h2 className="w-full text-center text-xl font-bold">Estadísticas</h2>
                </div>

                <div className="rounded-2xl border border-dashed border-slate-700 bg-slate-950/60 p-4">
                    <p className="text-sm text-slate-400">
                        Futuras versiones
                    </p>
                </div>
            </aside>
        </div>
    )
}

export default PlayerSidebar
