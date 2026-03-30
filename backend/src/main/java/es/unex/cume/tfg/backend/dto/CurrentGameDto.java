package es.unex.cume.tfg.backend.dto;

public record CurrentGameDto(
        boolean inGame,
        boolean hidden,
        Integer queueId,
        String queueName,
        Long gameLengthSeconds,
        Integer championId,
        String championName
) {
    public static CurrentGameDto notInGame() {
        return new CurrentGameDto(false, false, null, null, null, null, null);
    }

    public static CurrentGameDto hiddenGame() {
        return new CurrentGameDto(false, true, null, null, null, null, null);
    }

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
