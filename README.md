# Configuración de los certificados

Ejecutar los siguientes comandos para crear los certificados necesarios para la generación y validación del JWT

```sh
# 1. Generar clave privada en formato 2048-bit RSA
openssl genpkey -algorithm RSA -out jwt_private.pem -pkeyopt rsa_keygen_bits:2048

# 2. Extraer la clave pública
openssl rsa -pubout -in jwt_private.pem -out jwt_public.pem
```

Crear un JWK desde https://russelldavies.github.io/jwk-creator/ usando la siguiente configuración:
- Clave: `jwt_public.pem`
- KID: reusos-scaffold-tutorial ()
- Algorithm: RS256

Eosteriormente, envolver el JWK resultante en un objeto `{"keys": [<JWK_OUTPUT>]}` y guardar en un archivo `jwks.json`
