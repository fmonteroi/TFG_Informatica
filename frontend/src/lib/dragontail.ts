import { useEffect, useState } from 'react'

type ChampionStaticFile = {
    data: Record<string, { key: string; image: { full: string } }>
}

type SummonerStaticFile = {
    data: Record<string, { key: string; image: { full: string } }>
}

type ItemStaticFile = {
    data: Record<
        string,
        {
            name: string
            description: string
            image: { full: string }
        }
    >
}

export type ItemInfo = {
    id: number
    name: string
    description: string
    imageUrl: string
}

const DRAGONTAIL_HOST = 'https://ddragon.leagueoflegends.com'
const VERSIONS_URL = `${DRAGONTAIL_HOST}/api/versions.json`

let versionPromise: Promise<string> | null = null
let championMapPromise: Promise<Map<number, string>> | null = null
let summonerSpellMapPromise: Promise<Map<number, string>> | null = null
let itemInfoMapPromise: Promise<Map<number, ItemInfo>> | null = null

async function fetchStaticJson<T>(url: string): Promise<T> {
    const response = await fetch(url)
    if (!response.ok) {
        throw new Error(`No se pudo cargar ${url}`)
    }

    return response.json() as Promise<T>
}

async function loadDataDragonVersion() {
    if (!versionPromise) {
        versionPromise = fetchStaticJson<string[]>(VERSIONS_URL).then((versions) => {
            const latestVersion = versions[0]

            if (!latestVersion) {
                throw new Error('Data Dragon no devolvió ninguna versión')
            }

            return latestVersion
        })
    }

    return versionPromise
}

function dataDragonBases(version: string) {
    const base = `${DRAGONTAIL_HOST}/cdn/${version}`

    return {
        englishData: `${base}/data/en_US`,
        spanishData: `${base}/data/es_ES`,
        images: `${base}/img`,
    }
}

async function loadChampionMap() {
    if (!championMapPromise) {
        championMapPromise = loadDataDragonVersion().then(async (version) => {
            const bases = dataDragonBases(version)
            const file = await fetchStaticJson<ChampionStaticFile>(
                `${bases.englishData}/champion.json`,
            )
            const map = new Map<number, string>()

            Object.values(file.data).forEach((champion) => {
                map.set(
                    Number(champion.key),
                    `${bases.images}/champion/${champion.image.full}`,
                )
            })

            return map
        })
    }

    return championMapPromise
}

async function loadSummonerSpellMap() {
    if (!summonerSpellMapPromise) {
        summonerSpellMapPromise = loadDataDragonVersion().then(async (version) => {
            const bases = dataDragonBases(version)
            const file = await fetchStaticJson<SummonerStaticFile>(
                `${bases.englishData}/summoner.json`,
            )
            const map = new Map<number, string>()

            Object.values(file.data).forEach((spell) => {
                map.set(Number(spell.key), `${bases.images}/spell/${spell.image.full}`)
            })

            return map
        })
    }

    return summonerSpellMapPromise
}

async function loadItemInfoMap() {
    if (!itemInfoMapPromise) {
        itemInfoMapPromise = loadDataDragonVersion().then(async (version) => {
            const bases = dataDragonBases(version)
            const file = await fetchStaticJson<ItemStaticFile>(
                `${bases.spanishData}/item.json`,
            )
            const map = new Map<number, ItemInfo>()

            Object.entries(file.data).forEach(([itemId, item]) => {
                map.set(Number(itemId), {
                    id: Number(itemId),
                    name: item.name,
                    description: item.description,
                    imageUrl: `${bases.images}/item/${item.image.full}`,
                })
            })

            return map
        })
    }

    return itemInfoMapPromise
}

/**
 * Loads and exposes cached Data Dragon asset maps.
 */
export function useDragontailAssets() {
    const [dataDragonVersion, setDataDragonVersion] = useState<string | null>(null)
    const [championMap, setChampionMap] = useState<Map<number, string> | null>(null)
    const [summonerSpellMap, setSummonerSpellMap] = useState<Map<number, string> | null>(null)
    const [itemInfoMap, setItemInfoMap] = useState<Map<number, ItemInfo> | null>(null)

    useEffect(() => {
        let cancelled = false

        async function load() {
            const [loadedVersion, loadedChampionMap, loadedSpellMap, loadedItemInfoMap] =
                await Promise.all([
                    loadDataDragonVersion(),
                    loadChampionMap(),
                    loadSummonerSpellMap(),
                    loadItemInfoMap(),
                ])

            if (!cancelled) {
                setDataDragonVersion(loadedVersion)
                setChampionMap(loadedChampionMap)
                setSummonerSpellMap(loadedSpellMap)
                setItemInfoMap(loadedItemInfoMap)
            }
        }

        void load()

        return () => {
            cancelled = true
        }
    }, [])

    return { dataDragonVersion, championMap, summonerSpellMap, itemInfoMap }
}


/**
 * Builds a Data Dragon profile icon URL.
 */
export function getProfileIconUrl(profileIconId: number, version: string) {
    return `${dataDragonBases(version).images}/profileicon/${profileIconId}.png`
}
