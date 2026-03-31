export function safeError(error: unknown) {
    if (error instanceof Error) {
        return error.message
    }

    return 'Ha ocurrido un error desconocido'
}

export function getPlayerSearchErrorMessage(error: unknown) {
    const message = safeError(error).toLowerCase()

    if (
        message.includes('404') ||
        message.includes('not found') ||
        message.includes('data not found')
    ) {
        return 'El jugador buscado no existe'
    }

    return 'Ha ocurrido un error desconocido'
}
