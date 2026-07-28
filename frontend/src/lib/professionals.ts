const TEAM_LOGO_SLUGS: Record<string, string> = {
    KOI: 'koi',
    G2: 'g2',
    Fnatic: 'fnatic',
    GIANTX: 'giantx',
    KC: 'kc',
    NAVI: 'navi',
    Vitality: 'vitality',
}

export function teamLogoUrl(teamName: string) {
    let slug = TEAM_LOGO_SLUGS[teamName]

    if (!slug) {
        slug = teamName.toLowerCase().replaceAll(' ', '-')
    }

    return `/team-logos/${slug}.webp`
}
