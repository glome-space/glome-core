# S6Work


## Development

Before you can build this project, you must install and configure the following dependencies on your machine:

- [Node.js][]
- [Yarn][]
- Java JDK 1.8
- Maven
- Eclipse
-- https://marketplace.eclipse.org/content/tm-terminal



After installing Node, you should be able to run the following command to install development tools.
You will only need to run this command when dependencies change in [package.json](package.json).

    yarn install

We use yarn scripts and [Webpack][] as our build system.


Run the following commands in two separate terminals to create a blissful development experience where your browser
auto-refreshes when files change on your hard drive.

    mvn spring-boot:run
    yarn start

[Yarn][] is also used to manage CSS and JavaScript dependencies used in this application. You can upgrade dependencies by
specifying a newer version in [package.json](package.json). You can also run `yarn update` and `yarn install` to manage dependencies.
Add the `help` flag on any command to see how you can use it. For example, `yarn help update`.

The `yarn run` command will list all of the scripts available to run for this project.

### Service workers

Service workers are commented by default, to enable them please uncomment the following code.

* The service worker registering script in index.html
```
<script>
    if ('serviceWorker' in navigator) {
        navigator.serviceWorker
        .register('./sw.js')
        .then(function() { console.log('Service Worker Registered'); });
    }
</script>
```
* The copy file option in webpack-common.js
```js
{ from: './src/main/webapp/sw.js', to: 'sw.js' },
```
Note: Add the respective scripts/assets in `sw.js` that is needed to be cached.

### Managing dependencies

For example, to add [Leaflet][] library as a runtime dependency of your application, you would run following command:

    yarn add --exact leaflet

To benefit from TypeScript type definitions from [DefinitelyTyped][] repository in development, you would run following command:

    yarn add --dev --exact @types/leaflet

Then you would import the JS and CSS files specified in library's installation instructions so that [Webpack][] knows about them:

Edit [src/main/webapp/app/vendor.ts](src/main/webapp/app/vendor.ts) file:
~~~
import 'leaflet/dist/leaflet.js';
~~~

Edit [src/main/webapp/content/css/vendor.css](src/main/webapp/content/css/vendor.css) file:
~~~
@import '~leaflet/dist/leaflet.css';
~~~

Note: there are still few other things remaining to do for Leaflet that we won't detail here.

### Using angular-cli

You can also use [Angular CLI][] to generate some custom client code.

For example, the following command:

    ng generate component my-component

will generate few files:

    create src/main/webapp/app/my-component/my-component.component.html
    create src/main/webapp/app/my-component/my-component.component.ts
    update src/main/webapp/app/app.module.ts

## Building for production

To optimize the S6Work application for production, run:

    ./mvnw -Pprod clean package

This will concatenate and minify the client CSS and JavaScript files. It will also modify `index.html` so it references these new files.
To ensure everything worked, run:

    java -jar target/*.war

Then navigate to [http://localhost:8080](http://localhost:8080) in your browser.

## Testing

To launch your application's tests, run:

    ./mvnw clean test

### Client tests

Unit tests are run by [Karma][] and written with [Jasmine][]. They're located in [src/test/javascript/](src/test/javascript/) and can be run with:

    yarn test



## Using Docker to simplify development (optional)

You can use Docker to improve your JHipster development experience. A number of docker-compose configuration are available in the [src/main/docker](src/main/docker) folder to launch required third party services.
For example, to start a postgresql database in a docker container, run:

    docker-compose -f src/main/docker/postgresql.yml up -d

To stop it and remove the container, run:

    docker-compose -f src/main/docker/postgresql.yml down

You can also fully dockerize your application and all the services that it depends on.
To achieve this, first build a docker image of your app by running:

    ./mvnw package -Pprod docker:build

Then run:

    docker-compose -f src/main/docker/app.yml up -d

## Generating Enities

https://github.com/mraible/jhipster4-demo#generate-entities


## Links

### hterm

hterm is a JS library that provides a terminal emulator.
https://chromium.googlesource.com/apps/libapps/+/HEAD/hterm

Terminal in browser over http/https.
https://github.com/krishnasrinivas/wetty

hterm.html in one file with web sockets
https://github.com/progrium/envy/blob/master/pkg/hterm/assets/hterm.html

Relay Server for the Secure Shell Chromium plugin
https://github.com/zyclonite/nassh-relay/wiki/Manual

https://github.com/x-miz-saka/Hyper

Eduterm
https://github.com/awesomescot/eduterm-frontend/blob/05ab7ef4ba903d0c281294a170d7c18b1ed1cfa5/src/app/terminal/terminal.component.ts

### java-based term

A small PTY interface for Java.
https://github.com/jawi/JPty

Pure Java Terminal Emulator. Works with SSH and PTY.
https://github.com/JetBrains/jediterm

Terminal plug-in for Eclipse
https://code.google.com/archive/p/elt/

Ajaxterm for Java
http://ajaxterm4j.kohsuke.org/

[Node.js]: https://nodejs.org/
[Yarn]: https://yarnpkg.org/
[Webpack]: https://webpack.github.io/
[Angular CLI]: https://cli.angular.io/
[Leaflet]: http://leafletjs.com/
[DefinitelyTyped]: http://definitelytyped.org/

### WebSocket

https://github.com/spring-guides/gs-messaging-stomp-websocket

http://devsullo.com/github/angular2-stomp-over-websocket-service/

https://github.com/stomp-js/stomp-websocket
