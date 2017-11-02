# LIQUIDACIONES
Esta aplicación fue generanda usando JHIPSTER 4.10.2

Esta es una aplicación de tipo "microservicio" destinada a ser parte de una arquitectura de microservicios, Click en el siguiente enlace para más información [Trabajando microservicios con JHipster][] 

Esta aplicación esta configurada para Detección de Servicios con Jhipster-Registry. Al lanzar la aplicación no iniciará correctamente si no es capaz de conectarse al Jhipster-Registry en [http://localhost:8761](http://localhost:8761). Para mas información puede leer la documentación [Service Discovery y Configuracion con JHipster-Registry][].

## Desarrollo

Para empezar la aplicación en el perfil developer(desarrollador), simplemente ejecute:

    ./mvnw

## Desplegando para producción

Para optimizar la aplicación SEGURIDAD para producción, ejectute:

    ./mvnw -Pprod clean package

Para asegurarse de que todo funcionó, ejecute:

    java -jar target/*.war


## Testeo

Para iniciar las pruebas de su aplicación, ejecute:

    ./mvnw clean test



[Trabajando microservicios con JHipster]: http://www.jhipster.tech/documentation-archive/v4.10.2/microservices-architecture/
[Service Discovery y Configuracion con JHipster-Registry]: http://www.jhipster.tech/documentation-archive/v4.10.2/microservices-architecture/#jhipster-registry
