import type { BuildDto } from '../types/api'
import type { ItemInfo } from '../lib/dragontail'
import ItemTooltip from './ItemTooltip'

type BuildLoadoutProps = {
    build: BuildDto
    spellMap: Map<number, string> | null
    itemInfoMap: Map<number, ItemInfo> | null
    showRoleBoundItem?: boolean
}

function BuildLoadout({
                          build,
                          spellMap,
                          itemInfoMap,
                          showRoleBoundItem = false,
                      }: BuildLoadoutProps) {
    const spellIds = [build.summoner1Id, build.summoner2Id]
    const mainItemIds = [
        build.item0,
        build.item1,
        build.item2,
        build.item3,
        build.item4,
        build.item5,
    ]

    return (
        <div className="flex flex-wrap items-end gap-4">
            <div>
                <p className="mb-2 text-xs font-semibold uppercase text-slate-500">Hechizos</p>
                <div className="flex gap-2">
                    {spellIds.map((spellId, index) => {
                        const icon = spellId ? spellMap?.get(spellId) : null

                        return icon ? (
                            <img
                                key={`${spellId}-${index}`}
                                src={icon}
                                alt={`Hechizo de invocador ${spellId}`}
                                className="h-11 w-11 rounded-lg"
                            />
                        ) : (
                            <div
                                key={`empty-spell-${index}`}
                                role="img"
                                aria-label="Hechizo no disponible"
                                className="h-11 w-11 rounded-lg border border-slate-700 bg-slate-800"
                            />
                        )
                    })}
                </div>
            </div>

            <div className="min-w-0 flex-1">
                <p className="mb-2 text-xs font-semibold uppercase text-slate-500">Objetos</p>
                <div className="flex flex-wrap gap-2">
                    {mainItemIds.map((itemId, index) => (
                        <ItemTooltip
                            key={`loadout-item-${index}`}
                            itemId={itemId}
                            itemInfoMap={itemInfoMap}
                            sizeClassName="h-11 w-11"
                            roundedClassName="rounded-lg"
                        />
                    ))}

                    {showRoleBoundItem && (
                        <ItemTooltip
                            itemId={build.roleBoundItem}
                            itemInfoMap={itemInfoMap}
                            sizeClassName="h-11 w-11"
                            roundedClassName="rounded-lg"
                        />
                    )}

                    <span className="mx-1 h-11 w-px bg-slate-700" aria-hidden="true" />

                    <ItemTooltip
                        itemId={build.item6}
                        itemInfoMap={itemInfoMap}
                        sizeClassName="h-11 w-11"
                        roundedClassName="rounded-lg"
                    />
                </div>
            </div>
        </div>
    )
}

export default BuildLoadout
