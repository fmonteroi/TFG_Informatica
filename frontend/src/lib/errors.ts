/**
 * Converts unknown caught values into safe UI text.
 */
export function safeError(error: unknown) {
    if (error instanceof Error) {
        return error.message
    }

    return 'Ha ocurrido un error desconocido'
}
