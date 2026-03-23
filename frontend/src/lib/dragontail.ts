import { useEffect, useState } from 'react'

type ChampionStaticFile = {
    data: Record<string, { key: string; image: { full: string } }>
}

type SummonerStaticFile = {
    data: Record<string, { key: string; image: { full: string } }>
}

const DRAGONTAIL_BASE = '/dragontail'
const DATA_BASE = `${DRAGONTAIL_BASE}/data/en_US`
const IMG_BASE = `${DRAGONTAIL_BASE}/img`

let championMapPromise: Promise<Map<number, string>> | null = null
let summonerSpellMapPromise: Promise<Map<number, string>> | null = null

async function fetchStaticJson<T>(url: string): Promise<T> {
    const response = await fetch(url)
    if (!response.ok) throw new Error(`No se pudo cargar ${url}`)
    return response.json() as Promise<T>
}

async function loadChampionMap() {
    if (!championMapPromise) {
        championMapPromise = fetchStaticJson<ChampionStaticFile>(`${DATA_BASE}/champion.json`).then((file) => {
            const map = new Map<number, string>()
            Object.values(file.data).forEach((champion) => {
                map.set(Number(champion.key), `${IMG_BASE}/champion/${champion.image.full}`)
            })
            return map
        })
    }
    return championMapPromise
}

async function loadSummonerSpellMap() {
    if (!summonerSpellMapPromise) {
        summonerSpellMapPromise = fetchStaticJson<SummonerStaticFile>(`${DATA_BASE}/summoner.json`).then((file) => {
            const map = new Map<number, string>()
            Object.values(file.data).forEach((spell) => {
                map.set(Number(spell.key), `${IMG_BASE}/spell/${spell.image.full}`)
            })
            return map
        })
    }
    return summonerSpellMapPromise
}

export function useDragontailAssets() {
    const [championMap, setChampionMap] = useState<Map<number, string> | null>(null)
    const [summonerSpellMap, setSummonerSpellMap] = useState<Map<number, string> | null>(null)

    useEffect(() => {
        async function load() {
            const [loadedChampionMap, loadedSpellMap] = await Promise.all([
                loadChampionMap(),
                loadSummonerSpellMap(),
            ])
            setChampionMap(loadedChampionMap)
            setSummonerSpellMap(loadedSpellMap)
        }

        void load()
    }, [])

    return { championMap, summonerSpellMap }
}

export function getItemImageUrl(itemId: number | null | undefined) {
    if (!itemId || itemId === 0) return null
    return `${IMG_BASE}/item/${itemId}.png`
}

export function getProfileIconUrl(profileIconId: number) {
    return `${IMG_BASE}/profileicon/${profileIconId}.png`
}
