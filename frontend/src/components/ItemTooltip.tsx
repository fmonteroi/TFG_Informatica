import { useEffect, useRef, useState } from 'react'
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
    const [isOpen, setIsOpen] = useState(false)
    const containerRef = useRef<HTMLDivElement>(null)

    useEffect(() => {
        if (!isOpen) {
            return
        }

        function closeWhenClickingOutside(event: PointerEvent) {
            if (!containerRef.current?.contains(event.target as Node)) {
                setIsOpen(false)
            }
        }

        function closeWhenPressingEscape(event: KeyboardEvent) {
            if (event.key === 'Escape') {
                setIsOpen(false)
            }
        }

        function closeWhenResizing() {
            setIsOpen(false)
        }

        document.addEventListener('pointerdown', closeWhenClickingOutside)
        document.addEventListener('keydown', closeWhenPressingEscape)
        window.addEventListener('resize', closeWhenResizing)

        return () => {
            document.removeEventListener('pointerdown', closeWhenClickingOutside)
            document.removeEventListener('keydown', closeWhenPressingEscape)
            window.removeEventListener('resize', closeWhenResizing)
        }
    }, [isOpen])

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
        <div ref={containerRef} className="group relative">
            <button
                type="button"
                aria-label={`Ver información de ${imageAlt}`}
                aria-expanded={isOpen}
                onClick={() => {
                    if (window.matchMedia('(max-width: 639px)').matches) {
                        setIsOpen(!isOpen)
                    }
                }}
                className="block"
            >
                <img
                    src={imageUrl}
                    alt={imageAlt}
                    className={`${sizeClassName} ${roundedClassName} shrink-0`}
                />
            </button>

            {itemInfo && (
                <div
                    role="tooltip"
                    className={`pointer-events-none fixed inset-x-4 bottom-4 z-50 rounded-2xl border border-slate-700 bg-slate-950 p-4 shadow-2xl sm:absolute sm:inset-x-auto sm:bottom-full sm:left-1/2 sm:mb-3 sm:hidden sm:w-72 sm:-translate-x-1/2 sm:group-hover:block ${isOpen ? 'block' : 'hidden'}`}
                >
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
