Before getting started install gradle
https://docs.gradle.org/current/userguide/command_line_interface.html

### Computing all outputs
It is common in Gradle builds for the build task to designate assembling all outputs and running all checks.

`gradle build`

### Running applications
To run the server, use the "gradle run" command which starts the application on port 8081.

`gradle run`

### Cleaning outputs
You can delete the contents of the build directory using the clean task, though doing so will cause pre-computed outputs to be lost, causing significant additional build time for the subsequent task execution.

`gradle clean`



