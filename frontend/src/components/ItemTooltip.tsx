import type { ItemInfo } from '../lib/dragontail'

type ItemTooltipProps = {
    itemId: number | null | undefined
    itemInfoMap: Map<number, ItemInfo> | null
    sizeClassName?: string
    roundedClassName?: string
    emptyClassName?: string
}

function ItemTooltip({
                         itemId,
                         itemInfoMap,
                         sizeClassName = 'h-10 w-10',
                         roundedClassName = 'rounded-xl',
                         emptyClassName = 'border border-slate-700 bg-slate-800',
                     }: ItemTooltipProps) {
    if (!itemId || itemId === 0) {
        return (
            <div
                aria-hidden="true"
                className={`${sizeClassName} ${roundedClassName} ${emptyClassName}`}
            />
        )
    }

    let itemInfo = null

    if (itemInfoMap) {
        itemInfo = itemInfoMap.get(itemId) ?? null
    }

    let imageUrl = `/dragontail/img/item/${itemId}.png`
    let imageAlt = `Item ${itemId}`

    if (itemInfo) {
        imageUrl = itemInfo.imageUrl
        imageAlt = itemInfo.name
    }

    return (
        <div className="group relative">
            <button
                type="button"
                aria-label={`Ver información de ${imageAlt}`}
                className="block"
            >
                <img
                    src={imageUrl}
                    alt={imageAlt}
                    className={`${sizeClassName} ${roundedClassName} shrink-0`}
                />
            </button>

            {itemInfo && (
                <div className="pointer-events-none absolute bottom-full left-1/2 z-50 mb-3 hidden w-72 -translate-x-1/2 rounded-2xl border border-slate-700 bg-slate-950 p-4 shadow-2xl group-hover:block group-focus-within:block">
                    <div className="mb-3 flex items-center gap-3">
                        <img
                            src={itemInfo.imageUrl}
                            alt={itemInfo.name}
                            className="h-12 w-12 rounded-xl"
                        />

                        <p className="font-bold text-slate-100">
                            {itemInfo.name}
                        </p>
                    </div>

                    <div
                        className="text-sm text-slate-200 [&_maintext]:text-slate-100 [&_stats]:text-cyan-300"
                        dangerouslySetInnerHTML={{ __html: itemInfo.description }}
                    />

                </div>
            )}
        </div>
    )
}

export default ItemTooltip
