# Jakarta EE Traveling

This is a Jakarta EE project that interacts with Gemini Generative Artificial Intelligence

The purpose of this application is to show the tourist and historical sites that can be found on
a certain route. Since Google Maps does not have enough information for each of these places,
we will use Gemini to obtain relevant information about the tourist site.

## Getting Started

### Prerequisites

- [Java SE 21+](https://adoptium.net/?variant=openjdk21)
- [Maven](https://maven.apache.org/download.cgi)
- [Google API Key](https://console.cloud.google.com/apis/credentials)

## About Google API Key

You need to have a Google API Key credential. This can be created at the following link.
And the APIs that need to be activated are the following:

- [Places API](https://console.cloud.google.com/apis/api/places.googleapis.com/overview)
- [Generative Language API](https://console.cloud.google.com/apis/api/generativelanguage.googleapis.com/overview)
- [Routes API](https://console.cloud.google.com/apis/api/routes.googleapis.com/overview)
- [Directions API](https://console.cloud.google.com/apis/api/directions-backend.googleapis.com/overview)
- [Maps JavaScript API](https://console.cloud.google.com/apis/api/maps-backend.googleapis.com/overview)
- [Geocoding API](https://console.cloud.google.com/apis/api/geocoding-backend.googleapis.com/overview)

Then add the environment variable called `GOOGLE_API_KEY` with the value obtained from [APIs & Services](https://console.cloud.google.com/apis/credentials)

This variable will be used in the  [`pom.xml`](pom.xml) file, in the  `docker-mave-plugin` variables configuration. Edit  [`Dockerfile`](Dockerfile) file and set  `GOOGLE_API_KEY` variable, if required. But if it is already defined in the computer's environment variable, and you are going to build the image from Cargo, it is not necessary

## Running the Application

To run the application locally, follow these steps:

1. Open a terminal and navigate to the project's root directory.

2. Make sure you have the appropriate Java version installed. We have tested with Java SE 8, Java SE 11, Java SE 17, and Java SE 21.

3. Execute the following command:

```shell
./mvnw clean package cargo:run -Pcontainer
```

4. Once the runtime starts, you can access the project at http://localhost:8080/traveling


## Building a Docker Image
To build a Docker image for this application follow these steps:

Open a terminal and navigate to the project's root directory. Make sure you have Docker installed and running on your system.
Execute the following Maven command to build the Docker image:

```shell
./mvnw docker:build -Pcontainer
```

This command will build a Docker image for your application.

Once the image is built, you can run a Docker container from the image using the following command:

```shell
docker run -p 8080:8080 traveling:${project.version}
```
Replace traveling:${project.version} with the actual image name and tag.

## Technologies used
- Jakarta EE API
- Microprofile
- Google Maps API

That's it! You have successfully built and run the application in a Docker container.



