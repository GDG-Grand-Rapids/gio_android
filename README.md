#Overview
This project is the android mobile application for the GIO Conference Web Application.

##Running the application
To run the application there are two build flavors to be aware of:

- Local (debug or release):  Runs with the API calls pointing to the locally run instance of Docker.
- Prod (debug or release): Runs with the API calls pointing to the production servers.

###Selecting Build Variants
1. In Android Studio select the "Build Variants" menu option on the left side menu bar
2. Select "Local Debug" for running the local instance
  1. If you are running on an actual device you will need to use something like Charles to setup a proxy to talk to the local docker instances
3. Select "Prod Debug" for running against production services

## Getting Local Data Running
Refer to the [GIO Web page](https://github.com/GDG-Grand-Rapids/gio_web) for further info on how to 
setup and run the local Docker instances needed for the local build flavor.




