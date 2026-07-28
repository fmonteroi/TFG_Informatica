import type {ReactNode} from 'react'
import type {ItemInfo} from '../lib/dragontail'
import ItemTooltip from './ItemTooltip'

type ParticipationCardProps = {
    tone?: 'win' | 'loss' | 'neutral'
    topLeft: ReactNode
    topRight: ReactNode
    championIcon: string | null
    championName: string | null
    summaryLine: string
    spellIds: Array<number | null>
    spellMap: Map<number, string> | null
    mainItemIds: Array<number | null>
    trinketItemId: number | null
    specialItemId?: number | null
    showSpecialItem?: boolean
    itemInfoMap: Map<number, ItemInfo> | null
    expanded?: boolean
    onToggle?: () => void
    children?: ReactNode
}


function toneCardClass(tone: 'win' | 'loss' | 'neutral') {
    if (tone === 'win') {
        return 'border-emerald-500/30 bg-emerald-950/40'
    }

    if (tone === 'loss') {
        return 'border-rose-500/30 bg-rose-950/40'
    }

    return 'border-slate-700 bg-slate-900/80'
}

function ParticipationCard({
                               tone = 'neutral',
                               topLeft,
                               topRight,
                               championIcon,
                               championName,
                               summaryLine,
                               spellIds,
                               spellMap,
                               mainItemIds,
                               trinketItemId,
                               specialItemId = null,
                               showSpecialItem = false,
                               itemInfoMap,
                               expanded = false,
                               onToggle,
                               children,
                           }: ParticipationCardProps) {
    let championIconContent

    if (championIcon) {
        championIconContent = (
            <img
                src={championIcon}
                alt={championName ?? 'Champion'}
                className="h-20 w-20 rounded-2xl"
            />
        )
    } else {
        championIconContent = (
            <div
                role="img"
                aria-label="Icono de campeón no disponible"
                className="h-20 w-20 rounded-2xl border border-slate-700 bg-slate-800"
            />
        )
    }

    let toggleButtonLabel = 'Mostrar detalle'
    let toggleIconClass = 'rotate-0'

    if (expanded) {
        toggleButtonLabel = 'Ocultar detalle'
        toggleIconClass = 'rotate-180'
    }

    return (
        <article
            className={[
                'rounded-3xl border',
                toneCardClass(tone),
            ].join(' ')}
        >
            <div className="space-y-5 p-5">
                <div className="flex flex-wrap items-center justify-between gap-3">
                    <div>{topLeft}</div>
                    <div>{topRight}</div>
                </div>

                <div className="flex flex-col gap-4 lg:flex-row lg:items-start lg:justify-between">
                    <div className="flex min-w-0 items-start gap-4">
                        {championIconContent}

                        <div className="flex min-w-0 flex-wrap items-center gap-8">
                            <div className="min-w-0">
                                <p className="text-2xl font-bold text-slate-50">
                                    {championName}
                                </p>

                                <p className="text-sm text-slate-300">
                                    {summaryLine}
                                </p>
                            </div>

                            <div className="flex gap-2">
                                {spellIds.map((spellId, index) => {
                                    let spellIcon = null

                                    if (spellId != null && spellMap != null) {
                                        spellIcon = spellMap.get(spellId)
                                    }

                                    if (!spellIcon) {
                                        return (
                                            <div
                                                key={`card-spell-empty-${index}`}
                                                aria-hidden="true"
                                                className="h-12 w-12 rounded-xl border border-slate-700 bg-slate-800"
                                            />
                                        )
                                    }

                                    return (
                                        <img
                                            key={`card-spell-${index}`}
                                            src={spellIcon}
                                            alt={`Summoner spell ${spellId}`}
                                            className="h-12 w-12 rounded-xl"
                                        />
                                    )
                                })}
                            </div>
                        </div>
                    </div>
                </div>

                <div className="flex items-end justify-between gap-4">
                    <div className="flex flex-wrap items-end gap-2">
                        {mainItemIds.map((itemId, index) => (
                            <ItemTooltip
                                key={`card-item-${index}`}
                                itemId={itemId}
                                itemInfoMap={itemInfoMap}
                                sizeClassName="h-14 w-14"
                                roundedClassName="rounded-2xl"
                            />
                        ))}

                        {showSpecialItem && specialItemId && (
                            <>
                                <div className="h-14 w-4"/>
                                <ItemTooltip
                                    itemId={specialItemId}
                                    itemInfoMap={itemInfoMap}
                                    sizeClassName="h-14 w-14"
                                    roundedClassName="rounded-2xl"
                                />
                            </>
                        )}


                        <div className="h-14 w-4"/>

                        <ItemTooltip
                            itemId={trinketItemId}
                            itemInfoMap={itemInfoMap}
                            sizeClassName="h-14 w-14"
                            roundedClassName="rounded-2xl"
                        />

                    </div>

                    {onToggle && (
                        <button
                            type="button"
                            onClick={onToggle}
                            className="flex h-14 w-14 items-center justify-center rounded-2xl bg-slate-950/80 text-slate-100 transition hover:bg-slate-900"
                            aria-expanded={expanded}
                            aria-label={toggleButtonLabel}
                        >
                            <svg
                                viewBox="0 0 20 20"
                                fill="none"
                                aria-hidden="true"
                                className={[
                                    'h-6 w-6 transition-transform duration-200',
                                    toggleIconClass,
                                ].join(' ')}
                            >
                                <path
                                    d="M5 7.5L10 12.5L15 7.5"
                                    stroke="currentColor"
                                    strokeWidth="2"
                                    strokeLinecap="round"
                                    strokeLinejoin="round"
                                />
                            </svg>
                        </button>
                    )}
                </div>
            </div>

            {expanded && children && (
                <div className="border-t border-slate-800 bg-slate-900/80 p-5">
                    {children}
                </div>
            )}
        </article>
    )
}

export default ParticipationCard
