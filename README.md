# Biblioteca de Literatura Universal

Este es un programa desarrollado en Java utilizando **Spring Boot** que interactúa con la **API de Gutendex** para 
obtener información sobre libros y autores, y luego persiste estos datos en una base de datos para que puedan ser
consultados posteriormente. El programa tiene una estructura de capas muy organizada, lo que facilita su 
mantenimiento y expansión.

## Funcionalidades

El programa ofrece las siguientes funciones:

1. **Buscar libro por título mediante la API de Gutendex**: Permite buscar un libro a través de su título utilizando la API pública de **Gutendex**.
2. **Listar libros registrados**: Muestra una lista de los libros que están almacenados en la base de datos local.
3. **Listar autores disponibles en la base de datos**: Muestra todos los autores registrados en la base de datos.
4. **Buscar listado de libros escritos por un autor mediante la API**: Permite obtener todos los libros escritos por un autor específico a través de la API de **Gutendex**.
5. **Listar libros de un autor mediante la base de datos**: Muestra todos los libros de un autor específico que están almacenados en la base de datos.
6. **Buscar autores vivos en determinado año**: Permite buscar autores que estén vivos en un año específico.
7. **Contar la cantidad de libros disponibles en un idioma**: Permite contar cuántos libros están disponibles en un idioma específico que es proporcionado por el usuario.

## Estructura del Proyecto

La estructura de este programa está organizada en capas de manera que facilita la separación de responsabilidades y el mantenimiento:

src
└── main
└── java
└── com
└── example
└── biblioteca_literatura_universal
├── controller
│   └── BookController.java
├── model
│   ├── Book.java
│   └── Author.java
├── repository
│   ├── BookRepository.java
│   └── AuthorRepository.java
├── service
│   ├── BookService.java
│   └── AuthorService.java
├── serviceImpl
│   ├── BookServiceImpl.java
│   └── AuthorServiceImpl.java
├── MenuService.java
├── HttpClientServiceImpl.java
└── Application.java
└── resources
└── application.properties



### Descripción de las Capas

- **Modelo (`model`)**: Contiene las clases que representan las entidades en la base de datos, como `Book` (libro) y `Author` (autor).

- **Repositorio (`repository`)**: Interfaz que extiende de `JpaRepository` para interactuar con la base de datos y realizar operaciones CRUD sobre las entidades `Book` y `Author`.

- **Servicio (`service`)**: Define las interfaces para las operaciones que deben realizarse, como obtener libros por título, listar autores, etc.

- **Implementación de servicio (`serviceImpl`)**: Contiene las implementaciones concretas de los servicios definidos en la capa anterior. Aquí se encuentra la lógica de negocio, como la búsqueda de libros mediante la API, el listado de autores y libros en la base de datos, entre otras.

- **Controlador (`controller`)**: Maneja las solicitudes que el usuario realiza, ya sea desde la consola o la interfaz de usuario.

- **Configuración y Utilidades (`application.properties`)**: Configuración para la conexión a la base de datos y otros parámetros de la aplicación.

## Flujo de Trabajo

1. **Solicitud a la API de Gutendex**:
    - Inicialmente, el programa solicita información de la API de **Gutendex**, que es una API pública para obtener libros y autores.

2. **Mostrar la Información**:
    - Una vez que se recibe la información de la API, los datos son mostrados en la consola.

3. **Persistencia en la Base de Datos**:
    - Después de mostrar los datos al usuario, la información obtenida de la API se persiste en la base de datos local para su uso futuro.

4. **Consultas Locales**:
    - Cuando se realizan otras acciones, como listar libros registrados o autores disponibles, el programa consulta la base de datos local en lugar de la API.

## Requisitos

Para ejecutar este programa, necesitas:

- **Java 17** o superior.
- **Spring Boot** 2.x.
- **Base de datos PostgreSQL** (configuración en `application.properties`).

## Instalación

1. Clona el repositorio del proyecto:
   ```bash
   git clone <URL_DEL_REPOSITORIO>
