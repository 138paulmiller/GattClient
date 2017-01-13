First  - Import the aar into project.
	File -> Project Structure -> (Green Plus) -> Import AAR -> Select sdk-0.15.0.arr

Second - Add the following line to the app modules build.gradle file. (./app/build.gradle)
	compile 'com.estimote:sdk:0.15.0@aar'