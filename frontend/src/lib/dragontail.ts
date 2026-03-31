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

const DRAGONTAIL_VERSION = '16.6.1'
const DRAGONTAIL_BASE = `https://ddragon.leagueoflegends.com/cdn/${DRAGONTAIL_VERSION}`
const EN_DATA_BASE = `https://ddragon.leagueoflegends.com/cdn/${DRAGONTAIL_VERSION}/data/en_US`
const ES_DATA_BASE = `https://ddragon.leagueoflegends.com/cdn/${DRAGONTAIL_VERSION}/data/es_ES`
const IMG_BASE = `${DRAGONTAIL_BASE}/img`

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

async function loadChampionMap() {
    if (!championMapPromise) {
        championMapPromise = fetchStaticJson<ChampionStaticFile>(`${EN_DATA_BASE}/champion.json`).then((file) => {
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
        summonerSpellMapPromise = fetchStaticJson<SummonerStaticFile>(`${EN_DATA_BASE}/summoner.json`).then((file) => {
            const map = new Map<number, string>()

            Object.values(file.data).forEach((spell) => {
                map.set(Number(spell.key), `${IMG_BASE}/spell/${spell.image.full}`)
            })

            return map
        })
    }

    return summonerSpellMapPromise
}

async function loadItemInfoMap() {
    if (!itemInfoMapPromise) {
        itemInfoMapPromise = fetchStaticJson<ItemStaticFile>(`${ES_DATA_BASE}/item.json`).then((file) => {
            const map = new Map<number, ItemInfo>()

            Object.entries(file.data).forEach(([itemId, item]) => {
                map.set(Number(itemId), {
                    id: Number(itemId),
                    name: item.name,
                    description: item.description,
                    imageUrl: `${IMG_BASE}/item/${item.image.full}`,
                })
            })

            return map
        })
    }

    return itemInfoMapPromise
}

export function useDragontailAssets() {
    const [championMap, setChampionMap] = useState<Map<number, string> | null>(null)
    const [summonerSpellMap, setSummonerSpellMap] = useState<Map<number, string> | null>(null)
    const [itemInfoMap, setItemInfoMap] = useState<Map<number, ItemInfo> | null>(null)

    useEffect(() => {
        async function load() {
            const [loadedChampionMap, loadedSpellMap, loadedItemInfoMap] = await Promise.all([
                loadChampionMap(),
                loadSummonerSpellMap(),
                loadItemInfoMap(),
            ])

            setChampionMap(loadedChampionMap)
            setSummonerSpellMap(loadedSpellMap)
            setItemInfoMap(loadedItemInfoMap)
        }

        void load()
    }, [])

    return { championMap, summonerSpellMap, itemInfoMap }
}


export function getProfileIconUrl(profileIconId: number) {
    return `${IMG_BASE}/profileicon/${profileIconId}.png`
}
