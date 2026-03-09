package es.unex.cume.tfg.backend.service;

import es.unex.cume.tfg.backend.dto.CurrentGameDto;

public interface CurrentGameService {
    CurrentGameDto findCurrentGame(String puuid);
}