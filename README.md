# Scaffold Reusos

## Propósito

Este repositorio es un proyecto de ejemplo para que el equipo practique **Spring Boot (WebFlux)** y el plugin
[Scaffold Clean Architecture](https://bancolombia.github.io/scaffold-clean-architecture/docs/intro). Para eso, el
proyecto se conecta a servicios de *mocking* locales que se apoyan en `Docker Compose` para montar fácilmente estos servicios.

---

## ¿Por qué usar mocks?

Por lo general, al momento de aprender a usar el plugin de scaffold, creamos unos servicios de mocking muy sencillos que
puede que no nos permitan evaluar todas las capacidades de que este tiene, o intentamos conectarnos a sistemas reales que
pueden ser inestables, tardar en responder, requerir credenciales o autenticaciones que no siempre tenemos disponibles.
Los servicios de mocking permiten:

* Probar integración HTTP/REST de forma determinista.
* Aislar el aprendizaje de Spring WebFlux y del plugin scaffold.

---

## Qué contiene este README

1. Diagrama y artefactos: el archivo `scaffold-reusos.drawio` contiene el diagrama de contexto (C4), de componentes (C4) y el diagrama de actividades (UML) usados para diseñar los mocks y las rutas. Revisa ese archivo para entender el flujo completo.
2. Lista de servicios de mocking que se levantan con `docker-compose`.
3. `docker-compose.yml` para levantar los servicios de mock y otros servicios adicionales.
4. Instrucciones para inicializar los mocks
5. Colecciones de postman con ejemplos de conexión a todos los servicios del proyecto. Las colecciones se encuentran en la carpeta `./postman/`
6. Un ejemplo finalizado de un proyecto de scaffold que se conecta con los mocks

---

## Servicios de mocking (nombres según diagrama)

Los mocks reproducen la funcionalidad mínima necesaria para que la aplicación y el scaffolding practiquen llamadas entre
capas. Los nombres aparecen en los diagramas y se reflejan como servicios HTTP:

- **Natural Person Transaction Payments**: Endpoint de pago para personas naturales (El servicio de scaffold que se quiere construir con el curso).
- **Natural Person Account Management**: Servicio REST de consulta de cuentas de personas naturales con información de los saldos. También permite actualizar los saldos de las cuentas.
- **Natural Person Data Information**: Servicio REST de consulta de personas naturales asociadas a las cuentas del servicio **Natural Person Account Management**.
- **Internal Transaction Management**: Servicio en MQ y trama plana para realizar y consultar transacciones internas.
- **JWKS Provider**: Servicio que provee archivos estáticos sobre http. Aquí pondremos nuestros archivos JWKS para poder firmar y validar tokens JWT. este servicio expone una carpeta (`./secret-files` por defecto) en donde vamos a subir nuestros JWKS

---

## Dependencias

- Docker o Podman
- Docker compose
- Postman
- Java 21
- Gradle
- OpenSSL
- WSL si estamos trabajando desde Windows

## Inicialización de los mocks y otros servicios

Este proyecto está configurado para que todos los mocks y servicios extra se ejecuten por medio de Docker, por lo que
para poder inicializar los servicios, debemos ejecutar los siguientes comandos:

**Construir las imágenes del proyecto:**
```sh
docker compose build
```

**Iniciar los contenedores del proyecto:**
```sh
docker compose up -d
```
> [!NOTE]
> 
> Para detener los contenedores una vez ya no se estén usando:
> ```sh
> docker compose down
> ```

### Configuración de los certificados

Para la firma y validación de los tokens JWT, vamos a usar JWKS las cuales vamos a crear a partir de claves `.pem`,
por lo que vamos a partir desde la generación de las claves pem:

```sh
# 1. Generar clave privada en formato 2048-bit RSA
openssl genpkey -algorithm RSA -out jwt_private.pem -pkeyopt rsa_keygen_bits:2048

# 2. Extraer la clave pública
openssl rsa -pubout -in jwt_private.pem -out jwt_public.pem
```
Posteriormente, vamos a generar un JWK desde el link https://russelldavies.github.io/jwk-creator/ con los siguientes datos:
- Clave: el contenido del archivo `jwt_public.pem`
- KID: reusos-scaffold-tutorial
- Algorithm: RS256
- Public key use: (unspecified)

Y deberíamos quedar con un JWK en formato JSON similar a este:

```json
{
  "kty": "RSA",
  "n": "kFPtdVOklzpKVlk5kyAMR6sUAo6zeW1VbxR...",
  "e": "AQAB",
  "alg": "RS256",
  "kid": "reusos-scaffold-tutorial"
}
```

El cual tenemos que transformar en un JWKS usando el formato `{"keys": [<JWK_OUTPUT>]}`, por lo que deberíamos quedar
con un JSON similar a este:

```json
{
  "keys":[
    {
      "kty": "RSA",
      "n": "kFPtdVOklzpKVlk5kyAMR6sUAo6zeW1VbxR...",
      "e": "AQAB",
      "alg": "RS256",
      "kid": "reusos-scaffold-tutorial"
    }
  ]
}
```
Y vamos a guardar el JWKS en la carpeta `./secret-files` y le vamos a poner el nombre `jwks.json`. Si se le pone un nombre
diferente al archivo, hay que modificar las variables de entorno `JWKS_URL` en el archivo `docker-compose.yaml` para los
siguientes servicios:
- natural-person-account-management
- natural-person-data-information

---

## Troubleshooting

* Si alguno de los puertos de los servicios de mocking ya está en uso, cambia los puertos en `docker-compose.yml` o apaga servicios locales.