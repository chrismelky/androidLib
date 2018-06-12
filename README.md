# Android Reusable functionalities
 * Regitration
 * Verification through sms


# Registration Module 

 Register user using mobile number
 
 Verify phone number through SMS
 
 Usange:
 
* 1. Include  jitpack repo to build.gradle
 ```
 allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
 ```
 
* 2. Add  dependence build.gradle
 ```
 dependencies {
               //Regigtration module
	        compile 'com.github.chrismelky.androidLib:registration:1.0.0'
	}
 ```
 
