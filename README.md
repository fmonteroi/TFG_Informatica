# EasyRift

EasyRift es una aplicación web para consultar y analizar información de *League of Legends*.

La aplicación se encuentra publicada en [easyrift.es](https://easyrift.es). Las siguientes instrucciones permiten ejecutarla en local.

## Instalación

Antes de comenzar es necesario instalar:

- [Docker Desktop](https://www.docker.com/products/docker-desktop/), que incluye Docker y Docker Compose para ejecutar el backend y PostgreSQL.
- [Node.js](https://nodejs.org/en/download), que incluye npm y permite ejecutar el frontend con Vite.

No es necesario instalar Java, Maven ni PostgreSQL por separado, ya que se incluyen en los contenedores de Docker.

También se necesita una clave de la API de Riot Games. Se puede obtener gratuitamente iniciando sesión con una cuenta de Riot en el [Riot Developer Portal](https://developer.riotgames.com/). La clave de desarrollo se desactiva cada 24 horas, por lo que debe generarse de nuevo cuando caduque.

## Configuración

Dentro de `deploy`, se debe copiar `.env.example` como `.env` y completar todas sus variables. El valor de `RIOT_API_KEY` debe contener la clave obtenida en el portal de Riot.

## Ejecución local

La primera vez, desde la carpeta `deploy`, se construyen las imágenes y se inician el backend y PostgreSQL:

```console
docker compose up -d --build
```

Después, desde la carpeta `frontend`, se instalan las dependencias y se inicia el frontend:

```console
npm install
npm run dev
```

En las siguientes ejecuciones no es necesario volver a construir las imágenes ni instalar las dependencias. Desde `deploy` se ejecuta:

```console
docker compose up -d
```

Y desde `frontend`:

```console
npm run dev
```
