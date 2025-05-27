

Adama_UI – Warehouse Management System User Interface

Adama_UI is the JavaFX-based frontend for the Adama warehouse product management system. 
Built with Java 23 and JavaFX 21, it allows users to interact with the backend via a role-based interface to request products, approve them, manage users, and communicate internally.

🔧 Key Features

	•	Menu-based navigation based on user role (admin, manager, user).
	•	Product request and management.
	•	Role-based approval of requests.
	•	User management panel.
	•	Internal messaging system.
	•	REST API connection and JWT authentication.
	•	Contextual interface based on session state.

📁 Project Structure

	•	/src/main/java
	•	/auth – Login logic, session management, and JWT handling.
	•	/products – Controllers for product-related views.
	•	/users – Controllers and DTOs for user views.
	•	/orders – Product request functionality.
	•	/messages – Internal communication logic.
	•	/src/main/resources
	•	/fxml – Interface layouts.
	•	application.css – Styling.

🌐 Backend

This client connects to the backend service hosted at:
https://github.com/Mazer-R/AdamaProject

    To change the backend endpoint, update the constant API_BASE_URL inside the SessionManager class (com.adama_ui.auth.SessionManager).

🛠️ Technologies Used

	•	Java 23
	•	JavaFX 21
	•	Maven
	•	Lombok
	•	Jackson (ObjectMapper)
	•	Java built-in HttpClient

⚙️ Setup & Execution

1.	Clone this repository:
   
  	    git clone https://github.com/Mazer-R/Adama_UI.git
  	
2.	Make sure Java 23 and Maven are properly installed.
3.	Set your backend endpoint in the SessionManager class.
4.	From the root directory, run:
    
            mvn clean javafx:run

Alternatively, open it in your IDE as a Maven project and run the main class directly.

📷 Screenshots:
![Login](https://github.com/Mazer-R/Adama_UI/raw/master/screenshots/login.png)
![Main Screen](https://github.com/Mazer-R/Adama_UI/raw/master/screenshots/main%20screen.png)
![Message](https://github.com/Mazer-R/Adama_UI/raw/master/screenshots/message.png)


![Add Product](https://github.com/Mazer-R/Adama_UI/raw/master/screenshots/add-product.png)
![Product](https://github.com/Mazer-R/Adama_UI/raw/master/screenshots/product.png)






🖥️ Compatibility

Tested on:

	•	Windows 10/1
	•	macOS (Apple Silicon)

============================================================================

Adama_UI – Interfaz de usuario del sistema de gestión de productos de almacén

Adama_UI es la interfaz de usuario del sistema Adama, desarrollada en Java 23 con JavaFX 21. Representa el punto de interacción visual con el backend de gestión de almacenes, permitiendo a los usuarios realizar operaciones como solicitudes de productos, gestión de usuarios, comunicación interna y validación jerárquica según el rol.

🔧 Características principales

	•	Navegación por menús según el rol del usuario (admin, manager, user).
	•	Solicitud y gestión de productos.
	•	Validación de solicitudes por roles superiores.
	•	Gestión de usuarios.
	•	Sistema de mensajería interna.
	•	Conexión con backend mediante API REST y autenticación JWT.
	•	Cambios visuales según el contexto y estado de sesión.

📁 Estructura del proyecto

	•	/src/main/java
	•	/auth – Login, sesión y token manager.
	•	/products – Lógica y controladores para gestión de productos.
	•	/users – Controladores y DTOs relacionados con usuarios.
	•	/orders – Controladores de solicitudes.
	•	/messages – Comunicación interna.
	•	/src/main/resources
	•	/fxml – Archivos de interfaz visual.
	•	application.css – Estilos personalizados.

🌐 Backend

Este cliente se conecta al backend disponible en:

    https://github.com/Mazer-R/AdamaProject

Para cambiar el endpoint de conexión, modifica la constante API_BASE_URL en la clase SessionManager (ubicada en com.adama_ui.auth.SessionManager).

🛠️ Tecnologías utilizadas

	•	Java 23
	•	JavaFX 21
	•	Maven
	•	Lombok
	•	Jackson (ObjectMapper)
	•	HttpClient (java.net)

⚙️ Instalación y ejecución

1.	Clona el repositorio:
 
      	git clone https://github.com/Mazer-R/Adama_UI.git
	      
2.	Asegúrate de tener Java 23 y Maven instalados.
3.	Configura el endpoint si es necesario en SessionManager.
4.	Desde el directorio raíz, ejecuta:

    	mvn clean javafx:run

También puedes abrirlo desde tu IDE (IntelliJ, Eclipse…) como proyecto Maven y lanzar la aplicación desde la clase principal.

📷 Capturas de pantalla

![Login](https://github.com/Mazer-R/Adama_UI/raw/master/screenshots/login.png)
![Main Screen](https://github.com/Mazer-R/Adama_UI/raw/master/screenshots/main%20screen.png)
![Message](https://github.com/Mazer-R/Adama_UI/raw/master/screenshots/message.png)


![Add Product](https://github.com/Mazer-R/Adama_UI/raw/master/screenshots/add-product.png)
![Product](https://github.com/Mazer-R/Adama_UI/raw/master/screenshots/product.png)






🖥️ Compatibilidad

Probado en sistemas operativos:

	•	Windows 10
	•	macOS (Apple Silicon)
