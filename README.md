# GattClient
An Android Bluetooth Low Energy ( BLE ) GATT Client. Used to scan and connect to GATT Servers on nearby BLE Devices.

How to Setup Project:
	Clone or Download Repository from GitHub.
	In Android Studio, Open the downloaded folder as an existing project.

Below are a series of screenshots demonstrating how the UI if formatted. 

On Start Scan Clicked - Found BLE Devices will be added to the "BLE Device Address" List.

On Device Address Clicked - Attempts to connect to the GATT Server on the device.
			    If any services are found their UUIDs are added to the "Service UUID List"

On Service UUIDs Clicked - Reads the available characteristics in the selected service.
			    If any characteristics are found their UUIDs are added to the Characteristics UUID List"

![Alt text](pics/img1.png?raw=true)
![Alt text](pics/img2.png?raw=true)
![Alt text](pics/img3.png?raw=true)
![Alt text](pics/img4.png?raw=true)
![Alt text](pics/img5.png?raw=true)
