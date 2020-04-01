# Clone app Whatsapp- only for studying
> This app try remake the famous app WhatsApp with Android SDK



### Updates
> View chat WORKING

> Send message WORKING

> Status Upload WORKING

> Create Profile WORKING
<br>


<img src="https://user-images.githubusercontent.com/59422918/78160904-08468600-741b-11ea-90ee-529e29d96b4c.png" alt="alt text" >


## How to use

Windows:

1. Clone the project

2. Create a Firebase Project

3. Sign up SHA1 app key in Firebase Project

4. Replace file "app/YOUR CONFIG FIREBASE FILE HERE.JSON" with your firebase config file

5. Edit rules of Firebase Realtime Datebase:
```bash
"rules": {
    ".read": true, #Edit here
    ".write": true #Edit here
  }
```
6. Edit rules of Firebase Storage
```bash
rules_version = '2' #Current Version;
service firebase.storage {
  match /b/{bucket}/o {
    match /{allPaths=**} {
      allow read, write: if true; #Edit this line
    }
  }
}

```
## Installation 

### APK
Android:
Download the apk [soon](https://github.com/0.apk)


