package es.unex.cume.tfg.backend.dto;

/**
 * DTO that represents a player's current game status.
 */
public record CurrentGameDto(
        boolean inGame,
        boolean hidden,
        Integer queueId,
        String queueName,
        Long gameLengthSeconds,
        Integer championId,
        String championName
) {
    /**
     * Creates a response for a player that is not currently in a game.
     *
     * @return the current game DTO.
     */
    public static CurrentGameDto notInGame() {
        return new CurrentGameDto(false, false, null, null, null, null, null);
    }

    /**
     * Creates a response for a game whose participant data cannot be resolved.
     *
     * @return the current game DTO.
     */
    public static CurrentGameDto hiddenGame() {
        return new CurrentGameDto(false, true, null, null, null, null, null);
    }

    /**
     * Creates a response for a player that is currently in a game.
     *
     * @param queueId the Riot queue ID.
     * @param queueName the readable queue name.
     * @param gameLengthSeconds the current game duration in seconds.
     * @param championId the champion ID used by the player.
     * @param championName the champion name used by the player.
     * @return the current game DTO.
     */
    public static CurrentGameDto inGame(
            Integer queueId,
            String queueName,
            Long gameLengthSeconds,
            Integer championId,
            String championName
    ) {
        return new CurrentGameDto(true, false, queueId, queueName, gameLengthSeconds, championId, championName);
    }
}
