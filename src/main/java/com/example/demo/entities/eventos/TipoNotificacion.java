package com.example.demo.entities.eventos;

public enum TipoNotificacion {
    // Postulaciones
    SOLICITUD_POSTULACION_OFERTA(
        "Nueva solicitud de postulación en una oferta",
        "El candidato {candidato} ha solicitado participar en la oferta {oferta}."
    ),

    SOLICITUD_POSTULACION_OFERTA_ACEPTADA(
        "¡Tu solicitud de postulación ha sido aceptada!",
        "Tu postulación solicitada el dia {fecha} ha sido aceptada. Ahora estas participando para el puesto {oferta} en {empresa}."
    ),

    CAMBIO_ETAPA_POSTULACION(
    "Actualización de postulación",
    "Tu postulación para {oferta} en {empresa} ha cambiado a la etapa {etapa}."
    ),

    CAMBIO_ETAPA_POSTULACION_EMPRESA(
    "Actualización de postulación",
    "{candidato} ha avanzado en su postulación para {oferta}."
    ),

    // Invitaciones
    INVITACION_OFERTA(
        "¡Han solicitado tu participación en una oferta!",
        "La {empresa} ha solicitado tu participación en la oferta de {oferta}."
    ),

    INVITACION_ACEPTADA_EMPRESA(
        "¡Tu solicitud de postulación ha sido aceptada!",
        "Tu postulación solicitada a {candidato} ha sido acepatada. Ahora {candidato} esta participando para el puesto {oferta}."
    ),
    
    INVITACION_ACEPTADA_CANDIDATO(
        "¡Has aceptado la invitación a postular!",
        "Has aceptado la invitación para postular en la oferta {oferta} de {empresa}. ¡Buena suerte!"
    ),

    INVITACION_RECHAZADA(
        "La invitación a postular ha sido rechazada",
        "Lamentablemente, {candidato} ha rechazado tu invitación para participar en la postulación para {oferta}."
    ),

    CANDIDATO_SELECCIONADO(
        "¡Has sido seleccionado!",
        "¡Felicidades! Has sido seleccionado para el puesto de {oferta} en {empresa}."
    ),
    
    CANDIDATO_RECHAZADO(
        "Tu postulación ha sido rechazada",
        "Lamentablemente, tu postulación para {oferta} en {empresa} ha sido rechazada."
    ),
    
    CANDIDATO_ABANDONA(
        "Actualización de postulación",
        "{candidato} ha decidido abandonar su postulación para {oferta} en {empresa}."
    ),

    // Eventos / Recordatorios
    EVENTO_ELIMINADO(
        "Tu evento ha sido eliminado",
        "El evento {titulo} para el puesto de {oferta} en {empresa} ha sido eliminado."
    ),
    EVENTO_MODIFICADO_CANDIDATO(
        "Tu evento ha sido modificado",
        "El evento {titulo} para el puesto de {oferta} en {empresa} ha sido modificado. Esta programado para el dia {fecha} a las {horas}."
    ),

    EVENTO_MODIFICADO_EMPRESA(
        "Tu evento ha sido modificado",
        "El evento {titulo} con {candidato} para el puesto de {oferta} ha sido modificado. Esta programado para el dia {fecha} a las {horas}."
    ),
    
    RECORDATORIO_EVENTO_3_DIAS_CANDIDATO(
        "Recordatorio de evento",
        "Tu evento {titulo} con {empresa} esta programado para el dia {fecha} a las {horas}."
    ),
    RECORDATORIO_EVENTO_1_DIA_CANDIDATO(
        "Recordatorio de evento",
        "Tu evento {titulo} con {empresa} esta programado para hoy a las {horas}."
    ),

    RECORDATORIO_EVENTO_3_DIAS_EMPRESA(
        "Recordatorio de evento",
        "Tu evento {titulo} con {candidato} para el puesto {oferta} esta programado para el dia {fecha} a las {horas}."
    ),

    RECORDATORIO_EVENTO_1_DIA_EMPRESA(
        "Recordatorio de evento",
        "Tu evento {titulo} con {candidato} para el puesto {oferta} esta programado para hoy a las {horas}."
    ),

    // Otros ?
    ALERTA_GENERAL(
        "Nueva notificación",
        "Has recibido una nueva notificación de {origen}."
    );

    private final String titulo;
    private final String template;

    TipoNotificacion(String titulo, String template) {
        this.titulo = titulo;
        this.template = template;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getTemplate() {
        return template;
    }


}
