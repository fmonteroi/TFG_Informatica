import type { RecommendedBuildDto } from '../../types/api'
import type { ItemInfo } from '../../lib/dragontail'
import BuildLoadout from '../BuildLoadout'

type RecommendedBuildCardProps = {
    build: RecommendedBuildDto | null
    spellMap: Map<number, string> | null
    itemInfoMap: Map<number, ItemInfo> | null
}

function RecommendedBuildCard({ build, spellMap, itemInfoMap }: RecommendedBuildCardProps) {
    return (
        <section className="rounded-lg border border-cyan-400/35 bg-cyan-400/5 p-5">
            <div className="mb-5 flex flex-wrap items-start justify-between gap-3">
                <div>
                    <p className="text-xs font-semibold uppercase text-cyan-300">Recomendación</p>
                    <h2 className="mt-1 text-xl font-bold">Build recomendada</h2>
                </div>
                <span className="rounded-md bg-cyan-400/15 px-2 py-1 text-xs font-semibold text-cyan-200">
                    Datos ranked
                </span>
            </div>

            {build ? (
                <BuildLoadout
                    build={build}
                    spellMap={spellMap}
                    itemInfoMap={itemInfoMap}
                    showRoleBoundItem
                />
            ) : (
                <p className="text-sm text-slate-400">
                    Aún no hay suficientes datos para generar una recomendación.
                </p>
            )}
        </section>
    )
}

export default RecommendedBuildCard
