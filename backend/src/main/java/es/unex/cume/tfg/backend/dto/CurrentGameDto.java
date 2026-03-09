package es.unex.cume.tfg.backend.dto;

public record CurrentGameDto(
        boolean inGame,
        Integer queueId,
        String queueName,
        Long gameLengthSeconds,
        Integer championId,
        String championName
) {
    public static CurrentGameDto notInGame(){
        return new CurrentGameDto(false, null, null, null, null, null);
    }

    public static CurrentGameDto inGame(Integer queueId, String queueName, Long gameLengthSeconds, Integer championId, String championName) {
        return new CurrentGameDto(true, queueId, queueName, gameLengthSeconds, championId, championName);
    }

}
