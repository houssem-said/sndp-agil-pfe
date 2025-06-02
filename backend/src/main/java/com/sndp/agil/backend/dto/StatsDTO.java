package com.sndp.agil.backend.dto;

/**
 * DTO pour exposer les statistiques globales (nombre total de tickets et de rendez-vous).
 */
public class StatsDTO {
    private long totalTickets;
    private long totalRendezVous;

    public StatsDTO() {
    }

    public StatsDTO(long totalTickets, long totalRendezVous) {
        this.totalTickets = totalTickets;
        this.totalRendezVous = totalRendezVous;
    }

    public long getTotalTickets() {
        return totalTickets;
    }

    public void setTotalTickets(long totalTickets) {
        this.totalTickets = totalTickets;
    }

    public long getTotalRendezVous() {
        return totalRendezVous;
    }

    public void setTotalRendezVous(long totalRendezVous) {
        this.totalRendezVous = totalRendezVous;
    }
}
