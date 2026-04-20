# ColectiGo
Este proyecto permitirá digitalizar, visualizar los recorridos de colectivos y calcular rutas

## Características
- Mapa interactivo de Coyhaique con límites de movimiento.
- Trazado de rutas en color azul celeste cargadas dinámicamente
- Interfaz modularizada siguiendo principios de ingeniería de software.

## Requisitos
- Java 17 o superior.
- Maven.
- MySQL 8.0.

## Cómo ejecutar
1. Clona el repositorio.
2. Ejecuta el script `colectigo.sql` en tu servidor MySQL.
4. Configura `ConexionBD.java` con los datos que correspondan: puerto, user, password (si es que tiene).
5. Ejecuta: `mvn clean compile exec:java -Dexec.mainClass="com.colectigo.Main"`
![Pantallazo de ColectiGo]("ColectiGo imgs/Mapa.png").
![Pantallazo de ColectiGo]("ColectiGo imgs/Linea5_ida.png").
![Pantallazo de ColectiGo]("ColectiGo imgs/Linea5_vuelta.png").
