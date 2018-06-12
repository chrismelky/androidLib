# Android Reusable functionalities
 * Regitration
 * Verification through sms


# Registration Module 

 Register user using mobile number
 
 Verify phone number through SMS
 
 Usange:
 
- 1. Include  jitpack repo to build.gradle
 ```
 allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
 ```
 
- 2. Add  dependence build.gradle
 ```
 dependencies {
               //Regigtration module
	        compile 'com.github.chrismelky.androidLib:registration:1.0.0'
	}
 ```
 
 - 3. Usage in your MainActivity
 
 ```
 Intent registrationIntent = new Intent(this, RegistrationActivity.class);
 //Specify registration POST URL with request Body e.g {'phoneNumber':'12345678'}
 registrationIntent.putExtra("REGISTRATION_URL", "http://your-domain.com/registration-end-point");
 
 //If registration will require sms verification
 registrationIntent.putExtra("SMS_VERIFICATION", true);
 
 //Specify Verification [POST] End Point
 registrationIntent.putExtra("VERIFICATION_URL", "http://your-domain.com/verifification-end-point");
 
 //Specify Re-send SMS verification code End Point
 registrationIntent.putExtra("RESEND_SMS_URL", "http://your-domain.com/resend-verification-code-end-point");
 
 //Start registration Activity
 startActivity(registrationIntent);
 
 finish();
 ```
