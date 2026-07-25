const PROFESSIONAL_ROLES: Record<string, string> = {
    Myrwn: 'Top',
    Elyoya: 'Jungla',
    jojopyun: 'Mid',
    Supa: 'Bot',
    Alvaro: 'Support',
    BrokenBlade: 'Top',
    SkewMond: 'Jungla',
    Caps: 'Mid',
    'Hans sama': 'Bot',
    Labrov: 'Support',
    Empyros: 'Top',
    Razork: 'Jungla',
    Vladi: 'Mid',
    Upset: 'Bot',
    Lospa: 'Support',
    Lot: 'Top',
    ISMA: 'Jungla',
    Jackies: 'Mid',
    Noah: 'Bot',
    Jun: 'Support',
    Canna: 'Top',
    Yike: 'Jungla',
    kyeahoo: 'Mid',
    Caliste: 'Bot',
    Busio: 'Support',
    Maynter: 'Top',
    Rhilech: 'Jungla',
    Poby: 'Mid',
    SamD: 'Bot',
    Parus: 'Support',
    'Naak Nako': 'Top',
    Lyncas: 'Jungla',
    Humanoid: 'Mid',
    Carzzy: 'Bot',
    Fleshy: 'Support',
}

const TEAM_LOGO_SLUGS: Record<string, string> = {
    KOI: 'koi',
    G2: 'g2',
    Fnatic: 'fnatic',
    GIANTX: 'giantx',
    KC: 'kc',
    NAVI: 'navi',
    Vitality: 'vitality',
}

export function professionalRole(proName: string) {
    return PROFESSIONAL_ROLES[proName] ?? 'Rol pendiente'
}

export function professionalRoleOrder(proName: string) {
    const roleOrder = ['Top', 'Jungla', 'Mid', 'Bot', 'Support']
    const index = roleOrder.indexOf(professionalRole(proName))
    return index === -1 ? roleOrder.length : index
}

export function teamLogoUrl(teamName: string) {
    const slug = TEAM_LOGO_SLUGS[teamName] ?? teamName.toLowerCase().replaceAll(' ', '-')
    return `/team-logos/${slug}.webp`
}
