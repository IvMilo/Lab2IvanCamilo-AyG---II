<h1>Proyecto ChatBot en Java Swing usando la API de Ollama</h1>
    <h2>Descripción</h2>
    <p>
        Este proyecto es una aplicación de escritorio interactiva desarrollada con Java Swing. 
        El chatbot se conecta a la API de Ollama para responder preguntas de los usuarios             mediante solicitudes HTTP. 
        La aplicación ofrece una interfaz de usuario amigable que permite gestionar conversaciones, mantener un historial, iniciar nuevas sesiones y más.
    </p>
    <h2>Requisitos Previos</h2>
    <ul>
        <li><strong>Java Development Kit (JDK)</strong> versión 8 o superior.</li>
        <li><strong>Apache Maven</strong> (opcional para gestión de dependencias).</li>
        <li><strong>IDE recomendado:</strong> NetBeans, Eclipse o IntelliJ IDEA.</li>
        <li><strong>Conexión a la API de Ollama:</strong> Asegúrate de que la API esté disponible en <code>http://localhost:11434</code>.</li>
    </ul>

   <h2>Instalación</h2>
    <ol>
        <li>
            <strong>Clonar el repositorio:</strong>
            <pre><code>git clone https://github.com/IvMilo/Lab2IvanCamilo-AyG---II</code></pre>
        </li>
        <li>
            <strong>Abrir el proyecto en un IDE:</strong>
            <ul>
                <li>NetBeans: Importar el proyecto como un proyecto de Java.</li>
                <li>Eclipse o IntelliJ IDEA: Si usas Maven, importa el proyecto como un proyecto Maven.</li>
            </ul>
        </li>
        <li>
            <strong>Compilar el proyecto:</strong><br>
            Si estás usando Maven:
            <pre><code>mvn clean install</code></pre>
            Si no usas Maven, compila el proyecto directamente desde tu IDE.
        </li>
    </ol>

   <h2>Ejecución</h2>
    <ol>
        <li>Ejecuta la clase principal <code>ChatUi</code> desde tu IDE.</li>
        <li>
            La aplicación mostrará una interfaz que incluye:
            <ul>
                <li><strong>Campo de entrada de texto</strong> para escribir preguntas.</li>
                <li><strong>Botón "Enviar"</strong> para enviar la pregunta al chatbot.</li>
                <li><strong>Área de historial</strong> para mostrar y seleccionar conversaciones previas.</li>
                <li><strong>Botón "NewChat"</strong> para iniciar una nueva conversación.</li>
                <li><strong>Botón "Limpiar"</strong> para borrar todo el historial.</li>
            </ul>
        </li>
    </ol>

   <h2>Uso</h2>
    <ul>
        <li>
            <strong>Enviar una pregunta:</strong><br>
            Escribe tu pregunta en el campo de texto y haz clic en "Enviar". La respuesta se mostrará en el área de conversación.
        </li>
        <li>
            <strong>Historial de conversaciones:</strong><br>
            El historial muestra todas las conversaciones previas. Haz clic en una conversación para visualizarla y continuarla.
        </li>
        <li>
            <strong>Nuevo chat:</strong><br>
            Haz clic en "NewChat" para iniciar una nueva conversación. La conversación actual se guarda automáticamente en el historial.
        </li>
        <li>
            <strong>Limpiar historial:</strong><br>
            Haz clic en "Limpiar" para borrar todo el historial de conversaciones.
        </li>
    </ul>
