# Navium | Microservicio de Contenedores (ms-contenedores)

## Contexto del Proyecto
En la operativa del **Puerto de Valparaíso**, un contenedor no es solo una caja; es un activo sujeto a estrictas regulaciones internacionales y nacionales. El microservicio `ms-contenedores` actúa como el **"Validador de Confianza"** de la plataforma Navium. 

Su propósito es asegurar que ningún movimiento físico ocurra en el patio si no existe una liberación legal previa, centralizando la trazabilidad documental y física de cada unidad que ingresa al terminal.

---

## Responsabilidades del Microservicio
Este componente de la arquitectura Navium gestiona tres pilares críticos:

1.  **Validación Documental:** Controla de forma segregada el estado del **BL (Bill of Lading)** y del **TATC (Título de Admisión Temporal de Contenedores)**, bloqueando automáticamente el agendamiento de turnos si la carga está retenida por el Servicio Nacional de Aduanas.
2.  **Identificación Unificada:** Implementa el estándar internacional de 11 caracteres (Sigla + Número de serie + Dígito verificador) para asegurar la integridad de los datos en toda la cadena.
3.  **Localización en Patio:** Registra la ubicación dinámica de la carga (Andenes/Zonas de espera), alimentando en tiempo real el mapa de ocupación del Centro de Mando.

---

## Especificaciones Técnicas

* **Runtime:** Java 21.
* **Framework:** Spring Boot 3.x (Spring Web, Spring Data JPA).
* **Base de Datos:** PostgreSQL (Esquema aislado por microservicio).
* **Arquitectura Interna:** Patrón de capas (Controller -> Service -> Repository -> Model).

---

## API Endpoints

Este microservicio expone un CRUD completo y endpoints especializados para la gestión logística:

| Método | Endpoint | Acción | Descripción |
| :--- | :--- | :--- | :--- |
| `POST` | `/api/contenedores` | **Create** | Registro inicial de carga entrante (Manifiesto). |
| `GET` | `/api/contenedores` | **Read (All)** | Listado general de contenedores. |
| `GET` | `/api/contenedores/{id}` | **Read (ID)** | Consulta detallada de estados legales para agendamiento. |
| `GET` | `/api/contenedores/patio` | **Read (Patio)** | Retorna solo ocupación actual. |
| `PUT` | `/api/contenedores/{id}/estado` | **Update (Legal)**| Liberación o retención documental (BL/TATC). |
| `PUT` | `/api/contenedores/{id}/anden` | **Update (Físico)**| Asignación física a un andén específico en terreno. |
| `DELETE` | `/api/contenedores/{id}` | **Delete** | Borrado lógico/físico del registro histórico. |

---

