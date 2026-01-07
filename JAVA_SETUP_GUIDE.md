# Java Environment Setup Guide

## 🎯 Quick Setup Options

### **Option 1: Install via Winget (Recommended - Easiest)**

You have winget already installed! Run this command in PowerShell:

```powershell
winget install Microsoft.OpenJDK.21
```

**Follow the installation prompts**, then close and reopen PowerShell.

---

### **Option 2: Manual Download & Install**

1. **Download OpenJDK:**
   - Visit: https://adoptium.net/temurin/releases/
   - Select: **Version 21 (LTS)**, **Windows**, **x64**
   - Download the **.msi installer**

2. **Install:**
   - Run the downloaded .msi file
   - **Important:** Check the box "Add to PATH" during installation
   - Complete the installation wizard

3. **Verify Installation:**
   - Open a **NEW** PowerShell window
   - Run: `java -version`
   - Run: `javac -version`

---

### **Option 3: Use Microsoft Build of OpenJDK**

1. **Download:**
   - Visit: https://learn.microsoft.com/en-us/java/openjdk/download
   - Download **Microsoft Build of OpenJDK 21**

2. **Install:**
   - Run the installer
   - Follow installation prompts
   - Ensure "Set JAVA_HOME" option is selected

---

## ✅ Verify Java Installation

After installation, open a **NEW PowerShell window** and run:

```powershell
java -version
javac -version
```

You should see output like:
```
openjdk version "21.0.x" 2024-xx-xx
OpenJDK Runtime Environment...
```

---

## 🔧 If Java is Still Not Recognized

### Manually Set Environment Variables:

1. **Find Java Installation Path:**
   ```powershell
   Get-ChildItem "C:\Program Files\Microsoft" -Filter "jdk*" -Directory
   ```
   Or check: `C:\Program Files\Eclipse Adoptium\`

2. **Set JAVA_HOME (Temporary - Current Session):**
   ```powershell
   $env:JAVA_HOME = "C:\Program Files\Microsoft\jdk-21.x.x.x-hotspot"
   $env:Path += ";$env:JAVA_HOME\bin"
   ```

3. **Set JAVA_HOME (Permanent - All Sessions):**
   ```powershell
   # Run PowerShell as Administrator
   [System.Environment]::SetEnvironmentVariable('JAVA_HOME', 'C:\Program Files\Microsoft\jdk-21.x.x.x-hotspot', 'Machine')
   [System.Environment]::SetEnvironmentVariable('Path', $env:Path + ';C:\Program Files\Microsoft\jdk-21.x.x.x-hotspot\bin', 'Machine')
   ```

4. **Close and Reopen PowerShell** to apply changes.

---

## 🚀 Test Your Part A Implementation

Once Java is installed and verified, compile and run your code:

### Navigate to Part A:
```powershell
cd C:\Users\ZIN-CE-LP\Desktop\IIT\Hospital-Diagnostics-Coordination-System\PartA\src
```

### Compile:
```powershell
javac com/hospital/*.java
```

### Run:
```powershell
java com.hospital.Main
```

---

## 🎓 Alternative: Use VS Code Java Extension

If you prefer not to use command line:

1. **Install VS Code Java Extension Pack:**
   - Open VS Code
   - Go to Extensions (Ctrl+Shift+X)
   - Search for "Extension Pack for Java"
   - Click Install

2. **The extension will:**
   - Detect if Java is missing
   - Offer to download and install JDK automatically
   - Configure everything for you

3. **Run your code:**
   - Open `PartA/src/com/hospital/Main.java`
   - Click the "Run" button above the main method
   - Or right-click → "Run Java"

---

## 📝 Quick Installation Steps (Summary)

1. Open PowerShell
2. Run: `winget install Microsoft.OpenJDK.21`
3. Wait for installation to complete
4. **Close and reopen PowerShell** (important!)
5. Verify: `java -version`
6. Navigate to PartA: `cd PartA\src`
7. Compile: `javac com/hospital/*.java`
8. Run: `java com.hospital.Main`

---

## 🐛 Troubleshooting

### "javac is not recognized"
- Java JRE is installed, but you need JDK
- Make sure you installed JDK (Java Development Kit), not just JRE
- JDK includes javac compiler

### "java -version works but javac doesn't"
- You have JRE but need JDK
- Reinstall with JDK option selected

### Path not updating
- Close **ALL** PowerShell/Command Prompt windows
- Open a fresh PowerShell window
- If still not working, restart your computer

### Permission issues
- Run PowerShell as Administrator for system-wide installation
- Or install for current user only

---

## 💡 Recommended: Microsoft OpenJDK 21

For your assignment, I recommend **OpenJDK 21 (LTS)** because:
- ✅ Long-term support (LTS version)
- ✅ Latest stable features
- ✅ Best compatibility with modern Java code
- ✅ Free and open-source
- ✅ Regular security updates

---

## ✨ After Setup Complete

Once Java is installed, you can:

1. ✅ Compile and run Part A
2. ✅ Start implementing Parts B, C, D
3. ✅ Test different workload scenarios
4. ✅ Collect performance metrics
5. ✅ Complete your assignment

---

**Need help?** After installing Java, just let me know and I'll help you compile and run your Part A implementation! 🚀
