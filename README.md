# Availability service
## Description
Microservice that is a part of AppointmentManager app. Its job create, delete and update availability data.

## Api endpoints
- Create availability:
  - path: /api/v1/availability/{doctorId}
  - method: POST
  - description: receives doctor id as path variable and two timestamps that represent start time and end time of doctors availability. Service then creates an availability entry for every hour between those timestamps.

- Get availability for selected doctor:
  - path: /api/v1/availability/{doctorId}
  - method: GET
  - description: receives doctor id as path variable and returns an array of availability times for the provided doctor id.

- Delete availability:
  - path: /api/v1/availability/{availabilityId}
  - method: DELETE
  - description: receives availability id as path variable and deletes availabiliy for the specified id if the it exists.

- Get availability info:
  - path: /api/v1/availability/{availabilityId}
  - method: GET
  - description: receives availability id as path variable and returns the availability object that contains start end end times.

- Book availability:
  - path: /api/v1/availability/book/{availabilityId}
  - method: GET
  - description: marks the availability as booked if its not booked and returns true on success 
  - note: used in the appointment service when creating an appointment.
 
- Reset availability:
  - path: /api/v1/availability/reset/{availabilityId}
  - method: GET
  - description: marks the availability as free if its not free and returns true on success 
  - note: used in the appointment service when deleting an appointment.
