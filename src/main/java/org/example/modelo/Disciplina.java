package org.example.modelo;

// Juegos base, guarda los límites de cuánta gente puede jugar dependiendo del deporte o videojuego.

public enum Disciplina {

    FUTBOL(11, 22),
    FUTBOLITO(7, 14),
    BABY_FUTBOL(5, 10),
    BASQUETBOL(5, 12),
    LEAGUE_OF_LEGENDS(5, 6),
    VALORANT(5, 6),
    AJEDREZ(1,1),
    TENIS(1,1),
    BOXEO(1,1),
    LIBRE(1, Integer.MAX_VALUE);

    private final int minJugadoresPorEquipo;
    private final int maxJugadoresPorEquipo;

    Disciplina(int minJugadoresPorEquipo, int maxJugadoresPorEquipo) {
        this.minJugadoresPorEquipo = minJugadoresPorEquipo;
        this.maxJugadoresPorEquipo = maxJugadoresPorEquipo;
    }

    public int getMinJugadoresPorEquipo() {
        return minJugadoresPorEquipo;
    }

    public int getMaxJugadoresPorEquipo() {
        return maxJugadoresPorEquipo;
    }
}
