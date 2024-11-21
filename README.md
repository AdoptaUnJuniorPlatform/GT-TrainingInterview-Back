# PROYECTO BACKEND "DORI" (Entrenador de entrevistas)
## Introducción
- En este proyecto, desarrollé el backend para un simulador de entrevistas de trabajo enfocado en roles de diseñador UX/UI, backend y frontend. El objetivo principal fue construir una API eficiente que facilite la gestión de las entrevistas y que permitiese la comunicación fluida con el frontend.

## Tecnologías Utilizadas
- **Java y Spring Boot**
    - Elegí esta combinación por su robustez, escalabilidad y amplia comunidad de soporte. Spring Boot permitió una configuración rápida y la creación de una API REST bien estructurada.
- **PostgreSQL**
    - Base de datos relacional seleccionada por su capacidad para manejar datos complejos y consultas avanzadas.
- **Docker**
    - Para contenerizar el backend, asegurando la portabilidad y facilidad de pruebas en diferentes entornos.
- **GitHub**
    - Utilizado para el control de versiones y la colaboración eficiente con el resto del equipo.

## Proceso de Desarrollo (*main branch*)
- <ins>Diseño de la Arquitectura:</ins>
    - Implementé un modelo basado en capas, separando responsabilidades entre controladores, servicios y repositorios. Diseñé el esquema de la base de datos para almacenar preguntas, respuestas y feedback para el usuario.

- <ins>Configuración de la API:</ins>
    - Configuré los endpoints RESTful para operaciones CRUD relacionadas con los roles y temáticas de cada rol. Implementé validaciones de datos y gestión de errores para mejorar la seguridad y la experiencia del usuario.

- <ins>Integración de PostgreSQL:</ins>
    - Establecí la conexión con la base de datos utilizando Spring Data JPA. Creé entidades y repositorios para mapear y acceder a los datos.

- <ins>Contenerización con Docker:</ins>
    - Escribí un archivo Dockerfile para contenerizar la aplicación backend y poder compartirla con mis compañeros de grupo. Finalmente se usé este contenedor para el despliegue en el servidor

- <ins>Pruebas y Entrega:</ins>
    - Realicé pruebas locales para asegurar el correcto funcionamiento del backend.
    Compartí el proyecto en GitHub, y proporcioné un contenedor Docker listo para pruebas por parte del equipo.

- <ins>Desafíos y Soluciones:</ins>
    - Desafío: Configuración de CORS para permitir solicitudes desde el frontend. Solución: Implementé una configuración en Spring Boot para aceptar solicitudes específicas desde el puerto del frontend.

- <ins>Desafío:</ins>
    - Asegurar la consistencia entre los entornos de desarrollo y producción. Solución: Utilicé Docker para estandarizar el entorno de ejecución.

- <ins>Resultados</ins>
    - El backend está completamente funcional, ofreciendo endpoints seguros y escalables. Gracias a la contenerización, los compañeros del equipo pudieron integrarlo fácilmente con el frontend para pruebas y desarrollo continuo.

- <ins>Repositorio</ins>
    El código fuente del backend está disponible en el repositorio de GitHub, junto con la documentación adicional para su uso y despliegue.
