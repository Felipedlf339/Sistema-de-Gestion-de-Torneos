package org.example.modelo;

import java.util.ArrayList;
import java.util.List;
import java.io.Serializable;


public class EliminatoriaDoble implements FormatoTorneo, Serializable {

    private List<Participante> WinnerBracket = new ArrayList<>();
    private List<Participante> LoserBracket  = new ArrayList<>();
    private Participante campeonGanadores;
    private Participante campeonPerdedores;
    private boolean enGranFinal = false;
    private boolean granFinalJugada = false;

    @Override
    public List<Partido> generarEnfrentamientos(List<Participante> participantes) {
        WinnerBracket = new ArrayList<>(participantes);
        LoserBracket = new ArrayList<>();
        List<Partido> partidos = new ArrayList<>();

        for(int i = 0; i + 1 < WinnerBracket.size(); i+= 2){
            partidos.add(new Partido(WinnerBracket.get(i), WinnerBracket.get(i+1)));
        }

        return partidos;
    }

    @Override
    public void actualizarClasificacion(Partido partido) {
        Resultado resultado = partido.getResultado();
        if(resultado == null) return;

        if(resultado.getPuntajeParticipanteA() == resultado.getPuntajeParticipanteB()){
            throw new IllegalStateException("En eliminatoria doble no puede haber empate.");
        }
        Participante ganador;
        Participante perdedor;

        if (resultado.ganoParticipanteA()) {
            ganador = partido.getParticipanteA();
            perdedor = partido.getParticipanteB();
        } else {
            ganador = partido.getParticipanteB();
            perdedor = partido.getParticipanteA();
        }

        if(enGranFinal){
            //El perdedor de la WinnerBracket nunca ha perdido
            // si gana el que viene de la LoserBracket hay revancha
            if(LoserBracket.contains(ganador) && !granFinalJugada){
                granFinalJugada = true; //Se juega revancha
            } else {
                campeonGanadores = ganador; //Campeon DEFINITIVO
            }
            return;
        }
        if(WinnerBracket.contains(perdedor)){
            WinnerBracket.remove(perdedor);
            LoserBracket.add(perdedor);
        } else {
            //Perdió por 2da vez, queda eliminado.
            LoserBracket.remove(perdedor);
        }
    }

    public List<Partido> generarSiguienteRonda(List<Partido> partidosAnteriores){
        List<Partido> siguientes = new ArrayList<>();

        //Separamos los ganadores y perdedores de la ronda anterior
        List<Participante> ganadoresRonda = new ArrayList<>();
        List<Participante> perdedoresRonda = new ArrayList<>();

        for(Partido p: partidosAnteriores){
            Resultado r = p.getResultado();
            if(r == null) continue;
            Participante ganador;
            Participante perdedor;

            if (r.ganoParticipanteA()) {
                ganador = p.getParticipanteA();
                perdedor = p.getParticipanteB();
            } else {
                ganador = p.getParticipanteB();
                perdedor = p.getParticipanteA();
            }
            ganadoresRonda.add(ganador);
            perdedoresRonda.add(perdedor);
        }

        //Actualizar los brackets
        WinnerBracket = ganadoresRonda;
        LoserBracket.addAll(perdedoresRonda);

        //Si queda un solo ganador y un solo perdedor, se pasa a la Gran Final
        if(WinnerBracket.size() == 1 && LoserBracket.size() == 1){
            enGranFinal = true;
            campeonGanadores = WinnerBracket.get(0);
            campeonPerdedores = WinnerBracket.get(0);
            siguientes.add(new Partido(campeonGanadores, campeonPerdedores));
            return siguientes;
        }

        //Revancha si la gran final la ganó alguien que venia de la LoserBracket
        if(granFinalJugada){
            siguientes.add(new Partido(campeonGanadores, campeonPerdedores));
            granFinalJugada = false;
            return siguientes;
        }

        //Partidos normales de WinnerBracket
        for(int i = 0; i + 1 < WinnerBracket.size(); i+= 2){
            siguientes.add(new Partido(WinnerBracket.get(i), WinnerBracket.get(i+1)));
        }

        //Partidos normales de LoserBracket
        for(int i = 0; i + 1 < LoserBracket.size(); i+= 2){
            siguientes.add(new Partido(LoserBracket.get(i), LoserBracket.get(i+1)));
        }
        return siguientes;
    }

    private boolean isEnGranFinal(){return enGranFinal;}

    public List<Participante> getWinnerBracket(){return WinnerBracket;}
    public List<Participante> getLoserBracket(){return LoserBracket;}


}


