import type { ItemInfo } from '../../lib/dragontail'
import type { RecommendedBuildDto, Role } from '../../types/api'
import BuildLoadout from '../BuildLoadout'

type RecommendedBuildCardProps = {
    build: RecommendedBuildDto | null
    role: Role | null
    spellMap: Map<number, string> | null
    itemInfoMap: Map<number, ItemInfo> | null
}

function RecommendedBuildCard({
    build,
    role,
    spellMap,
    itemInfoMap,
}: RecommendedBuildCardProps) {
    let buildContent

    if (build) {
        buildContent = (
            <BuildLoadout
                build={build}
                spellMap={spellMap}
                itemInfoMap={itemInfoMap}
                showRoleBoundItem={role === 'BOTTOM'}
            />
        )
    } else {
        buildContent = (
            <p className="text-sm text-slate-400">
                No hay suficientes partidas para generar una recomendación.
            </p>
        )
    }

    return (
        <section className="rounded-lg border border-cyan-400/35 bg-cyan-400/5 p-5">
            <h2 className="mb-5 text-xl font-bold">Build recomendada</h2>

            {buildContent}
        </section>
    )
}

export default RecommendedBuildCard
