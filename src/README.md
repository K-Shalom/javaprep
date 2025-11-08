# One Click Partition Creator and Storage Manager

## Project Description

One Click Partition Creator and Storage Manager is a LAN-based, offline-first application that enables users to create and manage storage partitions through a centralized MySQL/MariaDB database hosted on a local server. Users perform all operations via the client app, while only the admin can trigger secure, one-click backups to the cloud (e.g., Google Drive) when internet connectivity is available.

### Key Features

- **LAN-Based Architecture**: Centralized database on local server
- **Offline-First**: Works without internet connectivity
- **Role-Based Access**: Admin and User roles with different permissions
- **Partition Management**: Create, delete, resize, format partitions
- **Activity Logging**: Track all partition operations
- **Cloud Backup**: Admin-only one-click backup feature (coming soon)

## System Requirements

### Software Requirements
- **Java Development Kit (JDK)**: Version 8 or higher
- **MySQL/MariaDB**: Version 5.7 or higher
- **MySQL Connector/J**: JDBC driver for MySQL
- **Operating System**: Windows (for PowerShell partition commands)

### Hardware Requirements
- Minimum 4GB RAM
- Network connectivity for LAN access
- Administrator privileges for partition operations

## Installation & Setup

### 1. Database Setup

#### Install MySQL/MariaDB
1. Download and install MySQL or MariaDB from official website
2. During installation, set root password (or leave empty for default)
3. Start MySQL service

#### Create Database
1. Open MySQL command line or phpMyAdmin
2. Run the SQL script to create database and tables:
   ```bash
   mysql -u root -p < onclick_db.sql
   ```
   Or import `onclick_db.sql` through phpMyAdmin

#### Create Default Admin User
Run this SQL command to create a default admin account:
```sql
INSERT INTO users (username, password, role) VALUES ('admin', 'admin123', 'ADMIN');
```

**Important**: Change the default password after first login!

### 2. Configure Database Connection

Edit the `config/database.properties` file:

```properties
# Database URL (change localhost to your server IP for LAN access)
db.url=jdbc:mysql://localhost:3306/onclick_db

# Database Username
db.user=root

# Database Password
db.password=your_password_here
```

**For LAN Access**: Replace `localhost` with your server's IP address (e.g., `192.168.1.100`)

### 3. Add MySQL Connector to Project

#### Option A: Using Maven (Recommended)
Add to your `pom.xml`:
```xml
<dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
    <version>8.0.33</version>
</dependency>
```

#### Option B: Manual Download
1. Download MySQL Connector/J from: https://dev.mysql.com/downloads/connector/j/
2. Extract the JAR file
3. Add to your project's classpath:
   - In IntelliJ IDEA: File → Project Structure → Libraries → Add JAR
   - In Eclipse: Right-click project → Build Path → Add External Archives

### 4. Compile and Run

#### Using IDE (IntelliJ IDEA / Eclipse)
1. Open the project in your IDE
2. Ensure all dependencies are resolved
3. Run `LoginForm.java` as the main class

#### Using Command Line
```bash
# Compile
javac -cp ".;mysql-connector-java-8.0.33.jar" src/**/*.java -d bin

# Run
java -cp "bin;mysql-connector-java-8.0.33.jar" gui.LoginForm
```

## Project Structure

```
oneclickFull APP (user and admin)/
├── src/
│   ├── gui/                    # GUI classes
│   │   ├── LoginForm.java      # Login screen
│   │   ├── SignupForm.java     # User registration
│   │   ├── AdminDashboard.java # Admin interface
│   │   └── UserDashboard.java  # User interface
│   ├── models/                 # Data models
│   │   ├── User.java
│   │   ├── Machine.java
│   │   ├── Partition.java
│   │   └── ActivityLog.java
│   ├── dao/                    # Data Access Objects
│   │   ├── UserDAO.java
│   │   ├── MachineDAO.java
│   │   ├── PartitionDAO.java
│   │   └── ActivityLogDAO.java
│   └── database/               # Database utilities
│       └── DatabaseConnection.java
├── config/
│   └── database.properties     # Database configuration
├── onclick_db.sql              # Database schema
└── README.md                   # This file
```

