# peanobar-clone
A clone of peanobar made in Spring for the API and Angular for the frontend

# Clone
```sh
git clone --recursive https://github.com/cr3eperall04/peanobar-clone.git
```

# Build
Before building you need to edit peanobar-clone/src/main/resources/application.properties.example with the context path, database url and credentials.
```sh
cd peanobar-clone/
./mvnw clean install
```
The war file will be generated in the peanobar-clone/target/ directory

Use [peanobar.sql](peanobar.sql) to create the database.

default user:
| username | password |
| ----- | ----- |
| admin | admin |