---
## ENDPOINTS
- Post (http://localhost:5172/questionary/loadQuestions)
![image](https://github.com/user-attachments/assets/35540ce4-ef71-4e4f-9152-c6f91b1fccad)

---
# INTEGRACIÓN DE LA APLIACIÓN CON COHERE AI (*develop branch*)
- <ins>Desarrollo con Cohere AI</ins>
    - En una fase inicial del proyecto, investigué la posibilidad de generar preguntas, respuestas y feedback dinámicos mediante el uso de inteligencia artificial. Para ello, seleccioné Cohere AI, una herramienta especializada en procesamiento de lenguaje natural (NLP), que ofrecía la capacidad de trabajar con modelos avanzados para generar contenido relevante.

## Proceso de Integración

- <ins>Definición del Objetivo:<ins>
    - El objetivo era automatizar la creación de preguntas para practicar entrevistas de trabajo, opciones de respuesta y un feedback personalizado basado en la selección del usuario.
    - Esto permitiría que el simulador se adaptara dinámicamente a diferentes contextos sin depender de una base de datos fija.

- <ins>Configuración del Entorno:<ins>
    - Registré una cuenta en Cohere AI y generé una clave de API para acceder a sus servicios (Token).
    - Añadí la dependencia necesaria en el proyecto para interactuar con la API de Cohere.

- <ins>Desarrollo del Modelo:<ins>
    - Diseñé un esquema básico para enviar prompts (indicaciones) a Cohere AI. Por ejemplo:
        - Prompt: "Genera una pregunta técnica para un rol de desarrollador backend con tres opciones de respuesta."
        - Resultado esperado: Cohere devolvía una pregunta técnica, opciones múltiples y un feedback relacionado.
    - mplementé un servicio en Java que se encargaba de construir estos prompts y procesar las respuestas de la API.

- <ins>Pruebas de Generación:<ins>
    - Realicé pruebas con diferentes tipos de prompts:
        - Preguntas técnicas para backend, frontend y diseño UX/UI.
        - Respuestas con opciones correctas e incorrectas.
        - Feedback general basado en la selección del usuario.
    - Ajusté los prompts para obtener resultados más coherentes y específicos según las necesidades del simulador.

- <ins>Resultados Obtenidos<ins>
    - Cohere AI mostró un buen rendimiento para generar contenido textual dinámico y relevante.
    - Al usar la versión gratuita, durante un 20% de las pruebas el resultado esperado no era el deseado, llegando a recibir datos vacíos. Este problema se podría resolver usando una suscripción a Cohere AI (prueba pendiente de realizar por no disponer de dicha suscripción)

- <ins>Limitaciones y Decisión Final<ins>
    - Aunque la integración con Cohere AI ofrecía flexibilidad, el cliente prefirió una solución más predecible y controlada mediante una base de datos con preguntas y respuestas predefinidas.
    - Esta decisión se basó en la necesidad de garantizar una experiencia uniforme y evitar respuestas inesperadas que pudieran surgir de la generación dinámica.

- <ins>Valor del Aprendizaje</ins>
    - Esta fase de investigación permitió:
        - Familiarizarme con el uso práctico de una API de inteligencia artificial.
        - Entender mejor los retos y oportunidades de integrar NLP en aplicaciones web.
        - Sentar las bases para considerar soluciones similares en futuros proyectos.
---
## Ejemplo de código con Cohere AI
~~~Java
public Question getIAResponse(Question question) {

        try {
            // Se genera un prompt aleatorio de una lista de 10 prompts para conseguir variedad en las preguntas
            String prompt = generatePrompt(fileLoader, question, "src/main/resources/prompts/prompts.json", "prompts");
            //Se genera el chatHistory para darle contexto a Cohere para que pueda generar preguntas más precisas
            chatHistory = generateChatHistoryList(fileLoader, question);

            // Se construye un ChatRequest pasándole el prompt inicial y un histórico de preguntas
            NonStreamedChatResponse response = cohere.chat(
                    ChatRequest.builder()
                            .message(prompt + finalPrompt)
                            .chatHistory(
                                    List.of(
                                            Message.user(ChatMessage.builder().message(chatHistory.get(chatHistory.size() - 5)).build()),
                                            Message.user(ChatMessage.builder().message(chatHistory.get(chatHistory.size() - 4)).build()),
                                            Message.user(ChatMessage.builder().message(chatHistory.get(chatHistory.size() - 3)).build()),
                                            Message.user(ChatMessage.builder().message(chatHistory.get(chatHistory.size() - 2)).build()),
                                            Message.user(ChatMessage.builder().message(chatHistory.get(chatHistory.size() - 1)).build())
                                    )
                            )
                            .build()
            );
            Question questionObj = splitResponse(question, response.getText());
            fillChatHistory(questionObj);

            return questionObj;

        } catch (Exception e) {
            // En caso de que Cohere falle, se implementará un método para que recoja el contenido en una base de datos
            return question;
        }
    }
~~~
