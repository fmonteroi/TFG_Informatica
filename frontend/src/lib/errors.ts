export function safeError(error: unknown) {
    if (error instanceof Error) {
        return error.message
    }

    return 'Ha ocurrido un error desconocido'
}
