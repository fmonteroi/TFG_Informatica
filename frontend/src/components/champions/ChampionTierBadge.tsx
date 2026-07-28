import type { Tier } from '../../types/api'

type ChampionTierBadgeSize = 'small' | 'large'

type ChampionTierBadgeProps = {
    tier: Tier | null
    size: ChampionTierBadgeSize
    id?: string
}

const TIER_COLORS: Record<Tier, string> = {
    S: 'bg-red-600',
    A: 'bg-orange-500',
    B: 'bg-amber-500',
    C: 'bg-emerald-600',
    D: 'bg-sky-600',
    E: 'bg-slate-600',
}

const TIER_SIZES: Record<ChampionTierBadgeSize, string> = {
    small: 'h-10 w-10 text-xl font-bold',
    large: 'h-16 w-16 text-3xl font-semibold',
}

function ChampionTierBadge({ tier, size, id }: ChampionTierBadgeProps) {
    let displayedTier = tier

    if (displayedTier == null) {
        displayedTier = 'C'
    }

    return (
        <div
            id={id}
            aria-label={`Tier ${displayedTier}`}
            className={`flex shrink-0 items-center justify-center rounded-md text-white ${TIER_COLORS[displayedTier]} ${TIER_SIZES[size]}`}
        >
            {displayedTier}
        </div>
    )
}

export default ChampionTierBadge
