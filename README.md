## Welcome to Detaq Backend Repository

This repository was developed using Ktor and colaborated with Supabase and Firebase. As our deployment platform, we use Ubuntu VPS from IDCloudHost.

## How to Run

If you want to try and modify this repository on your own, you have to do few steps.

Prepare a file named **properties.conf** and put it in **sources** folder on your project.

Type this code, and change all credentials with yours.

```plaintext
ktor{
	deployment{
		dev{
			HOST = "127.0.0.1"
			PORT = "8080"
			JDBC_URL = "{JDBC URL HERE}"
			DB_USERNAME = "postgres"
			DB_PASSWORD = "detaqfilkomjaya"
			JWT_SECRET = "{JWT SECRET HERE}"
			JWT_ISSUER = "127.0.0.1:8080/"
			JWT_AUDIENCE = "127.0.0.1:8080/audience"
			JWT_REALM = "127.0.01:8080/JWT_Realm"
			PW_SALT = "{PW SALT HERE}"
			FCM_SERVER_KEY="{FCM KEY HERE}"
		}
	}
	application{
		modules=[com.binbraw]
	}
}
```

**Dont forget to change the branch to "staging" first before run this repository**
