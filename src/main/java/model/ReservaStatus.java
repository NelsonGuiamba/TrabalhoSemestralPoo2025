package model;
// Activa → ainda não chegou a hora de terminar (dataHoraFim > agora) e não foi cancelada.
//
//Finalizada → passou o horário (dataHoraFim <= agora).
//
//Cancelada → usuário cancelou explicitamente.
public enum ReservaStatus {
    ACTIVA,
    FINALIZADA,
    CANCELADA
}
