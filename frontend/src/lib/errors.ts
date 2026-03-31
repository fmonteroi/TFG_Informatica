export function safeError(error: unknown) {
    if (error instanceof Error) {
        return error.message
    }

    return 'Ha ocurrido un error desconocido'
}

export function getPlayerSearchErrorMessage(error: unknown) {
    const message = safeError(error)
    const normalizedMessage = message.toLowerCase()

    if (
        normalizedMessage.includes('404') ||
        normalizedMessage.includes('not found') ||
        normalizedMessage.includes('data not found')
    ) {
        return 'El jugador buscado no existe'
    }

    if (
        normalizedMessage.includes('429') ||
        normalizedMessage.includes('rate limit') ||
        normalizedMessage.includes('límite de riot') ||
        normalizedMessage.includes('inténtalo de nuevo')
    ) {
        return message
    }

    return 'Ha ocurrido un error desconocido'
}


