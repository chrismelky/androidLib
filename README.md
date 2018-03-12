# Android Libaribries
Include:

# Registration Module 
 Register user using mobile number
 
 Verify phone number through SMS
 
 Usange:
 
 Include  jitpack repo to build.gradle
 
 allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
 
 Add  dependence build.gradle
 
 dependencies {
               //Regigtration module
	        compile 'com.github.chrismelky.androidLib:registration:1.0.0'
	}
 
 
