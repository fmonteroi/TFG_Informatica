type StatItem = {
    label: string
    value: string | number
    tone?: 'default' | 'positive' | 'negative' | 'accent'
}

type StatGridProps = {
    items: StatItem[]
}

function valueClass(tone: StatItem['tone']) {
    if (tone === 'positive') return 'text-emerald-300'
    if (tone === 'negative') return 'text-rose-300'
    if (tone === 'accent') return 'text-cyan-300'
    return 'text-slate-100'
}

function StatGrid({ items }: StatGridProps) {
    return (
        <dl className="grid grid-cols-2 gap-px overflow-hidden rounded-lg border border-slate-800 bg-slate-800">
            {items.map((item) => (
                <div key={item.label} className="bg-slate-950/70 p-3">
                    <dt className="text-xs font-semibold uppercase text-slate-500">
                        {item.label}
                    </dt>
                    <dd className={`mt-1 text-lg font-bold ${valueClass(item.tone)}`}>
                        {item.value}
                    </dd>
                </div>
            ))}
        </dl>
    )
}

export default StatGrid