## Database Schema

### Tables

#### users
- `user_id` (INT, Primary Key, Auto Increment)
- `username` (VARCHAR(50), Unique)
- `password` (VARCHAR(100))
- `role` (VARCHAR(10)) - 'ADMIN' or 'USER'

#### machines
- `machine_id` (INT, Primary Key, Auto Increment)
- `user_id` (INT, Foreign Key → users)
- `machine_name` (VARCHAR(50))
- `ip_address` (VARCHAR(15))

#### partitions
- `partition_id` (INT, Primary Key, Auto Increment)
- `machine_id` (INT, Foreign Key → machines)
- `user_id` (INT, Foreign Key → users)
- `drive_letter` (VARCHAR(2))
- `size_gb` (INT)
- `created_date` (DATE)

#### activity_logs
- `log_id` (INT, Primary Key, Auto Increment)
- `user_id` (INT, Foreign Key → users)
- `machine_id` (INT, Foreign Key → machines)
- `action` (VARCHAR(100))
- `log_date` (TIMESTAMP)

## Usage

### First Time Setup

1. **Login as Admin**
   - Username: `admin`
   - Password: `admin123`
   - **Change this password immediately!**

2. **Create User Accounts**
   - Click "Create Account" on login screen
   - Enter username and password
   - New users are created with 'USER' role by default

### Admin Features

- **User Management**: View all users in the system
- **Disk Monitor**: View all drives and unallocated space
- **Partition Operations**:
  - Shrink Volume
  - Create New Volume
  - Format Volume
  - Delete Volume
  - Extend Volume
  - Rename Volume
  - Change Drive Letter
- **Activity Logs**: View all system activities
- **Remote Partition**: Manage partitions on other machines (coming soon)
- **Cloud Backup**: One-click backup to cloud storage (coming soon)

### User Features

- **Disk Monitor**: View local drives and space
- **Partition Operations**: Same as admin (on own machine)
- **Activity Logs**: View personal activity history

### Important Notes

⚠️ **Administrator Privileges Required**: Partition operations require Windows Administrator privileges. Run the application as Administrator.

⚠️ **Data Loss Warning**: Partition operations can result in data loss. Always backup important data before performing any partition operations.

⚠️ **LAN Configuration**: For multi-user LAN access, ensure:
- MySQL is configured to accept remote connections
- Firewall allows MySQL port (default: 3306)
- All clients use the server's IP in database.properties

## Troubleshooting

### Database Connection Failed
- Check if MySQL service is running
- Verify database credentials in `config/database.properties`
- Ensure `onclick_db` database exists
- Check firewall settings for MySQL port

### MySQL JDBC Driver Not Found
- Ensure mysql-connector-java JAR is in classpath
- Download from: https://dev.mysql.com/downloads/connector/j/

### Partition Operations Not Working
- Run application as Administrator
- Check PowerShell execution policy
- Verify Windows Disk Management permissions

### LAN Access Issues
- Verify server IP address in database.properties
- Check MySQL remote access configuration
- Ensure firewall allows MySQL port (3306)
- Test connection: `mysql -h SERVER_IP -u root -p`

## Security Considerations

1. **Change Default Passwords**: Always change default admin password
2. **Use Strong Passwords**: Enforce strong password policy
3. **Secure Database**: Use strong MySQL root password
4. **Network Security**: Use firewall rules to restrict database access
5. **Backup Regularly**: Implement regular database backups
6. **Audit Logs**: Review activity logs regularly

## Future Enhancements

- [ ] Cloud backup integration (Google Drive, OneDrive)
- [ ] Remote partition management over LAN
- [ ] Email notifications for critical operations
- [ ] Advanced user permissions
- [ ] Partition scheduling
- [ ] Disk health monitoring
- [ ] Automated backup scheduling

## Credits

**Project**: One Click Partition Creator and Storage Manager  
**Institution**: Rwanda Polytechnic  
**Year**: 2025

## License

This project is developed for educational purposes at Rwanda Polytechnic.

## Support

For issues and questions, please contact your system administrator or project supervisor.
