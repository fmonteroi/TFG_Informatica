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
            <h2 className="mb-5 text-xl font-bold">Build recomendada</h2>

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
